package com.nstut.world.tree;

import com.nstut.world.feature.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class EbonyTreeGrower {
    
    // Static TreeGrower instance for ebony trees
    public static final TreeGrower EBONY = new TreeGrower(
            "ebony",
            Optional.of(ModConfiguredFeatures.EBONY_TREE), // Regular tree
            Optional.empty(), // No flower feature
            Optional.of(ModConfiguredFeatures.LARGE_EBONY_TREE) // Large tree (2x2)
    );
}
