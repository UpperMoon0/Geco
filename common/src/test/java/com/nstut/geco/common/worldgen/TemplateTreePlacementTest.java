package com.nstut.geco.common.worldgen;

import java.lang.reflect.Proxy;
import java.util.*;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TemplateTreePlacementTest {
    static final BlockPos SAPLING = new BlockPos(15, 100, 15);
    static final BlockPos LEAF = SAPLING.offset(1, 2, 0);
    static BlockState SAPLING_STATE;
    @BeforeAll static void bootstrap() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        SAPLING_STATE = Blocks.OAK_SAPLING.defaultBlockState();
        BuiltInRegistries.BLOCK.bindTags(Map.of(
                BlockTags.LOGS, List.of(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.OAK_LOG)),
                BlockTags.LEAVES, List.of(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.OAK_LEAVES))));
    }
    static StructureTemplate template() throws Exception {
        StructureTemplate template = new StructureTemplate();
        template.load(BuiltInRegistries.BLOCK.asLookup(), TagParser.parseTag(
                "{size:[3,3,3],palette:[{Name:'minecraft:oak_log',Properties:{axis:'y'}}," +
                "{Name:'minecraft:oak_leaves',Properties:{distance:'1',persistent:'false',waterlogged:'false'}}]," +
                "blocks:[{pos:[1,0,1],state:0},{pos:[2,2,1],state:1}],entities:[]}"));
        return template;
    }
    static class TestWorld {
        final Map<BlockPos, BlockState> blocks = new HashMap<>();
        final Set<BlockPos> denied = new HashSet<>();
        int maxY = 320;
        boolean loaded = true;
        BlockPos failedWrite;
        final List<BlockPos> notifications = new ArrayList<>();
        TestWorld() {
            blocks.put(SAPLING, SAPLING_STATE);
            blocks.put(SAPLING.below(), Blocks.FARMLAND.defaultBlockState());
        }
        BlockState read(BlockPos pos) { return blocks.getOrDefault(pos, Blocks.AIR.defaultBlockState()); }
        WorldGenLevel level() {
            return (WorldGenLevel) Proxy.newProxyInstance(WorldGenLevel.class.getClassLoader(),
                    new Class<?>[]{WorldGenLevel.class}, (proxy, method, args) -> switch (method.getName()) {
                case "getBlockState" -> read((BlockPos) args[0]);
                case "holderLookup" -> BuiltInRegistries.BLOCK.asLookup();
                case "hasChunk" -> loaded;
                case "ensureCanWrite" -> !denied.contains((BlockPos) args[0]);
                case "isOutsideBuildHeight" -> {
                    int y = args[0] instanceof BlockPos pos ? pos.getY() : (Integer) args[0];
                    yield y < -64 || y >= maxY;
                }
                case "setBlock" -> {
                    BlockPos pos = (BlockPos) args[0];
                    if (pos.equals(failedWrite)) yield false;
                    blocks.put(pos, (BlockState) args[1]);
                    yield true;
                }
                case "neighborShapeChanged", "scheduleTick" -> null;
                case "blockUpdated" -> { notifications.add((BlockPos) args[0]); yield null; }
                case "toString" -> "TestWorld";
                default -> throw new UnsupportedOperationException(method.toString());
            });
        }
    }
    @Test void pastesExactStatesAcrossAChunkBoundaryAndAnchorsTheTrunk() throws Exception {
        TestWorld world = new TestWorld();
        assertTrue(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
        assertEquals(Blocks.OAK_LOG.defaultBlockState(), world.read(SAPLING));
        assertEquals("1", world.read(LEAF).getValue(net.minecraft.world.level.block.LeavesBlock.DISTANCE).toString());
        assertEquals(2, world.notifications.size());
    }
    @Test void rejectsAPlayerObstacleWithoutConsumingTheSapling() throws Exception {
        TestWorld world = new TestWorld(); world.blocks.put(LEAF, Blocks.STONE.defaultBlockState());
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
        assertEquals(SAPLING_STATE, world.read(SAPLING));
        assertEquals(Blocks.STONE.defaultBlockState(), world.read(LEAF));
    }
    @Test void rejectsBlockEntitiesEvenWhenTheyOccupyTheTrunk() throws Exception {
        TestWorld world = new TestWorld(); world.blocks.put(SAPLING, Blocks.CHEST.defaultBlockState());
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), null));
        assertEquals(Blocks.CHEST.defaultBlockState(), world.read(SAPLING));
    }
    @Test void rejectsFluids() throws Exception {
        TestWorld world = new TestWorld(); world.blocks.put(LEAF, Blocks.WATER.defaultBlockState());
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
        assertEquals(SAPLING_STATE, world.read(SAPLING));
    }
    @Test void rejectsInvalidSoil() throws Exception {
        TestWorld world = new TestWorld(); world.blocks.put(SAPLING.below(), Blocks.STONE.defaultBlockState());
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
    }
    @Test void checksTheFullWriteRegionBeforeMutation() throws Exception {
        TestWorld world = new TestWorld(); world.denied.add(LEAF);
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
        assertEquals(SAPLING_STATE, world.read(SAPLING));
    }
    @Test void doesNotLoadMissingChunks() throws Exception {
        TestWorld world = new TestWorld(); world.loaded = false;
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
        assertEquals(SAPLING_STATE, world.read(SAPLING));
    }
    @Test void rejectsHeightOverflow() throws Exception {
        TestWorld world = new TestWorld(); world.maxY = 102;
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
        assertEquals(SAPLING_STATE, world.read(SAPLING));
    }
    @Test void rollsBackSaplingWhenALaterWorldWriteFails() throws Exception {
        TestWorld world = new TestWorld(); world.failedWrite = LEAF;
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, template(), SAPLING_STATE));
        assertEquals(SAPLING_STATE, world.read(SAPLING));
        assertTrue(world.read(LEAF).isAir());
        assertTrue(world.notifications.isEmpty());
    }
    @Test void anEmptyTemplateLeavesTheSaplingIntact() {
        TestWorld world = new TestWorld();
        assertFalse(TemplateTreePlacement.placeTemplate(world.level(), SAPLING, new StructureTemplate(), SAPLING_STATE));
        assertEquals(SAPLING_STATE, world.read(SAPLING));
    }
}
