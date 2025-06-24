package com.nstut.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import com.nstut.geco.common.Geco;
import com.nstut.neoforge.world.ModBiomeModifications;

@Mod(Geco.MOD_ID)
public class GecoNeoForge {
    
    public GecoNeoForge(IEventBus modEventBus) {
        Geco.init();
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {

    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        // Client setup
    }
    
    public static class ClientModEvents {
        // Client-side event handlers
    }
}