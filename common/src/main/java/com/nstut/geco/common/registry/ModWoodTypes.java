package com.nstut.geco.common.registry;

import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    
    public static final WoodType EBONY = WoodType.register(new WoodType("ebony", ModBlockSetTypes.EBONY));
    
    public static void init() {
        // Platform-specific initialization
    }
}