package com.nstut;

import com.nstut.registry.ModBlockEntities;
import com.nstut.registry.ModBlocks;
import com.nstut.registry.ModCreativeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Geco {
    public static final String MOD_ID = "geco";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    public static void init() {
        LOGGER.info("Geco mod initializing...");
        ModBlocks.register();
        ModBlockEntities.register();
        ModCreativeTabs.register();
        LOGGER.info("Geco mod initialized.");
    }
}
