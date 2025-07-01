package com.nstut.geco.common.wood;

import net.minecraft.resources.ResourceLocation;

/**
 * Represents a wood type with properties needed for dynamic block, item, and data generation.
 * This class will be expanded in future iterations to include additional properties.
 */
public class WoodType {
    private final ResourceLocation name;
    
    /**
     * Creates a new WoodType with the specified name.
     * 
     * @param name The ResourceLocation identifier for this wood type
     */
    public WoodType(ResourceLocation name) {
        this.name = name;
    }
    
    /**
     * Gets the name/identifier of this wood type.
     * 
     * @return The ResourceLocation name of this wood type
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
        WoodType woodType = (WoodType) obj;
        return name.equals(woodType.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return "WoodType{" + "name=" + name + '}';
    }
}