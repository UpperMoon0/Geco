package com.nstut.geco.common.worldgen;

import java.lang.reflect.Proxy;
import java.util.*;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class MarbleFeatureTest {
    @BeforeAll static void bootstrap() {
        SharedConstants.tryDetectVersion(); Bootstrap.bootStrap();
        BuiltInRegistries.BLOCK.bindTags(Map.of(BlockTags.BASE_STONE_OVERWORLD,
                List.of(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.STONE))));
    }
    static class World {
        final Map<BlockPos, BlockState> initial = new HashMap<>();
        final Set<BlockPos> written = new HashSet<>();
        final Map<BlockPos, BlockState> result = new HashMap<>();
        boolean denyPositiveX;
        int minY = -64;
        WorldGenLevel level() {
            return (WorldGenLevel) Proxy.newProxyInstance(WorldGenLevel.class.getClassLoader(),
                    new Class<?>[]{WorldGenLevel.class}, (proxy, method, args) -> switch (method.getName()) {
                case "ensureCanWrite" -> !denyPositiveX || ((BlockPos) args[0]).getX() <= 0;
                case "isOutsideBuildHeight" -> ((BlockPos) args[0]).getY() < minY || ((BlockPos) args[0]).getY() >= 320;
                case "getBlockState" -> {
                    BlockPos pos = (BlockPos) args[0];
                    assertTrue(!denyPositiveX || pos.getX() <= 0, "Must not read outside the writable region");
                    yield initial.getOrDefault(pos, Blocks.STONE.defaultBlockState());
                }
                case "setBlock" -> {
                    BlockPos pos = ((BlockPos) args[0]).immutable();
                    assertTrue(pos.getY() >= minY);
                    assertTrue(!denyPositiveX || pos.getX() <= 0);
                    written.add(pos); result.put(pos, (BlockState) args[1]); yield true;
                }
                default -> throw new UnsupportedOperationException(method.toString());
            });
        }
        boolean place(long seed) {
            return new MarbleFeature().place(new FeaturePlaceContext<>(Optional.empty(), level(), null,
                    RandomSource.create(seed), BlockPos.ZERO, new BlockStateConfiguration(Blocks.DIORITE.defaultBlockState())));
        }
    }
    @Test void seededFeatureProducesTheSameLargeFormation() {
        World first = new World(), second = new World();
        assertTrue(first.place(42)); assertTrue(second.place(42));
        assertEquals(first.written, second.written);
        assertTrue(first.written.size() > 5000);
        assertTrue(first.written.stream().allMatch(pos -> pos.distSqr(BlockPos.ZERO) <= 225));
    }
    @Test void differentSeedsChangeTheFormation() {
        World first = new World(), second = new World();
        first.place(42); second.place(43);
        assertNotEquals(first.written, second.written);
    }
    @Test void writeRegionAndBuildHeightAreRespectedBeforeReading() {
        World world = new World(); world.denyPositiveX = true; world.minY = 0;
        assertTrue(world.place(42));
        assertTrue(world.written.stream().allMatch(pos -> pos.getX() <= 0 && pos.getY() >= 0));
    }
    @Test void fluidsAirAndBlockEntitiesAreNeverReplaced() {
        World world = new World();
        List<BlockState> protectedStates = List.of(Blocks.WATER.defaultBlockState(), Blocks.LAVA.defaultBlockState(),
                Blocks.CHEST.defaultBlockState(), Blocks.AIR.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState());
        for (int i = 0; i < protectedStates.size(); i++) world.initial.put(new BlockPos(i, 0, 0), protectedStates.get(i));
        assertTrue(world.place(42));
        for (BlockPos pos : world.initial.keySet()) assertFalse(world.written.contains(pos));
    }
}
