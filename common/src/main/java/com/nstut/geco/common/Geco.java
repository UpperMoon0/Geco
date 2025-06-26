package com.nstut.geco.common;

import com.nstut.geco.common.registry.ModBlockEntities;
import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModBlockSetTypes;
import com.nstut.geco.common.registry.ModCreativeTabs;
import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.registry.ModWoodTypes;
import com.nstut.geco.common.registry.ModPlacedFeatures;
import net.minecraft.resources.ResourceLocation;

public class Geco {
    public static final String MOD_ID = "geco";
    
    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModBlockSetTypes.init();
        ModWoodTypes.init();
        ModCreativeTabs.init();
        ModBlockEntities.init();
        ModPlacedFeatures.init();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}