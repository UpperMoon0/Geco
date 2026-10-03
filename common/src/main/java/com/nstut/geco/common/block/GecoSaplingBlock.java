package com.nstut.geco.common.block;

import com.nstut.geco.common.wood.WoodType;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class GecoSaplingBlock extends SaplingBlock {
    private final WoodType woodType;

    public GecoSaplingBlock(WoodType woodType) {
        super(new net.minecraft.world.level.block.grower.TreeGrower(woodType.getName().toString(),
              java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()),
              BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
        this.woodType = woodType;
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else if (mayPlaceOn(level.getBlockState(pos.below()), level, pos.below())) {
            com.nstut.geco.common.worldgen.TemplateTreePlacement.place(level, pos, random, woodType, state);
        }
    }
}
