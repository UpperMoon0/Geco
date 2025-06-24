package com.nstut.geco.common.world;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public class EbonyTreeGrower {
    
    // Create a static instance of TreeGrower instead of extending it
    public static final TreeGrower EBONY_TREE_GROWER = new TreeGrower(
        "ebony",  // name
        Optional.empty(),  // megaTree - for 2x2 tree variants
        Optional.empty(),  // secondaryMegaTree - alternative 2x2 tree
        Optional.empty()   // tree - regular 1x1 tree (would be configured later)
    );
    
    // Alternative constructor-style method for custom configuration
    public static TreeGrower create(String name, 
                                   Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree, 
                                   Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree, 
                                   Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree) {
        return new TreeGrower(name, megaTree, secondaryMegaTree, tree);
    }
    
    // Helper method to create with just the regular tree configured feature
    public static TreeGrower createWithTree(ResourceKey<ConfiguredFeature<?, ?>> treeFeature) {
        return new TreeGrower(
            "ebony",
            Optional.empty(),  // no mega tree
            Optional.empty(),  // no secondary mega tree
            Optional.of(treeFeature)  // regular tree
        );
    }
}