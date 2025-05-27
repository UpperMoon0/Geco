package com.nstut.world.feature;

import com.nstut.Geko;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.List;

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
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "rare_large_ebony_trees_placed"));

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
    }
    
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Get holders for each specific configured feature
        Holder<ConfiguredFeature<?, ?>> ebonyTree = configuredFeatures.getOrThrow(ModConfiguredFeatures.EBONY_TREE);
        Holder<ConfiguredFeature<?, ?>> smallEbonyTree = configuredFeatures.getOrThrow(ModConfiguredFeatures.SMALL_EBONY_TREE);
        Holder<ConfiguredFeature<?, ?>> tallEbonyTree = configuredFeatures.getOrThrow(ModConfiguredFeatures.TALL_EBONY_TREE);
        Holder<ConfiguredFeature<?, ?>> bentEbonyTree = configuredFeatures.getOrThrow(ModConfiguredFeatures.BENT_EBONY_TREE);
        Holder<ConfiguredFeature<?, ?>> sparseEbonyTree = configuredFeatures.getOrThrow(ModConfiguredFeatures.SPARSE_EBONY_TREE);
        Holder<ConfiguredFeature<?, ?>> largeEbonyTree = configuredFeatures.getOrThrow(ModConfiguredFeatures.LARGE_EBONY_TREE);

        // Regular Ebony Tree
        register(context, EBONY_TREES_PLACED, ebonyTree, 
            List.of(RarityFilter.onAverageOnceEvery(16), // Regular
                      InSquarePlacement.spread(), 
                      HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), 
                      BiomeFilter.biome()));

        // Small Ebony Tree - more common
        register(context, SMALL_EBONY_TREES_PLACED, smallEbonyTree, 
            List.of(RarityFilter.onAverageOnceEvery(8), // More common
                      InSquarePlacement.spread(), 
                      HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), 
                      BiomeFilter.biome()));

        // Tall Ebony Tree - less common
        register(context, TALL_EBONY_TREES_PLACED, tallEbonyTree, 
            List.of(RarityFilter.onAverageOnceEvery(24), // Less common
                      InSquarePlacement.spread(), 
                      HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), 
                      BiomeFilter.biome()));
                      
        // Bent Ebony Tree
        register(context, BENT_EBONY_TREES_PLACED, bentEbonyTree, 
            List.of(RarityFilter.onAverageOnceEvery(20), // Moderate rarity
                      InSquarePlacement.spread(), 
                      HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), 
                      BiomeFilter.biome()));

        // Sparse Ebony Tree - less dense/rarer
        register(context, SPARSE_EBONY_TREES_PLACED, sparseEbonyTree, 
            List.of(RarityFilter.onAverageOnceEvery(32), // Rarer/more sparse
                      InSquarePlacement.spread(), 
                      HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), 
                      BiomeFilter.biome()));
                      
        // Rare Large Ebony Tree - very rare
        register(context, RARE_LARGE_EBONY_TREES_PLACED, largeEbonyTree, 
            List.of(RarityFilter.onAverageOnceEvery(64), // Very rare
                      InSquarePlacement.spread(), 
                      HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), 
                      BiomeFilter.biome()));
    }
}
