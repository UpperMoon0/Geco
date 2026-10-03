"""Contract mutation tests: prove that bad resources/packages/releases fail closed."""
import copy
import hashlib
import json
import os
import pathlib
import tempfile
import unittest
import zipfile
from unittest import mock

import check_resources as checks
import release


class ResourceContracts(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.resources = checks.source_resources()

    def mutate(self, path, operation):
        resources = self.resources.copy()
        document = json.loads(resources[path])
        operation(document)
        resources[path] = json.dumps(document).encode()
        return resources

    def test_complete_resource_graph(self):
        documents, blocks = checks.check_resources(self.resources)
        self.assertGreater(documents, 400)
        self.assertEqual(55, blocks)

    def test_missing_inventory_model(self):
        resources = self.resources.copy()
        del resources["assets/geco/models/item/ebony_log.json"]
        with self.assertRaisesRegex(ValueError, "inventory model"):
            checks.check_resources(resources)

    def test_missing_texture(self):
        resources = self.resources.copy()
        del resources["assets/geco/textures/block/ebony_planks.png"]
        with self.assertRaisesRegex(ValueError, "missing texture"):
            checks.check_resources(resources)

    def test_missing_translation(self):
        resources = self.mutate("assets/geco/lang/en_us.json", lambda x: x.pop("block.geco.ebony_log"))
        with self.assertRaisesRegex(ValueError, "translation"):
            checks.check_resources(resources)

    def test_missing_loot(self):
        resources = self.resources.copy()
        del resources["data/geco/loot_table/blocks/ebony_log.json"]
        with self.assertRaisesRegex(ValueError, "loot"):
            checks.check_resources(resources)

    def test_unknown_tag_member(self):
        resources = self.mutate("data/minecraft/tags/block/saplings.json",
                                lambda x: x["values"].append("geco:missing_sapling"))
        with self.assertRaisesRegex(ValueError, "tag member"):
            checks.check_resources(resources)

    def test_unknown_recipe_item(self):
        resources = self.resources.copy()
        resources["data/geco/recipe/broken.json"] = b'{"result":{"id":"geco:missing_item"}}'
        with self.assertRaisesRegex(ValueError, "unknown item"):
            checks.check_resources(resources)

    def test_missing_model(self):
        resources = self.mutate("assets/geco/models/item/ebony_log.json",
                                lambda x: x.update(parent="geco:block/missing"))
        with self.assertRaisesRegex(ValueError, "missing model"):
            checks.check_resources(resources)

    def test_cyclic_model(self):
        resources = self.mutate("assets/geco/models/item/ebony_log.json",
                                lambda x: x.update(parent="geco:item/ebony_log"))
        with self.assertRaisesRegex(ValueError, "Cyclic"):
            checks.check_resources(resources)

    def test_overworld_override(self):
        resources = self.resources.copy()
        resources["data/minecraft/dimension/overworld.json"] = b"{}"
        with self.assertRaisesRegex(ValueError, "Overworld override"):
            checks.check_resources(resources)

    def test_missing_template(self):
        resources = self.resources.copy()
        del resources["data/geco/structure/ebony_tree_m4.nbt"]
        with self.assertRaisesRegex(ValueError, "schematic"):
            checks.check_resources(resources)

    def test_duplicate_json_key(self):
        with self.assertRaisesRegex(ValueError, "Duplicate JSON"):
            checks.read_json('{"parent":"one","parent":"two"}')

    def test_vanilla_tag_replacement(self):
        resources = self.mutate("data/minecraft/tags/block/saplings.json",
                                lambda x: x.update(replace=True))
        with self.assertRaisesRegex(ValueError, "replaces vanilla"):
            checks.check_resources(resources)

    def test_wrong_feature_rarity(self):
        resources = self.mutate("data/geco/worldgen/placed_feature/cream_marble.json",
                                lambda x: x["placement"][0].update(chance=1))
        with self.assertRaisesRegex(ValueError, "rarity"):
            checks.check_resources(resources)


class PackageContracts(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.directory = pathlib.Path(self.temp.name)
        self.resources = checks.source_resources()
        self.resources["geco_LICENSE"] = (checks.ROOT / "LICENSE").read_bytes()
        self.resources["com/nstut/geco/common/worldgen/TemplateTreePlacement.class"] = b"fixture"
        self.resources["com/nstut/fabric/GecoFabric.class"] = b"fixture"
        self.resources["fabric.mod.json"] = json.dumps({
            "id": "geco", "version": "0.3.0", "license": "MIT",
            "depends": {"minecraft": "1.21.1", "fabric-api": "*", "fabricloader": "*",
                        "architectury": "*", "java": ">=21"},
            "entrypoints": {"main": ["com.nstut.fabric.GecoFabric"]}}).encode()
        self.addCleanup(self.temp.cleanup)

    def write(self):
        path = self.directory / "fixture.jar"
        with zipfile.ZipFile(path, "w") as jar:
            for name, data in self.resources.items():
                jar.writestr(name, data)
        return path

    def test_valid_fabric_package(self):
        checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_wrong_version(self):
        with self.assertRaisesRegex(ValueError, "identity"):
            checks.check_jar(self.write(), "fabric", "0.3.1")

    def test_unresolved_placeholder(self):
        self.resources["extra.json"] = bytes([123, 34, 36, 123, 120, 125, 34, 58, 49, 125])
        with self.assertRaisesRegex(ValueError, "placeholder"):
            checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_shared_code_missing(self):
        del self.resources["com/nstut/geco/common/worldgen/TemplateTreePlacement.class"]
        with self.assertRaisesRegex(ValueError, "Shared code"):
            checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_missing_license(self):
        del self.resources["geco_LICENSE"]
        with self.assertRaisesRegex(ValueError, "license"):
            checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_dead_generator_shipped(self):
        self.resources["com/nstut/GecoChunkGenerator.class"] = b"fixture"
        with self.assertRaisesRegex(ValueError, "Dead generator"):
            checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_wrong_loader_metadata(self):
        self.resources["META-INF/neoforge.mods.toml"] = b'license="MIT"'
        with self.assertRaisesRegex(ValueError, "NeoForge metadata in Fabric"):
            checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_missing_icon(self):
        metadata = json.loads(self.resources["fabric.mod.json"])
        metadata["icon"] = "missing.png"
        self.resources["fabric.mod.json"] = json.dumps(metadata).encode()
        with self.assertRaisesRegex(ValueError, "icon"):
            checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_missing_entrypoint(self):
        del self.resources["com/nstut/fabric/GecoFabric.class"]
        with self.assertRaisesRegex(ValueError, "entrypoint"):
            checks.check_jar(self.write(), "fabric", "0.3.0")

    def test_duplicate_zip_entry(self):
        path = self.write()
        with zipfile.ZipFile(path, "a") as jar:
            with self.assertWarns(UserWarning):
                jar.writestr("geco_LICENSE", b"duplicate")
        with self.assertRaisesRegex(ValueError, "duplicate ZIP"):
            checks.check_jar(path, "fabric", "0.3.0")


class ReleaseContracts(unittest.TestCase):
    COMMIT = "a" * 40

    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.directory = pathlib.Path(self.temp.name)
        self.addCleanup(self.temp.cleanup)
        patch = mock.patch.object(release, "identity", return_value=("0.3.0", self.COMMIT, "v0.3.0"))
        patch.start()
        self.addCleanup(patch.stop)
        # These tests exercise provenance and checksums; PackageContracts exercises ZIP contents separately.
        patch = mock.patch.object(release, "check_jar")
        patch.start()
        self.addCleanup(patch.stop)
        self.files = {}
        for loader in release.LOADERS:
            name = f"geco-{loader}-0.3.0.jar"
            path = self.directory / name
            path.write_bytes(loader.encode())
            self.files[name] = {"loader": loader, "sha256": release.digest(path)}
        self.manifest = {"schema": 1, "project_id": 1296676, "version": "0.3.0",
                         "minecraft": "1.21.1", "commit": self.COMMIT, "files": self.files}
        self.write_manifest()
        (self.directory / "SHA256SUMS").write_text(
            "".join(f"{self.files[name]['sha256']}  {name}\n" for name in sorted(self.files)))

    def write_manifest(self):
        (self.directory / "manifest.json").write_text(json.dumps(self.manifest))

    def test_complete_manifest(self):
        release.verify(self.directory)

    def test_tampered_jar(self):
        (self.directory / "geco-fabric-0.3.0.jar").write_bytes(b"modified")
        with self.assertRaisesRegex(ValueError, "checksum mismatch"):
            release.verify(self.directory)

    def test_foreign_commit(self):
        self.manifest["commit"] = "b" * 40
        self.write_manifest()
        with self.assertRaisesRegex(ValueError, "provenance"):
            release.verify(self.directory)

    def test_foreign_project(self):
        self.manifest["project_id"] = 1
        self.write_manifest()
        with self.assertRaisesRegex(ValueError, "project/schema"):
            release.verify(self.directory)

    def test_wrong_minecraft(self):
        self.manifest["minecraft"] = "1.21.2"
        self.write_manifest()
        with self.assertRaisesRegex(ValueError, "Minecraft"):
            release.verify(self.directory)

    def test_sources_jar_not_publishable(self):
        (self.directory / "geco-fabric-0.3.0-sources.jar").write_bytes(b"sources")
        with self.assertRaisesRegex(ValueError, "Unexpected/missing JAR"):
            release.verify(self.directory)

    def test_missing_loader(self):
        (self.directory / "geco-neoforge-0.3.0.jar").unlink()
        with self.assertRaisesRegex(ValueError, "Unexpected/missing JAR"):
            release.verify(self.directory)

    def test_manifest_cannot_add_arbitrary_file(self):
        self.manifest["files"]["../evil.jar"] = {"loader": "fabric", "sha256": "0" * 64}
        self.write_manifest()
        with self.assertRaisesRegex(ValueError, "exactly both"):
            release.verify(self.directory)

    def test_wrong_loader(self):
        self.manifest["files"]["geco-fabric-0.3.0.jar"]["loader"] = "neoforge"
        self.write_manifest()
        with self.assertRaisesRegex(ValueError, "Wrong loader"):
            release.verify(self.directory)

    def test_wrong_checksum_list(self):
        (self.directory / "SHA256SUMS").write_text("tampered")
        with self.assertRaisesRegex(ValueError, "SHA256SUMS"):
            release.verify(self.directory)

    def test_guard_accepts_missing_or_same_commit_tag(self):
        for value in (None, self.COMMIT):
            with mock.patch.object(release, "git", return_value=value):
                release.guard_tag("v0.3.0", self.COMMIT)

    def test_guard_rejects_tag_collision(self):
        with mock.patch.object(release, "git", return_value="b" * 40):
            with self.assertRaisesRegex(ValueError, "another commit"):
                release.guard_tag("v0.3.0", self.COMMIT)

    def test_safe_stable_version_parser(self):
        self.assertEqual("0.3.0", release.properties("mod_version = 0.3.0\nminecraft_version=1.21.1")["mod_version"])
        for version in ("", "v0.3.0", "0.3.0;evil", "../bad", "01.3.0", "0.3.0-beta"):
            with self.subTest(version=version), self.assertRaises(ValueError):
                release.properties(f"mod_version={version}\nminecraft_version=1.21.1")

    def test_duplicate_property(self):
        with self.assertRaisesRegex(ValueError, "Duplicate property"):
            release.properties("mod_version=0.3.0\nmod_version=0.3.1\nminecraft_version=1.21.1")

    def test_receipt_matches_loader_commit_hash_and_file_id(self):
        receipt = {"project_id": 1296676, "commit": self.COMMIT, "loader": "fabric",
                   "file_id": "1234567", "sha256": self.files["geco-fabric-0.3.0.jar"]["sha256"]}
        release.validate_receipt(receipt, "fabric", self.directory)
        for key, value in (("project_id", 1), ("commit", "b" * 40), ("loader", "neoforge"),
                           ("sha256", "0" * 64), ("file_id", ""), ("file_id", "0")):
            broken = receipt | {key: value}
            with self.subTest(key=key), self.assertRaises(ValueError):
                release.validate_receipt(broken, "fabric", self.directory)


if __name__ == "__main__":
    unittest.main()
