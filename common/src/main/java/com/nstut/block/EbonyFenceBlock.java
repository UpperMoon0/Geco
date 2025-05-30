package com.nstut.block;

import com.nstut.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class EbonyFenceBlock extends FenceBlock {
    public EbonyFenceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean connectsTo(BlockState pState, boolean pIsSideSolid, Direction pDirection) {
        // Ensure it always connects to itself, regardless of tags for this specific case.
        // We check against the registered block instance.
        if (pState.is(ModBlocks.EBONY_FENCE.get())) {
            return true;
        }
        // For all other connection logic (e.g., to gates, other fences via tags), defer to superclass.
        return super.connectsTo(pState, pIsSideSolid, pDirection);
    }
}