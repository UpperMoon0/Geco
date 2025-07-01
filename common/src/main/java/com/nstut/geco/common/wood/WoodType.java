package com.nstut.geco.common.wood;

import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a wood type with properties needed for dynamic block, item, and data generation.
 * This class will be expanded in future iterations to include additional properties.
 */
public class WoodType {
    private final ResourceLocation name;
    private final Set<Integer> blacklistedModels;
    
    /**
     * Creates a new WoodType with the specified name.
     *
     * @param name The ResourceLocation identifier for this wood type
     */
    public WoodType(ResourceLocation name) {
        this.name = name;
        this.blacklistedModels = new HashSet<>();
    }
    
    /**
     * Creates a new WoodType with the specified name and blacklisted models.
     *
     * @param name The ResourceLocation identifier for this wood type
     * @param blacklistedModels Set of model numbers to exclude from tree generation
     */
    public WoodType(ResourceLocation name, Set<Integer> blacklistedModels) {
        this.name = name;
        this.blacklistedModels = new HashSet<>(blacklistedModels);
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
    
    /**
     * Gets the set of blacklisted model numbers.
     *
     * @return Set of model numbers that should not be used for tree generation
     */
    public Set<Integer> getBlacklistedModels() {
        return new HashSet<>(blacklistedModels);
    }
    
    /**
     * Checks if a model number is blacklisted.
     *
     * @param modelNumber The model number to check
     * @return true if the model is blacklisted, false otherwise
     */
    public boolean isModelBlacklisted(int modelNumber) {
        return blacklistedModels.contains(modelNumber);
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