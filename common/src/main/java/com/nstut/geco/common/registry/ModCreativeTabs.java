package com.nstut.geco.common.registry;

import com.nstut.geco.common.Geco;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ModCreativeTabs {
    
    // Registry helper that will be set by the platform-specific code
    public static CreativeTabRegistryHelper REGISTRY_HELPER;
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("CreativeTabRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        // Use items from ModItems for the creative tab
        REGISTRY_HELPER.registerCreativeTab("geco_tab", ModItems.getAllItems());
    }
    
    // Interface that platform-specific code must implement
    public interface CreativeTabRegistryHelper {
        void registerCreativeTab(String name, java.util.List<Supplier<? extends Item>> items);
    }
}
