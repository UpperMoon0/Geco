package com.nstut.block;

import com.nstut.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class EbonyWallHangingSignBlock extends WallHangingSignBlock {
    public EbonyWallHangingSignBlock(WoodType woodType, BlockBehaviour.Properties properties) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.EBONY_HANGING_SIGN_BLOCK_ENTITY.get().create(pos, state);
    }
}