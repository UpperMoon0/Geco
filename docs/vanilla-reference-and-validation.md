# Geco 0.3.0 correctness repair

Reference: MC-Modding-Src commit `2e782f8ea29b04094efc76b5a59f51a7174013ee`, Minecraft 1.21.1 directory.

| Behavior | Vanilla reference | Repair |
| --- | --- | --- |
| Terrain | `NoiseBasedChunkGenerator.java`, `ChunkGenerator.java`, `WorldGenRegion.java` | No dimension override or custom chunk generator. Shared features use seeded RNG, height guards and writable-region checks. Marble retains radius 15 and approximately 2% combined placement attempts. |
| Saved generator | `NoiseBasedChunkGenerator.java`: CODEC | Legacy `geco:overworld` saves decode to vanilla noise terrain; subsequent saves use the vanilla codec. |
| Blockstates | `data/models/BlockModelGenerators.java`: createStairs/createFenceGate/createButton/createTrapdoor | Equivalent rotation tables, checked against original vanilla JSON for every variant and all ten marble stair families. Ebony uses ordinary non-orientable trapdoor models. |
| Stripping | `world/item/AxeItem.java`: getStripped | Fabric StrippableBlockRegistry and NeoForge BlockToolModificationEvent preserve the pillar axis. NeoForge checks the held tool ability. |
| Handmade trees | `StructureTemplate.java`: save/load/placeInWorld | Public template data supplies exact occupied cells. Preflight rejects obstacles, block entities, fluids, invalid soil, unavailable chunks and height overflow. Failed writes roll back. Vanilla placeInWorld returning true does not guarantee all writes succeeded. |
| Saplings | `TreeGrower.java`: growTree | The sapling is replaced only within the validated transaction; failures preserve its original growth stage. |
| Tree variants | Existing ebony_tree_m1 through m4 NBT files | Original NBT files are unchanged. Saplings and natural generation share one list and routine, with no procedural foliage, jigsaw or random leaf removal. |
| Neighbor updates | `StructureTemplate.java`: placement completion | Shape and block updates occur after successful placement. |
| Loot | `BlockLootSubProvider.java`: createDoorTable/createSlabItemTable | Door drops require the lower half. Double slabs drop two and use explosion decay without an extra explosion survival condition. Stripped IDs match the block registry. |
| Wood codecs | `BlockSetType.java`, `WoodType.java` | Registered vanilla OAK behavior types preserve ebony's distinct block, texture and item identities. |
| Datagen | Geco generators | Sorted recursive JSON object keys, preserved arrays, correct output-directory cleanup and propagated failures. |
| Packaging | Loader metadata and Gradle | MIT license, actual repository URLs, explicit Fabric API dependency, 1.21.1 version bounds, fixed plugin versions and release validation. |

## Validation

Use Java 21. Run `bash gradlew clean build`, `bash gradlew runDatagen`, and check that generated resources have no diff. Install `nbtlib==2.0.4`, then run `python scripts/smoke_server.py fabric` and `python scripts/smoke_server.py neoforge`.

The harness uses only each loader's `build/smoke-server` directory. It verifies persisted normal terrain, exact natural and bonemeal-grown tree silhouettes, obstructed saplings, both marble deposits and protected chest/water cells. The smoke launcher uses the loader directly with production-equivalent resources; ordinary IDE runs retain Architectury hot reload.

The one-log-to-one-wood recipes are retained as existing Geco balance. Trapdoors now craft two.

## Existing worlds

Released 0.2.0 worlds retain their vanilla generator. Features apply to newly generated terrain. Existing chunks are not rewritten. Worlds from the unreleased dev generator can decode through the compatibility alias, but already-empty chunks remain empty. Back up those worlds before upgrading and repair affected chunks separately.

Graphical client and shader playtesting are not claimed.
