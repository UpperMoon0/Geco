package com.nstut.geco.common.stone;

import net.minecraft.resources.ResourceLocation;

/**
 * Represents a stone type with properties needed for dynamic block, item, and data generation.
 * Each stone type has 5 variants: default, polished, polished_bricks, polished_tiles, and smooth.
 * Each variant has slab, stairs, and wall blocks.
 */
public class StoneType {
    private final ResourceLocation name;
    
    /**
     * Creates a new StoneType with the specified name.
     * 
     * @param name The ResourceLocation identifier for this stone type
     */
    public StoneType(ResourceLocation name) {
        this.name = name;
    }
    
    /**
     * Gets the name/identifier of this stone type.
     * 
     * @return The ResourceLocation name of this stone type
     */
    public ResourceLocation getName() {
        return name;
    }
    
    /**
     * Gets the path portion of the name, useful for creating block/item names.
     * 
     * @return The path string from the ResourceLocation
     */
    public String getPath() {
        return name.getPath();
    }
    
    /**
     * Gets the namespace portion of the name.
     * 
     * @return The namespace string from the ResourceLocation
     */
    public String getNamespace() {
        return name.getNamespace();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StoneType stoneType = (StoneType) obj;
        return name.equals(stoneType.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return "StoneType{" + "name=" + name + '}';
    }
}