package com.nstut.world.feature;

import com.nstut.Geko;
import com.nstut.registry.ModBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModPlacedFeatures {
    
    // Resource Keys for our placed features
    public static final ResourceKey<PlacedFeature> EBONY_TREES_PLACED = 
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "ebony_trees_placed"));
            
    // New savanna ebony tree variations - different spawn rates for variety
    public static final ResourceKey<PlacedFeature> SMALL_EBONY_TREES_PLACED = 
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "small_ebony_trees_placed"));
            
    public static final ResourceKey<PlacedFeature> TALL_EBONY_TREES_PLACED = 
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "tall_ebony_trees_placed"));
            
    public static final ResourceKey<PlacedFeature> BENT_EBONY_TREES_PLACED = 
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "bent_ebony_trees_placed"));
            
    public static final ResourceKey<PlacedFeature> SPARSE_EBONY_TREES_PLACED = 
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "sparse_ebony_trees_placed"));
            
    public static final ResourceKey<PlacedFeature> RARE_LARGE_EBONY_TREES_PLACED = 
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "rare_large_ebony_trees_placed"));    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Regular ebony trees in savanna biomes - moderate spawn rate
        context.register(EBONY_TREES_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.EBONY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(0, 0.05f, 1), // 5% chance for 1 tree per chunk
                        ModBlocks.EBONY_SAPLING.get())
        ));
        
        // Small ebony trees - more common, shrub-like
        context.register(SMALL_EBONY_TREES_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SMALL_EBONY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.1f, 2), // 1 guaranteed + 10% chance for 2 more per chunk
                        ModBlocks.EBONY_SAPLING.get())
        ));
        
        // Tall ebony trees - less common, majestic
        context.register(TALL_EBONY_TREES_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.TALL_EBONY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(0, 0.03f, 1), // 3% chance for 1 tree per chunk
                        ModBlocks.EBONY_SAPLING.get())
        ));
        
        // Bent ebony trees - wind-swept, uncommon
        context.register(BENT_EBONY_TREES_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.BENT_EBONY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(0, 0.04f, 1), // 4% chance for 1 tree per chunk
                        ModBlocks.EBONY_SAPLING.get())
        ));
        
        // Sparse ebony trees - multiple small trunks, moderate rarity
        context.register(SPARSE_EBONY_TREES_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SPARSE_EBONY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(0, 0.06f, 1), // 6% chance for 1 tree per chunk
                        ModBlocks.EBONY_SAPLING.get())
        ));
        
        // Large ebony trees - very rare, impressive
        context.register(RARE_LARGE_EBONY_TREES_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.LARGE_EBONY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(0, 0.01f, 1), // 1% chance for 1 tree per chunk
                        ModBlocks.EBONY_SAPLING.get())
        ));
    }
}
