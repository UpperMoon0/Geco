package com.nstut.fabric;

import com.nstut.fabric.world.ModBiomeModifications;
import net.fabricmc.api.ModInitializer;

import com.nstut.Geko;

public final class GekoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Geko.init();
        ModBiomeModifications.registerBiomeModifications();
    }
}
