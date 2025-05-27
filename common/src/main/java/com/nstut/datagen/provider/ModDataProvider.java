package com.nstut.datagen.provider;

import com.nstut.registry.ModWorldGeneration;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

/**
 * Base data provider containing shared datagen logic.
 * This allows platforms to extend and add their own specific data.
 */
public class ModDataProvider {
    
    /**
     * Get the base registry set builder with shared world generation features.
     * Platform-specific implementations can extend this builder.
     */
    public static RegistrySetBuilder getBaseRegistrySetBuilder() {
        return new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, ModWorldGeneration::bootstrapConfiguredFeatures)
                .add(Registries.PLACED_FEATURE, ModWorldGeneration::bootstrapPlacedFeatures);
    }
}
