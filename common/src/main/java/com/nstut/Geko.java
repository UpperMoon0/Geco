package com.nstut;

import com.nstut.registry.ModBlocks;
import com.nstut.registry.ModCreativeTabs; // Added import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Geko {
    public static final String MOD_ID = "geko";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Geko mod initializing...");
        ModBlocks.register();
        ModCreativeTabs.register(); // Added registration
        // Other registries like items, entities, etc. would go here
        LOGGER.info("Geko mod initialized.");
    }
}
