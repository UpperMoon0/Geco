package com.nstut.block;

import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class EbonyFenceBlock extends FenceBlock {
    public EbonyFenceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    /**
     * Implements the exact vanilla fence logic for proper fence connections.
     * This ensures ebony fences connect to themselves and vanilla fences connect back.
     */
    private boolean isSameFence(BlockState blockState) {
        return blockState.is(BlockTags.FENCES) &&
               blockState.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
    }
    
    @Override
    public boolean connectsTo(BlockState state, boolean isSideSolid, Direction direction) {
        // Use the exact vanilla fence connection logic
        if (this.isSameFence(state)) {
            return true;
        }
        
        // Connect to fence gates (vanilla logic)
        if (state.is(BlockTags.FENCE_GATES)) {
            return true;
        }
        
        // Use parent logic for walls and other solid block connections
        return super.connectsTo(state, isSideSolid, direction);
    }
}