package com.nstut.neoforge;

import net.neoforged.fml.common.Mod;

import com.nstut.Geko;

@Mod(Geko.MOD_ID)
public final class GekoNeoForge {
    public GekoNeoForge() {
        // Run our common setup.
        Geko.init();
    }
}
