package com.nstut.geco.common.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

/** Rare 30-block formations, using the feature context's world/chunk-seeded RNG. */
public final class MarbleFeature extends Feature<BlockStateConfiguration> {
    public MarbleFeature() { super(BlockStateConfiguration.CODEC); }
    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos center = context.origin();
        boolean placed = false;
        for (int x = -15; x <= 15; x++)
            for (int y = -15; y <= 15; y++)
                for (int z = -15; z <= 15; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance > 15) continue;
                    // Consume RNG independently of write availability.
                    if (context.random().nextFloat() >= Math.max(0.4F, 0.9F - (float) distance * 0.02F)) continue;
                    BlockPos pos = center.offset(x, y, z);
                    if (level.isOutsideBuildHeight(pos) || !level.ensureCanWrite(pos)) continue;
                    BlockState state = level.getBlockState(pos);
                    if (!state.getFluidState().isEmpty() || state.hasBlockEntity()) continue;
                    if (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.DIRT) || state.is(Blocks.GRAVEL))
                        placed |= level.setBlock(pos, context.config().state, 2);
                }
        return placed;
    }
}
