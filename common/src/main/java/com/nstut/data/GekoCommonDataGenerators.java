package com.nstut.data;

import com.nstut.registry.ModWorldGeneration;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class GekoCommonDataGenerators {
    
    // Registry builder for worldgen data - centralized for all platforms
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModWorldGeneration::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, ModWorldGeneration::bootstrapPlacedFeatures);
}
