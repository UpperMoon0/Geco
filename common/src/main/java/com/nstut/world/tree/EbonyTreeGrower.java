package com.nstut.world.tree;

import com.nstut.world.feature.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class EbonyTreeGrower {
    
    // Static TreeGrower instance for ebony trees
    public static final TreeGrower EBONY = new TreeGrower(
            "ebony", // name
            Optional.of(ModConfiguredFeatures.EBONY_TREE_TYPE_A), // regular tree feature
            Optional.empty(), // fancy tree feature / large variant (none for now)
            Optional.empty()  // flower feature (none)
    );
}
