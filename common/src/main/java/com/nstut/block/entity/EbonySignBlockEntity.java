package com.nstut.block.entity;

import com.nstut.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EbonySignBlockEntity extends SignBlockEntity {
    public EbonySignBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.EBONY_SIGN.get(), pos, blockState);
    }
}