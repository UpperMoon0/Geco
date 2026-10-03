"""Harness regressions; run after installing smoke-requirements.txt."""
import io
import pathlib
import queue
import struct
import tempfile
import unittest
import zlib
from unittest import mock

try:
    import smoke_server as smoke
except ImportError:
    smoke = None


@unittest.skipIf(smoke is None, "Install scripts/smoke-requirements.txt to test the runtime harness")
class SmokeHarnessTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = pathlib.Path(self.temp.name)
        self.addCleanup(self.temp.cleanup)

    def test_reads_zlib_region_chunk(self):
        region = self.root / "region"
        region.mkdir()
        payload = b"fixture NBT"
        compressed = zlib.compress(payload)
        header = bytearray(8192)
        header[:4] = ((2 << 8) | 1).to_bytes(4, "big")
        (region / "r.0.0.mca").write_bytes(header + struct.pack(">I", len(compressed) + 1) + b"\x02" + compressed)
        with mock.patch.object(smoke.nbtlib.File, "parse", side_effect=lambda stream: stream.read()):
            self.assertEqual([payload], list(smoke.read_chunks(self.root)))

    def test_rejects_unsupported_region_compression(self):
        region = self.root / "region"
        region.mkdir()
        header = bytearray(8192)
        header[:4] = ((2 << 8) | 1).to_bytes(4, "big")
        (region / "r.0.0.mca").write_bytes(header + struct.pack(">I", 2) + b"\x01x")
        with self.assertRaisesRegex(AssertionError, "compression"):
            list(smoke.read_chunks(self.root))

    def test_decodes_single_palette_sections(self):
        chunk = {"xPos": 0, "zPos": 0, "sections": [
            {"Y": 0, "block_states": {"palette": [{"Name": "minecraft:stone"}]}},
            {"Y": 12, "block_states": {"palette": [{"Name": "geco:ebony_log", "Properties": {"axis": "x"}}]}}]}
        with mock.patch.object(smoke, "read_chunks", return_value=[chunk]):
            blocks, natural = smoke.world_blocks(self.root)
        self.assertEqual(4096, natural)
        self.assertEqual(("geco:ebony_log", {"axis": "x"}), blocks[(0, 200, 0)])

    def test_decodes_signed_packed_longs_without_crossing_word_padding(self):
        # A 17-entry palette requires five bits: each long stores 12 cells plus four padding bits.
        palette = [{"Name": "minecraft:air"}] + [{"Name": f"fixture:{i}"} for i in range(1, 17)]
        indices = [i % 17 for i in range(4096)]
        words = []
        for offset in range(0, len(indices), 12):
            word = sum(value << (i * 5) for i, value in enumerate(indices[offset:offset + 12]))
            word |= 1 << 63  # Make the Java long signed; high padding must be ignored.
            words.append(word - (1 << 64))
        chunk = {"xPos": 0, "zPos": 0, "sections": [{"Y": 12,
                 "block_states": {"palette": palette, "data": words}}]}
        with mock.patch.object(smoke, "read_chunks", return_value=[chunk]):
            blocks, _ = smoke.world_blocks(self.root)
        for index in (1, 11, 12, 1023, 2048, 4095):
            pos = (index & 15, 192 + (index >> 8), (index >> 4) & 15)
            if indices[index]:
                self.assertEqual(f"fixture:{indices[index]}", blocks[pos][0])
            else:
                self.assertNotIn(pos, blocks)

    def test_legacy_restart_uses_the_launcher_and_migrates_codec(self):
        folder = self.root / "fabric/build/smoke-server"
        (folder / "world").mkdir(parents=True)
        class Level(dict):
            def save(self):
                pass
        def level():
            return Level(Data={"WorldGenSettings": {"dimensions": {"minecraft:overworld":
                   {"generator": {"type": smoke.nbtlib.String("minecraft:noise")}}}}})
        original = level()
        process = mock.Mock()
        process.stdout = io.StringIO("Done (1.0s)!\nGECO_REOPEN_OK\nLoaded 1 recipes\nSaved the game\n")
        process.stdin = mock.Mock()
        process.returncode = 0
        process.poll.return_value = 0
        launcher = ["bash", "gradlew", ":fabric:runServer"]
        with mock.patch.object(smoke, "ROOT", self.root), \
             mock.patch.object(smoke.nbtlib, "load", side_effect=[original, level()]), \
             mock.patch.object(smoke.subprocess, "Popen", return_value=process) as launch:
            smoke.restart_legacy("fabric", folder, launcher, 2)
        self.assertEqual(launcher, launch.call_args.args[0])
        self.assertEqual("geco:overworld", str(original["Data"]["WorldGenSettings"]["dimensions"]
                                               ["minecraft:overworld"]["generator"]["type"]))
        self.assertTrue(any("reload\n" in call.args[0] for call in process.stdin.write.call_args_list))


if __name__ == "__main__":
    unittest.main()
