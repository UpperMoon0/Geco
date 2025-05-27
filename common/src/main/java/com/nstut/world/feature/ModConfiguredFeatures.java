package com.nstut.world.feature;

import com.nstut.Geko;
import com.nstut.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;

import java.util.OptionalInt;

public class ModConfiguredFeatures {
    
    // Resource Keys for our tree features
    public static final ResourceKey<ConfiguredFeature<?, ?>> EBONY_TREE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "ebony_tree"));
            
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_EBONY_TREE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "fancy_ebony_tree"));
            
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_EBONY_TREE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "large_ebony_tree"));
            
    // New savanna ebony tree variations
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_EBONY_TREE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "small_ebony_tree"));
            
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_EBONY_TREE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "tall_ebony_tree"));
            
    public static final ResourceKey<ConfiguredFeature<?, ?>> BENT_EBONY_TREE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "bent_ebony_tree"));
            
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPARSE_EBONY_TREE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "sparse_ebony_tree"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        
        // Regular Ebony Tree - Similar to acacia but with ebony wood
        context.register(EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE, 
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new ForkingTrunkPlacer(5, 2, 2), // height, randomA, randomB
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), // radius, offset
                        new TwoLayersFeatureSize(1, 0, 2) // limit, lowerSize, upperSize
                )
                .ignoreVines()
                .build()
        ));

        // Fancy Ebony Tree - More elaborate tree structure
        context.register(FANCY_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new FancyTrunkPlacer(3, 11, 0), // Base height, height rand a, height rand b
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), // Radius, offset, height
                        new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)) // Limit, lower size, upper size, min clipped height
                )
                .ignoreVines()
                .build()
        ));
        
        // Large Ebony Tree - For 2x2 sapling growth
        context.register(LARGE_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        // Using GiantTrunkPlacer for a thicker, taller tree suitable for 2x2
                        new GiantTrunkPlacer(13, 2, 14), // baseHeight, heightRandA, heightRandB
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 3), // radius, offset, height
                        // ThreeLayersFeatureSize is often used for large trees that might grow from 2x2 saplings
                        new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()) // limit, upperLimit, lowerSize, middleSize, upperSize, minClippedHeight
                )
                .ignoreVines()
                .build()
        ));

        // Small Ebony Tree
        context.register(SMALL_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new StraightTrunkPlacer(3, 1, 0), // baseHeight, heightRandA, heightRandB
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2), // radius, offset, height
                        new TwoLayersFeatureSize(1, 0, 1) // limit, lowerSize, upperSize
                )
                .ignoreVines()
                .build()
        ));

        // Tall Ebony Tree
        context.register(TALL_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new StraightTrunkPlacer(8, 2, 1), // baseHeight, heightRandA, heightRandB
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), // radius, offset, height
                        new TwoLayersFeatureSize(1, 0, 2) // limit, lowerSize, upperSize
                )
                .ignoreVines()
                .build()
        ));

        // Bent Ebony Tree
        context.register(BENT_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new BendingTrunkPlacer(5, 2, 1, 3, ConstantInt.of(3)), // baseHeight, heightRandA, heightRandB, bendLength, minHeightForLeaves
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2), // radius, offset, height
                        new TwoLayersFeatureSize(1, 0, 1) // limit, lowerSize, upperSize
                )
                .ignoreVines()
                .build()
        ));

        // Sparse Ebony Tree - Using AcaciaFoliagePlacer for a sparser look
        context.register(SPARSE_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new ForkingTrunkPlacer(4, 2, 2), // baseHeight, heightRandA, heightRandB
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), // radius, offset
                        new TwoLayersFeatureSize(1, 0, 2) // limit, lowerSize, upperSize
                )
                .ignoreVines()
                .build()
        ));
    }
}