#!/usr/bin/env python3
"""Fast, dependency-free resource graph and production JAR contracts."""
import argparse
import collections
import json
import pathlib
import tomllib
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]


def require(condition, message):
    if not condition:
        raise ValueError(message)


def read_json(data):
    def pairs(entries):
        result = {}
        for key, value in entries:
            require(key not in result, f"Duplicate JSON key: {key}")
            result[key] = value
        return result
    return json.loads(data, object_pairs_hook=pairs)


def source_resources(root=ROOT):
    resources = {}
    for folder in ("common/src/main/resources", "common/src/generated/resources"):
        for path in sorted((root / folder).rglob("*")):
            if path.is_file():
                name = path.relative_to(root / folder).as_posix()
                require(name not in resources, f"Resource shadowed across source sets: {name}")
                resources[name] = path.read_bytes()
    return resources


def walk(value):
    if isinstance(value, dict):
        for key, child in value.items():
            yield key, child
            yield from walk(child)
    elif isinstance(value, list):
        for child in value:
            yield from walk(child)


def check_resources(resources):
    documents = {name: read_json(data) for name, data in resources.items() if name.endswith(".json")}
    blocks = {pathlib.PurePosixPath(name).stem for name in documents
              if name.startswith("assets/geco/blockstates/")}
    require(bool(blocks), "No Geco blockstates")
    items = {pathlib.PurePosixPath(name).stem for name in documents
             if name.startswith("assets/geco/models/item/")}
    language = documents["assets/geco/lang/en_us.json"]
    for block in sorted(blocks):
        require(block in items, f"Missing inventory model: {block}")
        require(f"block.geco.{block}" in language, f"Missing translation: {block}")
        require(f"data/geco/loot_table/blocks/{block}.json" in documents, f"Missing loot: {block}")
    for name, document in documents.items():
        if name.startswith("assets/geco/"):
            for key, value in walk(document):
                if key in {"model", "parent"} and isinstance(value, str) and value.startswith("geco:"):
                    require("assets/geco/models/" + value[5:] + ".json" in documents, f"{name}: missing model {value}")
                if key == "textures":
                    for texture in value.values():
                        if texture.startswith("geco:"):
                            require("assets/geco/textures/" + texture[5:] + ".png" in resources,
                                    f"{name}: missing texture {texture}")
        if name.startswith("data/"):
            for key, value in walk(document):
                if key in {"item", "id", "name", "block"} and isinstance(value, str) and value.startswith("geco:"):
                    require(value[5:] in items, f"{name}: unknown item/block {value}")
            if "/tags/" in name:
                require(document.get("replace", False) is False, f"{name}: replaces vanilla tag")
                for entry in document["values"]:
                    value = entry if isinstance(entry, str) else entry["id"]
                    if value.startswith("geco:"):
                        require(value[5:] in items, f"{name}: unknown tag member {value}")
                    if value.startswith("#geco:"):
                        kind = name.split("/tags/", 1)[1].split("/", 1)[0]
                        require(f"data/geco/tags/{kind}/{value[6:]}.json" in documents,
                                f"{name}: missing nested tag {value}")
    for name in ("cream_marble", "multicolor_marble", "ebony_trees"):
        placed = documents[f"data/geco/worldgen/placed_feature/{name}.json"]
        require(placed["feature"] == f"geco:{name}", f"Wrong configured feature: {name}")
        configured = documents[f"data/geco/worldgen/configured_feature/{name}.json"]
        expected = "geco:ebony_template_tree" if name == "ebony_trees" else "geco:marble"
        require(configured["type"] == expected, f"Wrong feature codec: {name}")
        modifiers = placed["placement"]
        require(modifiers[-1]["type"] == "minecraft:biome", f"Missing biome filter: {name}")
        rarity = next(x for x in modifiers if x["type"] == "minecraft:rarity_filter")
        require(rarity["chance"] == (4 if name == "ebony_trees" else 100), f"Wrong rarity: {name}")
        if name != "ebony_trees":
            height = next(x["height"] for x in modifiers if x["type"] == "minecraft:height_range")
            require(height == {"type": "minecraft:uniform", "min_inclusive": {"absolute": -60},
                               "max_inclusive": {"absolute": 60}}, f"Wrong deposit height: {name}")
    require("data/minecraft/dimension/overworld.json" not in resources, "Global Overworld override")
    require(not any("/worldgen/structure" in name for name in resources), "Obsolete jigsaw structures")
    for model in range(1, 5):
        require(f"data/geco/structure/ebony_tree_m{model}.nbt" in resources, "Missing tree schematic")
    def parent_chain(name, seen):
        require(name not in seen, f"Cyclic model parents: {name}")
        parent = documents[name].get("parent", "")
        if parent.startswith("geco:"):
            parent_chain("assets/geco/models/" + parent[5:] + ".json", seen | {name})
    for name in documents:
        if "/models/" in name:
            parent_chain(name, set())
    return len(documents), len(blocks)


def check_jar(path, loader, version):
    require(loader in {"fabric", "neoforge"}, "Unsupported loader")
    with zipfile.ZipFile(path) as jar:
        names = jar.namelist()
        duplicate = [name for name, count in collections.Counter(names).items() if count > 1]
        require(not duplicate, f"{path}: duplicate ZIP entries {duplicate}")
        resources = {name: jar.read(name) for name in names if not name.endswith("/")}
    check_resources(resources)
    require("geco_LICENSE" in resources, f"{path}: missing license")
    require(b"MIT License" in resources["geco_LICENSE"], f"{path}: wrong license")
    require("com/nstut/geco/common/worldgen/TemplateTreePlacement.class" in resources, "Shared code not bundled")
    require(not any("GecoChunkGenerator" in x or "/test/" in x or x.endswith("Test.class") for x in names),
            "Dead generator or test classes shipped")
    for name, data in resources.items():
        if name.endswith((".json", ".toml")):
            require(bytes([36, 123]) not in data, f"{path}: unresolved metadata placeholder in {name}")
    if loader == "fabric":
        metadata = read_json(resources["fabric.mod.json"])
        require(metadata["id"] == "geco" and metadata["version"] == version, "Wrong Fabric identity")
        require(metadata["license"] == "MIT", "Wrong Fabric license")
        require(metadata["depends"]["minecraft"] == "1.21.1", "Wrong Fabric MC range")
        require({"fabric-api", "architectury", "fabricloader", "java"} <= metadata["depends"].keys(), "Missing Fabric dependency")
        if "icon" in metadata:
            require(metadata["icon"] in resources, "Fabric icon is missing")
        for entries in metadata["entrypoints"].values():
            for entry in entries:
                require(entry.replace(".", "/") + ".class" in resources, f"Missing Fabric entrypoint {entry}")
        require("META-INF/neoforge.mods.toml" not in resources, "NeoForge metadata in Fabric")
    else:
        metadata = tomllib.loads(resources["META-INF/neoforge.mods.toml"].decode())
        require(metadata["mods"][0]["modId"] == "geco" and metadata["mods"][0]["version"] == version,
                "Wrong NeoForge identity")
        require(metadata["license"] == "MIT", "Wrong NeoForge license")
        if "logoFile" in metadata["mods"][0]:
            require(metadata["mods"][0]["logoFile"] in resources, "NeoForge logo is missing")
        require("com/nstut/neoforge/GecoNeoForge.class" in resources, "Missing NeoForge entrypoint")
        dependencies = {x["modId"]: x for x in metadata["dependencies"]["geco"]}
        require(dependencies["minecraft"]["versionRange"] == "[1.21.1,1.21.2)", "Wrong NeoForge MC range")
        require(all(dependencies[x]["type"] == "required" for x in ("minecraft", "neoforge", "architectury")),
                "Missing NeoForge dependency")
        require("fabric.mod.json" not in resources, "Fabric metadata in NeoForge")
    mixins = read_json(resources["geco.mixins.json"])
    for mixin in mixins.get("mixins", []) + mixins.get("client", []):
        require((mixins["package"] + "." + mixin).replace(".", "/") + ".class" in resources, "Missing mixin class")
    return resources


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--jars", action="store_true")
    args = parser.parse_args()
    count, blocks = check_resources(source_resources())
    print(f"Resource contracts passed: {count} JSON documents, {blocks} blocks")
    if args.jars:
        from release import properties
        version = properties()["mod_version"]
        for loader in ("fabric", "neoforge"):
            path = ROOT / loader / "build/libs" / f"geco-{loader}-{version}.jar"
            check_jar(path, loader, version)
            print(f"Production JAR contracts passed: {path.name}")


if __name__ == "__main__":
    main()
