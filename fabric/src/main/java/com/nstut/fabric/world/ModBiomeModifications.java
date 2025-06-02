package com.nstut.fabric.world;

import com.nstut.world.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModBiomeModifications {
    public static void registerBiomeModifications() {
        // Add Ebony Tree Type A to savanna biomes
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.EBONY_TREE_TYPE_A_PLACED
        );
    }
}
