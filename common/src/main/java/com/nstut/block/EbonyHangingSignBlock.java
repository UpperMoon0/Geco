package com.nstut.block;

import com.nstut.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class EbonyHangingSignBlock extends CeilingHangingSignBlock {
    public EbonyHangingSignBlock(WoodType woodType, Properties properties) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.EBONY_HANGING_SIGN.get().create(pos, state);
    }
}