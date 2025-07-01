package com.nstut.geco.common.registry;

import com.nstut.geco.common.wood.WoodType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.HashMap;
import java.util.Map;

public class ModBlockSetTypes {
    
    // Map to store BlockSetType instances for each WoodType
    private static final Map<WoodType, BlockSetType> BLOCK_SET_TYPES = new HashMap<>();
    
    // Public reference for existing code compatibility
    public static BlockSetType EBONY;
    
    /**
     * Gets or creates a BlockSetType for the given WoodType.
     *
     * @param woodType The WoodType to get the BlockSetType for
     * @return The BlockSetType instance for this wood type
     */
    public static BlockSetType getBlockSetType(WoodType woodType) {
        return BLOCK_SET_TYPES.computeIfAbsent(woodType, wt -> createBlockSetType(wt));
    }
    
    /**
     * Creates a new BlockSetType for the given WoodType.
     *
     * @param woodType The WoodType to create a BlockSetType for
     * @return A new BlockSetType instance
     */
    private static BlockSetType createBlockSetType(WoodType woodType) {
        return new BlockSetType(
            woodType.getName().toString(),
            true,
            true,
            true,
            BlockSetType.PressurePlateSensitivity.EVERYTHING,
            SoundType.WOOD,
            SoundEvents.WOODEN_DOOR_CLOSE,
            SoundEvents.WOODEN_DOOR_OPEN,
            SoundEvents.WOODEN_TRAPDOOR_CLOSE,
            SoundEvents.WOODEN_TRAPDOOR_OPEN,
            SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.WOODEN_BUTTON_CLICK_OFF,
            SoundEvents.WOODEN_BUTTON_CLICK_ON
        );
    }
    
    public static void init() {
        // Create BlockSetTypes for all registered wood types
        for (WoodType woodType : ModWoodTypes.REGISTERED_WOOD_TYPES) {
            getBlockSetType(woodType);
        }
        
        // Set the EBONY reference for backward compatibility
        if (ModWoodTypes.EBONY != null) {
            EBONY = getBlockSetType(ModWoodTypes.EBONY);
        }
    }
}