package com.nstut.geco.common;

import com.nstut.geco.common.registry.*;

public class Geco {
    public static final String MOD_ID = "geco";
    
    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModBlockSetTypes.init();
        ModWoodTypes.init();
        ModCreativeTabs.init();
        ModBlockEntities.init();
    }
}