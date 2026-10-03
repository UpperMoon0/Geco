#!/usr/bin/env python3
"""Boot either loader, exercise generation and sapling growth, then inspect the saved world."""
import argparse
import collections
import io
import os
import signal
import pathlib
import queue
import re
import shutil
import struct
import subprocess
import threading
import time
import zlib

import nbtlib
import numpy as np

ROOT = pathlib.Path(__file__).resolve().parents[1]


def read_chunks(world):
    for region in sorted((world / "region").glob("*.mca")):
        with region.open("rb") as stream:
            locations = stream.read(4096)
            for index in range(1024):
                location = int.from_bytes(locations[index * 4:index * 4 + 4], "big")
                offset = location >> 8
                if not offset:
                    continue
                stream.seek(offset * 4096)
                length = struct.unpack(">I", stream.read(4))[0]
                compression = stream.read(1)[0]
                assert compression == 2, f"Unexpected region compression {compression}"
                yield nbtlib.File.parse(io.BytesIO(zlib.decompress(stream.read(length - 1))))


def world_blocks(world):
    result = {}
    natural_stone = 0
    for chunk in read_chunks(world):
        cx, cz = int(chunk["xPos"]), int(chunk["zPos"])
        for section in chunk["sections"]:
            if "block_states" not in section:
                continue
            sy = int(section["Y"])
            states = section["block_states"]
            palette = states["palette"]
            bits = max(4, (len(palette) - 1).bit_length())
            per_long = 64 // bits
            packed = states.get("data")
            if packed is None:
                indices = np.zeros(4096, dtype=np.int64)
            else:
                unsigned = np.asarray(packed, dtype=np.int64).view(np.uint64)
                shifts = np.arange(per_long, dtype=np.uint64) * bits
                indices = ((unsigned[:, None] >> shifts) & ((1 << bits) - 1)).reshape(-1)[:4096]
            if sy < 2:
                counts = np.bincount(indices.astype(np.int64), minlength=len(palette))
                natural_stone += sum(int(counts[i]) for i, state in enumerate(palette)
                                     if str(state["Name"]) in {"minecraft:stone", "minecraft:deepslate"})
            if not (0 <= cx <= 3 and 0 <= cz <= 3 and 2 <= sy <= 13):
                continue
            decoded = [(str(state["Name"]), {str(k): str(v) for k, v in state.get("Properties", {}).items()})
                       for state in palette]
            for index, state_index in enumerate(indices):
                state = decoded[int(state_index)]
                if state[0] == "minecraft:air":
                    continue
                y = sy * 16 + (index >> 8)
                if 40 <= y <= 216:
                    x, z = cx * 16 + (index & 15), cz * 16 + ((index >> 4) & 15)
                    result[(x, y, z)] = state
    return result, natural_stone


def assert_tree(blocks, root):
    for model in range(1, 5):
        template = nbtlib.load(ROOT / f"common/src/main/resources/data/geco/structure/ebony_tree_m{model}.nbt")
        palette = template["palette"]
        cells = template["blocks"]
        anchor = min((tuple(map(int, cell["pos"])) for cell in cells
                      if str(palette[int(cell["state"])]["Name"]) == "geco:ebony_log"), key=lambda p: p[1])
        expected = {}
        for cell in cells:
            local = tuple(map(int, cell["pos"]))
            target = tuple(root[i] + local[i] - anchor[i] for i in range(3))
            state = palette[int(cell["state"])]
            name = str(state["Name"])
            if name in {"minecraft:air", "minecraft:structure_void"}:
                continue
            expected[target] = name
        # State distances may change through normal leaf updates; occupied cells must remain exact.
        actual = {pos: state[0] for pos, state in blocks.items()
                  if abs(pos[0] - root[0]) <= 6 and abs(pos[2] - root[2]) <= 5 and root[1] <= pos[1] <= root[1] + 14
                  and state[0] in {"geco:ebony_log", "geco:ebony_leaves"}}
        if actual == expected:
            for cell in cells:
                state = palette[int(cell["state"])]
                if str(state["Name"]) != "geco:ebony_log":
                    continue
                local = tuple(map(int, cell["pos"]))
                pos = tuple(root[i] + local[i] - anchor[i] for i in range(3))
                assert blocks[pos][1]["axis"] == str(state["Properties"]["axis"])
            return model
    raise AssertionError(f"Tree at {root} does not match any allowed NBT schematic")


def run(loader, timeout):
    run_dir = ROOT / loader / "build/smoke-server"
    # Delete only the isolated test directory, never any user's normal server directory.
    assert run_dir.resolve().parent == (ROOT / loader / "build").resolve()
    if run_dir.exists():
        shutil.rmtree(run_dir)
    run_dir.mkdir(parents=True)
    (run_dir / "eula.txt").write_text("eula=true\n")
    (run_dir / "server.properties").write_text(
        "level-seed=42\nonline-mode=false\nserver-port=0\nview-distance=2\nsimulation-distance=2\n"
        "spawn-protection=0\nmax-tick-time=120000\n")
    command = ["bash", str(ROOT / "gradlew"), f":{loader}:runServer", "--console=plain", "--no-daemon", "-PgecoSmokeServer"]
    process = subprocess.Popen(command, cwd=ROOT, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                               stderr=subprocess.STDOUT, text=True, bufsize=1, start_new_session=True)
    lines = []
    pending = queue.Queue()
    log = ROOT / loader / "build/smoke-server.log"

    def read_output():
        with log.open("w") as out:
            for line in process.stdout:
                lines.append(line)
                out.write(line)
                out.flush()
                pending.put(line)
        pending.put(None)

    threading.Thread(target=read_output, daemon=True).start()

    def send(command):
        assert process.poll() is None, f"Server exited early; inspect {log}"
        process.stdin.write(command + "\n")
        process.stdin.flush()

    def wait_for(pattern, seconds):
        deadline = time.monotonic() + seconds
        while time.monotonic() < deadline:
            try:
                line = pending.get(timeout=min(1, max(0.01, deadline - time.monotonic())))
            except queue.Empty:
                continue
            if line is None:
                break
            if re.search(pattern, line):
                return
        raise AssertionError(f"Missing log marker {pattern!r}; inspect {log}")

    try:
        wait_for(r'Done \(', timeout)
        print(f"{loader}: dedicated server ready", flush=True)
        send("gamerule randomTickSpeed 0")
        send("forceload add 0 0 63 63")
        deadline = time.monotonic() + 120
        while True:
            send("execute if loaded 16 200 16 if loaded 48 120 48 run say GECO_CHUNKS_READY")
            try:
                wait_for("GECO_CHUNKS_READY", 2)
                break
            except AssertionError:
                if time.monotonic() >= deadline:
                    raise
        for command in [
            "fill 8 199 8 56 199 24 minecraft:dirt",
            "place feature geco:ebony_trees 16 200 16",
            "fill 33 45 33 63 75 63 minecraft:stone",
            "setblock 48 60 48 minecraft:chest",
            "setblock 47 60 48 minecraft:water",
            "place feature geco:cream_marble 48 60 48",
            "fill 33 105 33 63 135 63 minecraft:stone",
            "place feature geco:multicolor_marble 48 120 48",
            "setblock 32 200 16 geco:ebony_sapling[stage=1]",
            "setblock 48 200 16 geco:ebony_sapling[stage=1]",
            "fill 44 201 12 52 214 20 minecraft:stone",
            'setblock 31 200 16 minecraft:dispenser[facing=east]{Items:[{Slot:0b,id:"minecraft:bone_meal",count:64}]}',
            'setblock 47 200 16 minecraft:dispenser[facing=east]{Items:[{Slot:0b,id:"minecraft:bone_meal",count:64}]}',
        ]:
            send(command)
        # Dispenser bonemeal goes through SaplingBlock.performBonemeal/advanceTree on each loader.
        for _ in range(30):
            send("setblock 31 200 15 minecraft:redstone_block")
            send("setblock 47 200 15 minecraft:redstone_block")
            time.sleep(0.4)
            send("setblock 31 200 15 minecraft:air")
            send("setblock 47 200 15 minecraft:air")
            time.sleep(0.2)
        send("save-all flush")
        wait_for(r'Saved the game', 60)
        send("stop")
        process.stdin.close()
        process.wait(timeout=90)
        assert process.returncode == 0, f"Server launcher failed with {process.returncode}; inspect {log}"
    finally:
        if process.poll() is None:
            try:
                send("stop")
                process.wait(timeout=30)
            except Exception:
                os.killpg(process.pid, signal.SIGTERM)
                try:
                    process.wait(timeout=10)
                except subprocess.TimeoutExpired:
                    os.killpg(process.pid, signal.SIGKILL)

    text = "".join(lines)
    for error in ["Couldn't parse loot table", "Unknown registry key", "Failed to load registries", "Exception in server tick loop"]:
        assert error not in text, f"{loader}: {error}; inspect {log}"
    blocks, natural_stone = world_blocks(run_dir / "world")
    assert natural_stone > 10000, f"Overworld terrain is empty: only {natural_stone} natural stone cells"
    assert_tree(blocks, (16, 200, 16))
    assert_tree(blocks, (32, 200, 16))
    assert blocks[(48, 200, 16)][0] == "geco:ebony_sapling", "Obstructed growth consumed its sapling"
    assert blocks[(48, 60, 48)][0] == "minecraft:chest", "Marble replaced a block entity"
    assert blocks[(47, 60, 48)][0] == "minecraft:water", "Marble replaced water"
    counts = collections.Counter(state[0] for state in blocks.values())
    for name in ["geco:cream_marble", "geco:multicolor_marble"]:
        assert counts[name] > 5000, f"{name} did not form a large deposit: {counts[name]}"
    print(f"{loader}: terrain, exact natural/sapling schematics, blocked growth, marble and protected cells passed", flush=True)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("loader", choices=["fabric", "neoforge"])
    parser.add_argument("--timeout", type=int, default=600)
    args = parser.parse_args()
    run(args.loader, args.timeout)
