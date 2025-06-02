package com.nstut.fabric;

import com.nstut.fabric.world.ModBiomeModifications;
import net.fabricmc.api.ModInitializer;

import com.nstut.Geco;

public final class GecoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Geco.init();
        
        // Register biome modifications during mod initialization
        // The dynamic registries should be available through datagen
        try {
            ModBiomeModifications.registerBiomeModifications();
        } catch (Exception e) {
            Geco.LOGGER.error("Failed to register biome modifications: {}", e.getMessage(), e);
        }
    }
}
