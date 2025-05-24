package com.nstut.registry;

import com.nstut.world.feature.ModConfiguredFeatures;
import com.nstut.world.feature.ModPlacedFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModWorldGeneration {
    
    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ModConfiguredFeatures.bootstrap(context);
    }
    
    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        ModPlacedFeatures.bootstrap(context);
    }
}
