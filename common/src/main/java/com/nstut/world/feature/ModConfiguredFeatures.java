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
                        new StraightTrunkPlacer(6, 3, 0), // taller trunk
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), // radius, offset, height
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                .ignoreVines()
                .build()
        ));

        // Large Ebony Tree - 2x2 trunk, very tall
        context.register(LARGE_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new GiantTrunkPlacer(13, 2, 14), // baseHeight, heightRandA, heightRandB
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 3), // radius, offset, height
                        new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                )
                .decorators(java.util.List.of()) // No decorators for now
                .ignoreVines()
                .build()
        ));
        
        // Small Ebony Tree - Compact shrub-like tree
        context.register(SMALL_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new StraightTrunkPlacer(3, 1, 0), // short trunk: 3-4 blocks
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0), 2), // small, compact foliage
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                .ignoreVines()
                .build()
        ));
        
        // Tall Ebony Tree - Single trunk, very tall
        context.register(TALL_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new StraightTrunkPlacer(8, 4, 0), // tall trunk: 8-12 blocks
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1)), // umbrella-like canopy
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                .ignoreVines()
                .build()
        ));
        
        // Bent Ebony Tree - Wind-swept appearance
        context.register(BENT_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new BendingTrunkPlacer(4, 2, 0, 3, ConstantInt.of(1)), // bent trunk
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), // scattered foliage
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                .ignoreVines()
                .build()
        ));
        
        // Sparse Ebony Tree - Multiple small branching trunks
        context.register(SPARSE_EBONY_TREE, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.EBONY_LOG.get()),
                        new ForkingTrunkPlacer(4, 2, 3), // multiple branches
                        BlockStateProvider.simple(ModBlocks.EBONY_LEAVES.get()),
                        new AcaciaFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)), // sparse foliage
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                .ignoreVines()
                .build()
        ));
    }
}