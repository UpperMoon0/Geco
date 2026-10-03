#!/usr/bin/env python3
"""Release preflight, artifact provenance and resumable publishing checkpoints."""
import argparse
import hashlib
import json
import os
import pathlib
import re
import shutil
import subprocess

from check_resources import ROOT, check_jar, require

LOADERS = ("fabric", "neoforge")
PROJECT_ID = 1296676


def git(*args, optional=False):
    result = subprocess.run(["git", *args], cwd=ROOT, text=True, capture_output=True)
    if result.returncode:
        if optional:
            return None
        raise ValueError(result.stderr.strip())
    return result.stdout.strip()


def properties(text=None):
    text = (ROOT / "gradle.properties").read_text() if text is None else text
    values = {}
    for line in text.splitlines():
        if "=" in line and not line.lstrip().startswith("#"):
            key, value = line.split("=", 1)
            require(key.strip() not in values, f"Duplicate property: {key}")
            values[key.strip()] = value.strip()
    version = values.get("mod_version", "")
    require(bool(re.fullmatch(r"(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)", version)),
            "mod_version must be a stable MAJOR.MINOR.PATCH version")
    require(values.get("minecraft_version") == "1.21.1", "Update release targets for new Minecraft version")
    return values


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def identity():
    version = properties()["mod_version"]
    commit = os.environ.get("GITHUB_SHA") or git("rev-parse", "HEAD")
    require(bool(re.fullmatch(r"[a-f0-9]{40}", commit)), "Invalid release commit")
    require(git("rev-parse", "HEAD") == commit, "Checkout differs from release commit")
    return version, commit, f"v{version}"


def guard_tag(tag, commit):
    existing = git("rev-parse", "--verify", f"refs/tags/{tag}^{{commit}}", optional=True)
    require(existing is None or existing == commit, f"Tag {tag} belongs to another commit; bump mod_version")


def output(values):
    text = "".join(f"{key}={value}\n" for key, value in values.items())
    path = os.environ.get("GITHUB_OUTPUT")
    if path:
        with open(path, "a") as stream:
            stream.write(text)
    print(text, end="")


def plan():
    version, commit, tag = identity()
    before = os.environ.get("BEFORE_SHA", "")
    previous = None
    if re.fullmatch(r"[a-f0-9]{40}", before) and before != "0" * 40:
        old = git("show", f"{before}:gradle.properties", optional=True)
        if old:
            previous = properties(old)["mod_version"]
    existing = git("rev-parse", "--verify", f"refs/tags/{tag}^{{commit}}", optional=True)
    manual = os.environ.get("GITHUB_EVENT_NAME") == "workflow_dispatch"
    release = manual or previous != version or existing is None
    # Ordinary commits at an already released version are CI-only.
    if previous == version and existing is not None and not manual:
        release = False
    if release:
        guard_tag(tag, commit)
        changelog = ROOT / f"changelogs/{tag}.md"
        require(changelog.is_file() and changelog.read_text().strip(), f"Missing changelog: {changelog.name}")
    output({"release": str(release).lower(), "version": version, "tag": tag,
            "changelog": f"changelogs/{tag}.md"})


def package(directory):
    version, commit, _ = identity()
    directory.mkdir(parents=True, exist_ok=True)
    require(not list(directory.iterdir()), "Release staging directory must be empty")
    files = {}
    for loader in LOADERS:
        name = f"geco-{loader}-{version}.jar"
        source = ROOT / loader / "build/libs" / name
        check_jar(source, loader, version)
        shutil.copyfile(source, directory / name)
        files[name] = {"loader": loader, "sha256": digest(source)}
    manifest = {"schema": 1, "project_id": PROJECT_ID, "version": version,
                "minecraft": "1.21.1", "commit": commit, "files": files}
    (directory / "manifest.json").write_text(json.dumps(manifest, indent=2, sort_keys=True) + "\n")
    (directory / "SHA256SUMS").write_text("".join(f"{files[name]['sha256']}  {name}\n" for name in sorted(files)))
    verify(directory)


def verify(directory):
    version, commit, _ = identity()
    manifest = json.loads((directory / "manifest.json").read_text())
    require(manifest.get("schema") == 1 and manifest.get("project_id") == PROJECT_ID, "Wrong release project/schema")
    require(manifest.get("version") == version and manifest.get("commit") == commit, "Artifact provenance mismatch")
    require(manifest.get("minecraft") == "1.21.1", "Wrong release Minecraft target")
    expected = {f"geco-{loader}-{version}.jar" for loader in LOADERS}
    require(set(manifest["files"]) == expected, "Release must contain exactly both production JARs")
    actual = {path.name for path in directory.glob("*.jar")}
    require(actual == expected, "Unexpected/missing JAR in release artifact")
    for loader in LOADERS:
        name = f"geco-{loader}-{version}.jar"
        require(manifest["files"][name]["loader"] == loader, "Wrong loader in manifest")
        require(digest(directory / name) == manifest["files"][name]["sha256"], f"Artifact checksum mismatch: {name}")
        check_jar(directory / name, loader, version)
    sums = "".join(f"{manifest['files'][name]['sha256']}  {name}\n" for name in sorted(expected))
    require((directory / "SHA256SUMS").read_text() == sums, "SHA256SUMS differs from manifest")
    print(f"Verified both release JARs for {commit}")


def gh(*args):
    return subprocess.run(["gh", *args], cwd=ROOT, text=True, check=True, capture_output=True).stdout.strip()


def prepare(directory):
    verify(directory)
    version, commit, tag = identity()
    git("fetch", "origin", "--tags")
    guard_tag(tag, commit)
    # List via an authenticated API call: an authentication/network error cannot be mistaken for a missing release.
    releases = json.loads(gh("api", "--paginate", "--slurp", f"repos/{os.environ['GITHUB_REPOSITORY']}/releases"))
    existing = next((release for page in releases for release in page if release["tag_name"] == tag), None)
    if existing:
        saved = directory / "existing"
        saved.mkdir(exist_ok=True)
        gh("release", "download", tag, "--pattern", "manifest.json", "--pattern", "SHA256SUMS",
           "--dir", str(saved), "--clobber")
        require((saved / "manifest.json").read_bytes() == (directory / "manifest.json").read_bytes(),
                "Existing release has different provenance or bytes; bump mod_version")
        require((saved / "SHA256SUMS").read_bytes() == (directory / "SHA256SUMS").read_bytes(),
                "Existing release checksums differ")
        # Never replace existing release bytes; verify the JAR assets themselves too.
        gh("release", "download", tag, "--pattern", "*.jar", "--dir", str(saved), "--clobber")
        verify(saved)
    else:
        if git("rev-parse", "--verify", f"refs/tags/{tag}", optional=True) is None:
            git("config", "user.name", "github-actions[bot]")
            git("config", "user.email", "41898282+github-actions[bot]@users.noreply.github.com")
            git("tag", "-a", tag, commit, "-m", f"Geco {tag}")
            git("push", "origin", f"refs/tags/{tag}")
        gh("release", "create", tag, "--verify-tag", "--draft", "--title", f"Geco {tag}",
           "--notes-file", f"changelogs/{tag}.md")
        gh("release", "upload", tag, *[str(directory / name) for name in
           ("manifest.json", "SHA256SUMS", f"geco-fabric-{version}.jar", f"geco-neoforge-{version}.jar")])
    assets = json.loads(gh("release", "view", tag, "--json", "assets"))["assets"]
    names = {asset["name"] for asset in assets}
    for loader in LOADERS:
        receipt_name = f"curseforge-{loader}.json"
        done = receipt_name in names
        if done:
            gh("release", "download", tag, "--pattern", receipt_name, "--dir", str(directory), "--clobber")
            validate_receipt(json.loads((directory / receipt_name).read_text()), loader, directory)
        output({f"{loader}_published": str(done).lower()})


def validate_receipt(receipt, loader, directory):
    manifest = json.loads((directory / "manifest.json").read_text())
    name = f"geco-{loader}-{manifest['version']}.jar"
    require(receipt.get("project_id") == PROJECT_ID and receipt.get("commit") == manifest["commit"]
            and receipt.get("loader") == loader and receipt.get("sha256") == manifest["files"][name]["sha256"],
            f"Invalid CurseForge receipt: {loader}")
    require(str(receipt.get("file_id", "")).isdigit() and int(receipt["file_id"]) > 0, "Invalid CurseForge file ID")


def receipt(directory, loader):
    verify(directory)
    version, commit, tag = identity()
    file_id = os.environ.get("CURSEFORGE_FILE_ID", "")
    data = {"project_id": PROJECT_ID, "commit": commit, "loader": loader, "file_id": file_id,
            "sha256": digest(directory / f"geco-{loader}-{version}.jar")}
    validate_receipt(data, loader, directory)
    path = directory / f"curseforge-{loader}.json"
    path.write_text(json.dumps(data, indent=2, sort_keys=True) + "\n")
    gh("release", "upload", tag, str(path), "--clobber")


def finalize(directory):
    verify(directory)
    _, _, tag = identity()
    for loader in LOADERS:
        validate_receipt(json.loads((directory / f"curseforge-{loader}.json").read_text()), loader, directory)
    gh("release", "edit", tag, "--draft=false")
    print(f"Published GitHub release {tag} after both CurseForge uploads")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("command", choices=["plan", "package", "verify", "prepare", "receipt", "finalize"])
    parser.add_argument("--directory", type=pathlib.Path, default=ROOT / "build/release")
    parser.add_argument("--loader", choices=LOADERS)
    args = parser.parse_args()
    if args.command == "plan":
        plan()
    elif args.command == "receipt":
        require(args.loader, "--loader is required")
        receipt(args.directory, args.loader)
    else:
        globals()[args.command](args.directory)


if __name__ == "__main__":
    main()
