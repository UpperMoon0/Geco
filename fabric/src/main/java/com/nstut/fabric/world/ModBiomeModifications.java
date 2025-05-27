package com.nstut.fabric.world;

import com.nstut.world.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModBiomeModifications {
      public static void registerBiomeModifications() {
        // Add various ebony tree types to savanna biomes with different rarities
        
        // Small ebony trees - most common
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.SMALL_EBONY_TREES_PLACED
        );
        
        // Regular ebony trees
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.EBONY_TREES_PLACED
        );
        
        // Sparse ebony trees
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.SPARSE_EBONY_TREES_PLACED
        );
        
        // Bent/wind-swept ebony trees
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.BENT_EBONY_TREES_PLACED
        );
        
        // Tall ebony trees - less common
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.TALL_EBONY_TREES_PLACED
        );
        
        // Large ebony trees - very rare
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.RARE_LARGE_EBONY_TREES_PLACED
        );
    }
}
