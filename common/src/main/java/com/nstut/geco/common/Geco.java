package com.nstut.geco.common;

import com.nstut.geco.common.registry.ModBlockEntities;
import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModBlockSetTypes;
import com.nstut.geco.common.registry.ModCreativeTabs;
import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.registry.ModWoodTypes;
import com.nstut.geco.common.registry.ModStoneTypes;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Geco {
    public static final String MOD_ID = "geco";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Geco: Initializing common mod components");

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

        LOGGER.info("Geco: Common initialization complete");
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}