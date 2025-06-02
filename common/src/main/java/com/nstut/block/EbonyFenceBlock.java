package com.nstut.block;

import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class EbonyFenceBlock extends FenceBlock {
    public EbonyFenceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    // No connectsTo() override - let vanilla FenceBlock handle all connections via tags
}