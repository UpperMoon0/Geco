package com.nstut.geco.common.registry;

import com.nstut.geco.common.wood.WoodType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
    
    // Registry helper that will be set by the platform-specific code
    public static ItemRegistryHelper REGISTRY_HELPER;
    
    // List to track all registered items for automatic creative tab population
    private static final List<Supplier<? extends Item>> ALL_ITEMS = new ArrayList<>();
    
    // Maps to store dynamically registered items
    private static final Map<WoodType, WoodItemSet> WOOD_ITEM_SETS = new HashMap<>();
    
    
    /**
     * Container class to hold all items for a wood type
     */
    public static class WoodItemSet {
        public final Supplier<BlockItem> log;
        public final Supplier<BlockItem> strippedLog;
        public final Supplier<BlockItem> wood;
        public final Supplier<BlockItem> strippedWood;
        public final Supplier<BlockItem> planks;
        public final Supplier<BlockItem> stairs;
        public final Supplier<BlockItem> slab;
        public final Supplier<BlockItem> fence;
        public final Supplier<BlockItem> fenceGate;
        public final Supplier<BlockItem> door;
        public final Supplier<BlockItem> trapdoor;
        public final Supplier<BlockItem> pressurePlate;
        public final Supplier<BlockItem> button;
        public final Supplier<BlockItem> leaves;
        public final Supplier<BlockItem> sapling;
        
        public WoodItemSet(Supplier<BlockItem> log, Supplier<BlockItem> strippedLog, Supplier<BlockItem> wood,
                          Supplier<BlockItem> strippedWood, Supplier<BlockItem> planks, Supplier<BlockItem> stairs,
                          Supplier<BlockItem> slab, Supplier<BlockItem> fence, Supplier<BlockItem> fenceGate,
                          Supplier<BlockItem> door, Supplier<BlockItem> trapdoor, Supplier<BlockItem> pressurePlate,
                          Supplier<BlockItem> button, Supplier<BlockItem> leaves, Supplier<BlockItem> sapling) {
            this.log = log;
            this.strippedLog = strippedLog;
            this.wood = wood;
            this.strippedWood = strippedWood;
            this.planks = planks;
            this.stairs = stairs;
            this.slab = slab;
            this.fence = fence;
            this.fenceGate = fenceGate;
            this.door = door;
            this.trapdoor = trapdoor;
            this.pressurePlate = pressurePlate;
            this.button = button;
            this.leaves = leaves;
            this.sapling = sapling;
        }
    }
    
    /**
     * Gets the WoodItemSet for a specific wood type.
     *
     * @param woodType The wood type to get items for
     * @return The WoodItemSet containing all items for this wood type
     */
    public static WoodItemSet getWoodItemSet(WoodType woodType) {
        return WOOD_ITEM_SETS.get(woodType);
    }
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("ItemRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        // Dynamically register items for all wood types
        for (WoodType woodType : ModWoodTypes.REGISTERED_WOOD_TYPES) {
            registerWoodItemSet(woodType);
        }
        
    }
    
    /**
     * Registers all items for a specific wood type.
     *
     * @param woodType The wood type to register items for
     */
    private static void registerWoodItemSet(WoodType woodType) {
        String woodName = woodType.getPath();
        ModBlocks.WoodBlockSet blockSet = ModBlocks.getWoodBlockSet(woodType);
        
        if (blockSet == null) {
            throw new IllegalStateException("WoodBlockSet not found for wood type: " + woodName +
                ". Make sure ModBlocks.init() is called before ModItems.init()");
        }
        
        // Register all block items
        Supplier<BlockItem> log = registerBlockItemAndTrack(woodName + "_log", blockSet.log);
        Supplier<BlockItem> strippedLog = registerBlockItemAndTrack("stripped_" + woodName + "_log", blockSet.strippedLog);
        Supplier<BlockItem> wood = registerBlockItemAndTrack(woodName + "_wood", blockSet.wood);
        Supplier<BlockItem> strippedWood = registerBlockItemAndTrack("stripped_" + woodName + "_wood", blockSet.strippedWood);
        Supplier<BlockItem> planks = registerBlockItemAndTrack(woodName + "_planks", blockSet.planks);
        Supplier<BlockItem> stairs = registerBlockItemAndTrack(woodName + "_stairs", blockSet.stairs);
        Supplier<BlockItem> slab = registerBlockItemAndTrack(woodName + "_slab", blockSet.slab);
        Supplier<BlockItem> fence = registerBlockItemAndTrack(woodName + "_fence", blockSet.fence);
        Supplier<BlockItem> fenceGate = registerBlockItemAndTrack(woodName + "_fence_gate", blockSet.fenceGate);
        Supplier<BlockItem> door = registerBlockItemAndTrack(woodName + "_door", blockSet.door);
        Supplier<BlockItem> trapdoor = registerBlockItemAndTrack(woodName + "_trapdoor", blockSet.trapdoor);
        Supplier<BlockItem> pressurePlate = registerBlockItemAndTrack(woodName + "_pressure_plate", blockSet.pressurePlate);
        Supplier<BlockItem> button = registerBlockItemAndTrack(woodName + "_button", blockSet.button);
        Supplier<BlockItem> leaves = registerBlockItemAndTrack(woodName + "_leaves", blockSet.leaves);
        Supplier<BlockItem> sapling = registerBlockItemAndTrack(woodName + "_sapling", blockSet.sapling);
        
        // Store the complete set
        WoodItemSet itemSet = new WoodItemSet(log, strippedLog, wood, strippedWood, planks,
                stairs, slab, fence, fenceGate, door, trapdoor, pressurePlate, button, leaves, sapling);
        
        WOOD_ITEM_SETS.put(woodType, itemSet);
    }
    
    private static <T extends BlockItem> Supplier<T> registerBlockItemAndTrack(String name, Supplier<?> block) {
        Supplier<T> registeredItem = REGISTRY_HELPER.registerBlockItem(name, block);
        ALL_ITEMS.add(registeredItem);
        return registeredItem;
    }
    
    // Public method to get all registered items for creative tab population
    public static List<Supplier<? extends Item>> getAllItems() {
        return new ArrayList<>(ALL_ITEMS);
    }
    
    // Interface that platform-specific code must implement
    public interface ItemRegistryHelper {
        <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);
        <T extends BlockItem> Supplier<T> registerBlockItem(String name, Supplier<?> block);
    }
}