package com.nstut.neoforge.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;

public class ModBiomeModifications {
    
    public static BiomeModifier createEbonyTreeModifier(Holder<PlacedFeature> placedFeature, HolderSet<Biome> biomes) {
        return new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes,
                HolderSet.direct(placedFeature),
                GenerationStep.Decoration.VEGETAL_DECORATION
        );
    }
    
    // Create modifiers for all ebony tree variations
    public static BiomeModifier createSmallEbonyTreeModifier(Holder<PlacedFeature> smallEbonyTrees, HolderSet<Biome> savannaBiomes) {
        return createEbonyTreeModifier(smallEbonyTrees, savannaBiomes);
    }
    
    public static BiomeModifier createRegularEbonyTreeModifier(Holder<PlacedFeature> regularEbonyTrees, HolderSet<Biome> savannaBiomes) {
        return createEbonyTreeModifier(regularEbonyTrees, savannaBiomes);
    }
    
    public static BiomeModifier createSparseEbonyTreeModifier(Holder<PlacedFeature> sparseEbonyTrees, HolderSet<Biome> savannaBiomes) {
        return createEbonyTreeModifier(sparseEbonyTrees, savannaBiomes);
    }
    
    public static BiomeModifier createBentEbonyTreeModifier(Holder<PlacedFeature> bentEbonyTrees, HolderSet<Biome> savannaBiomes) {
        return createEbonyTreeModifier(bentEbonyTrees, savannaBiomes);
    }
    
    public static BiomeModifier createTallEbonyTreeModifier(Holder<PlacedFeature> tallEbonyTrees, HolderSet<Biome> savannaBiomes) {
        return createEbonyTreeModifier(tallEbonyTrees, savannaBiomes);
    }
    
    public static BiomeModifier createLargeEbonyTreeModifier(Holder<PlacedFeature> largeEbonyTrees, HolderSet<Biome> savannaBiomes) {
        return createEbonyTreeModifier(largeEbonyTrees, savannaBiomes);
    }
}
