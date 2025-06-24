package com.nstut.fabric;

import net.fabricmc.api.ModInitializer;
import com.nstut.geco.common.Geco;

public class GecoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Geco.init();
    }
}