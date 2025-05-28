package com.nstut.neoforge;

import com.nstut.neoforge.world.ModBiomeModifications;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import com.nstut.Geko;

@Mod(Geko.MOD_ID)
public final class GekoNeoForge {
    public GekoNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Geko.init();
        
        // Register biome modifications for NeoForge
        ModBiomeModifications.register(modEventBus);
    }
}
