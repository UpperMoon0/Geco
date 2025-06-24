package com.nstut.geco.common.registry;

import net.minecraft.world.item.CreativeModeTab;
import java.util.function.Supplier;

public class ModCreativeTabs {
    
    // Registry helper that will be set by the platform-specific code
    public static CreativeTabRegistryHelper REGISTRY_HELPER;
    
    // Main creative tab for all Geco items
    public static Supplier<CreativeModeTab> GECO_TAB;
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("CreativeTabRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        GECO_TAB = REGISTRY_HELPER.registerCreativeTab("geco_tab");
    }
    
    // Interface that platform-specific code must implement
    public interface CreativeTabRegistryHelper {
        Supplier<CreativeModeTab> registerCreativeTab(String name);
    }
}