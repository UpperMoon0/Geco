package com.nstut.geco.common.registry;

import com.nstut.geco.common.Geco;
import com.nstut.geco.common.wood.WoodType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central registry for managing WoodType instances.
 * This class provides methods to register new wood types and access all registered types.
 */
public class ModWoodTypes {
    
    /**
     * Internal list to store all registered wood types.
     */
    private static final List<WoodType> WOOD_TYPES = new ArrayList<>();
    
    /**
     * Public read-only view of all registered wood types.
     */
    public static final List<WoodType> REGISTERED_WOOD_TYPES = Collections.unmodifiableList(WOOD_TYPES);
    
    // Registered wood types - these will be available for reference
    public static WoodType EBONY;
    public static WoodType CHERRY;
    
    /**
     * Registers a new WoodType with the specified ResourceLocation name.
     * 
     * @param name The ResourceLocation identifier for the wood type
     * @return The newly created and registered WoodType instance
     */
    public static WoodType register(ResourceLocation name) {
        WoodType woodType = new WoodType(name);
        WOOD_TYPES.add(woodType);
        return woodType;
    }
    
    /**
     * Registers a new WoodType with the specified namespace and path.
     * 
     * @param namespace The namespace for the wood type (typically mod ID)
     * @param path The path/name for the wood type
     * @return The newly created and registered WoodType instance
     */
    public static WoodType register(String namespace, String path) {
        return register(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
    
    /**
     * Registers a WoodType instance directly.
     * 
     * @param woodType The WoodType instance to register
     * @return The registered WoodType instance
     */
    public static WoodType register(WoodType woodType) {
        if (!WOOD_TYPES.contains(woodType)) {
            WOOD_TYPES.add(woodType);
        }
        return woodType;
    }
    
    /**
     * Gets all registered wood types as an unmodifiable list.
     * 
     * @return An unmodifiable list of all registered WoodType instances
     */
    public static List<WoodType> getAllWoodTypes() {
        return REGISTERED_WOOD_TYPES;
    }
    
    /**
     * Gets the count of registered wood types.
     * 
     * @return The number of registered wood types
     */
    public static int getCount() {
        return WOOD_TYPES.size();
    }
    
    /**
     * Finds a wood type by its ResourceLocation name.
     * 
     * @param name The ResourceLocation to search for
     * @return The WoodType with the specified name, or null if not found
     */
    public static WoodType findByName(ResourceLocation name) {
        return WOOD_TYPES.stream()
                .filter(woodType -> woodType.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Checks if a wood type with the specified name is registered.
     * 
     * @param name The ResourceLocation to check for
     * @return true if a wood type with this name is registered, false otherwise
     */
    public static boolean isRegistered(ResourceLocation name) {
        return findByName(name) != null;
    }
    
    /**
     * Initialize and register all wood types.
     * This method should be called during mod initialization.
     */
    public static void init() {
        // Register the Ebony wood type
        EBONY = register(Geco.MOD_ID, "ebony");
    }
}