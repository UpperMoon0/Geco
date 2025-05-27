package com.nstut.fabric;

import com.nstut.fabric.world.ModBiomeModifications;
import net.fabricmc.api.ModInitializer;

import com.nstut.Geko;

public final class GekoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Geko.init();
        
        // Register biome modifications during mod initialization
        // The dynamic registries should be available through datagen
        try {
            ModBiomeModifications.registerBiomeModifications();
        } catch (Exception e) {
            Geko.LOGGER.error("Failed to register biome modifications: {}", e.getMessage(), e);
        }
    }
}
