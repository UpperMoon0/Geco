package com.nstut.geco.common.registry;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;

public class ModBoatTypes {
    
    // Registry helper that will be set by the platform-specific code
    public static BoatTypeRegistryHelper REGISTRY_HELPER;
    
    public static Supplier<Boat.Type> EBONY;
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("BoatTypeRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        EBONY = REGISTRY_HELPER.registerBoatType("ebony", () -> (Block) ModBlocks.EBONY_PLANKS.get());
    }
    
    // Interface that platform-specific code must implement
    public interface BoatTypeRegistryHelper {
        Supplier<Boat.Type> registerBoatType(String name, Supplier<Block> planks);
    }
}