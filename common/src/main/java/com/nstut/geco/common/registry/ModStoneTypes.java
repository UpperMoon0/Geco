package com.nstut.geco.common.registry;

import com.nstut.geco.common.Geco;
import com.nstut.geco.common.stone.StoneType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central registry for managing StoneType instances.
 * This class provides methods to register new stone types and access all registered types.
 */
public class ModStoneTypes {
    
    /**
     * Internal list to store all registered stone types.
     */
    private static final List<StoneType> STONE_TYPES = new ArrayList<>();
    
    /**
     * Public read-only view of all registered stone types.
     */
    public static final List<StoneType> REGISTERED_STONE_TYPES = Collections.unmodifiableList(STONE_TYPES);
    
    // Registered stone types - these will be available for reference
    public static StoneType CREAM_MARBLE;
    public static StoneType MULTICOLOR_MARBLE;
    
    /**
     * Registers a new StoneType with the specified ResourceLocation name.
     * 
     * @param name The ResourceLocation identifier for the stone type
     * @return The newly created and registered StoneType instance
     */
    public static StoneType register(ResourceLocation name) {
        StoneType stoneType = new StoneType(name);
        STONE_TYPES.add(stoneType);
        return stoneType;
    }
    
    /**
     * Registers a new StoneType with the specified namespace and path.
     * 
     * @param namespace The namespace for the stone type (typically mod ID)
     * @param path The path/name for the stone type
     * @return The newly created and registered StoneType instance
     */
    public static StoneType register(String namespace, String path) {
        return register(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
    
    /**
     * Registers a StoneType instance directly.
     * 
     * @param stoneType The StoneType instance to register
     * @return The registered StoneType instance
     */
    public static StoneType register(StoneType stoneType) {
        if (!STONE_TYPES.contains(stoneType)) {
            STONE_TYPES.add(stoneType);
        }
        return stoneType;
    }
    
    /**
     * Gets all registered stone types as an unmodifiable list.
     * 
     * @return An unmodifiable list of all registered StoneType instances
     */
    public static List<StoneType> getAllStoneTypes() {
        return REGISTERED_STONE_TYPES;
    }
    
    /**
     * Gets the count of registered stone types.
     * 
     * @return The number of registered stone types
     */
    public static int getCount() {
        return STONE_TYPES.size();
    }
    
    /**
     * Finds a stone type by its ResourceLocation name.
     * 
     * @param name The ResourceLocation to search for
     * @return The StoneType with the specified name, or null if not found
     */
    public static StoneType findByName(ResourceLocation name) {
        return STONE_TYPES.stream()
                .filter(stoneType -> stoneType.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Checks if a stone type with the specified name is registered.
     * 
     * @param name The ResourceLocation to check for
     * @return true if a stone type with this name is registered, false otherwise
     */
    public static boolean isRegistered(ResourceLocation name) {
        return findByName(name) != null;
    }
    
    /**
     * Initialize and register all stone types.
     * This method should be called during mod initialization.
     */
    public static void init() {
        // Register the stone types
        CREAM_MARBLE = register(Geco.MOD_ID, "cream_marble");
        MULTICOLOR_MARBLE = register(Geco.MOD_ID, "multicolor_marble");
    }
}