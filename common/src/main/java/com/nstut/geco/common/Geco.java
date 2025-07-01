package com.nstut.geco.common;

import com.nstut.geco.common.registry.ModBlockEntities;
import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModBlockSetTypes;
import com.nstut.geco.common.registry.ModCreativeTabs;
import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.registry.ModWoodTypes;
import com.nstut.geco.common.registry.ModStoneTypes;
import net.minecraft.resources.ResourceLocation;

public class Geco {
    public static final String MOD_ID = "geco";
    
    public static void init() {
        // Initialize wood types first as they're needed by other registries
        ModWoodTypes.init();
        
        // Initialize stone types
        ModStoneTypes.init();
        
        // Initialize block set types after wood types
        ModBlockSetTypes.init();
        
        // Initialize blocks after wood types and block set types
        ModBlocks.init();
        
        // Initialize items after blocks
        ModItems.init();
        
        // Initialize remaining registries
        ModCreativeTabs.init();
        ModBlockEntities.init();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}