package com.nstut.geco.common.registry;

import com.nstut.geco.common.wood.WoodType;
import com.nstut.geco.common.stone.StoneType;
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
    private static final Map<StoneType, StoneItemSet> STONE_ITEM_SETS = new HashMap<>();
    
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
     * Container class to hold all items for a stone type
     */
    public static class StoneItemSet {
        // Default variant
        public final Supplier<BlockItem> base;
        public final Supplier<BlockItem> baseSlab;
        public final Supplier<BlockItem> baseStairs;
        public final Supplier<BlockItem> baseWall;
        
        // Polished variant
        public final Supplier<BlockItem> polished;
        public final Supplier<BlockItem> polishedSlab;
        public final Supplier<BlockItem> polishedStairs;
        public final Supplier<BlockItem> polishedWall;
        
        // Polished bricks variant
        public final Supplier<BlockItem> polishedBricks;
        public final Supplier<BlockItem> polishedBricksSlab;
        public final Supplier<BlockItem> polishedBricksStairs;
        public final Supplier<BlockItem> polishedBricksWall;
        
        // Polished tiles variant
        public final Supplier<BlockItem> polishedTiles;
        public final Supplier<BlockItem> polishedTilesSlab;
        public final Supplier<BlockItem> polishedTilesStairs;
        public final Supplier<BlockItem> polishedTilesWall;
        
        // Smooth variant
        public final Supplier<BlockItem> smooth;
        public final Supplier<BlockItem> smoothSlab;
        public final Supplier<BlockItem> smoothStairs;
        public final Supplier<BlockItem> smoothWall;
        
        public StoneItemSet(
                Supplier<BlockItem> base, Supplier<BlockItem> baseSlab, Supplier<BlockItem> baseStairs, Supplier<BlockItem> baseWall,
                Supplier<BlockItem> polished, Supplier<BlockItem> polishedSlab, Supplier<BlockItem> polishedStairs, Supplier<BlockItem> polishedWall,
                Supplier<BlockItem> polishedBricks, Supplier<BlockItem> polishedBricksSlab, Supplier<BlockItem> polishedBricksStairs, Supplier<BlockItem> polishedBricksWall,
                Supplier<BlockItem> polishedTiles, Supplier<BlockItem> polishedTilesSlab, Supplier<BlockItem> polishedTilesStairs, Supplier<BlockItem> polishedTilesWall,
                Supplier<BlockItem> smooth, Supplier<BlockItem> smoothSlab, Supplier<BlockItem> smoothStairs, Supplier<BlockItem> smoothWall) {
            this.base = base;
            this.baseSlab = baseSlab;
            this.baseStairs = baseStairs;
            this.baseWall = baseWall;
            this.polished = polished;
            this.polishedSlab = polishedSlab;
            this.polishedStairs = polishedStairs;
            this.polishedWall = polishedWall;
            this.polishedBricks = polishedBricks;
            this.polishedBricksSlab = polishedBricksSlab;
            this.polishedBricksStairs = polishedBricksStairs;
            this.polishedBricksWall = polishedBricksWall;
            this.polishedTiles = polishedTiles;
            this.polishedTilesSlab = polishedTilesSlab;
            this.polishedTilesStairs = polishedTilesStairs;
            this.polishedTilesWall = polishedTilesWall;
            this.smooth = smooth;
            this.smoothSlab = smoothSlab;
            this.smoothStairs = smoothStairs;
            this.smoothWall = smoothWall;
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
    
    /**
     * Gets the StoneItemSet for a specific stone type.
     *
     * @param stoneType The stone type to get items for
     * @return The StoneItemSet containing all items for this stone type
     */
    public static StoneItemSet getStoneItemSet(StoneType stoneType) {
        return STONE_ITEM_SETS.get(stoneType);
    }
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("ItemRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        // Dynamically register items for all wood types
        for (WoodType woodType : ModWoodTypes.REGISTERED_WOOD_TYPES) {
            registerWoodItemSet(woodType);
        }
        
        // Dynamically register items for all stone types
        for (StoneType stoneType : ModStoneTypes.REGISTERED_STONE_TYPES) {
            registerStoneItemSet(stoneType);
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
    
    /**
     * Registers all items for a specific stone type.
     *
     * @param stoneType The stone type to register items for
     */
    private static void registerStoneItemSet(StoneType stoneType) {
        String stoneName = stoneType.getPath();
        ModBlocks.StoneBlockSet blockSet = ModBlocks.getStoneBlockSet(stoneType);
        
        if (blockSet == null) {
            throw new IllegalStateException("StoneBlockSet not found for stone type: " + stoneName +
                ". Make sure ModBlocks.init() is called before ModItems.init()");
        }
        
        // Register all block items for base variant
        Supplier<BlockItem> base = registerBlockItemAndTrack(stoneName, blockSet.base);
        Supplier<BlockItem> baseSlab = registerBlockItemAndTrack(stoneName + "_slab", blockSet.baseSlab);
        Supplier<BlockItem> baseStairs = registerBlockItemAndTrack(stoneName + "_stairs", blockSet.baseStairs);
        Supplier<BlockItem> baseWall = registerBlockItemAndTrack(stoneName + "_wall", blockSet.baseWall);
        
        // Register all block items for polished variant
        Supplier<BlockItem> polished = registerBlockItemAndTrack("polished_" + stoneName, blockSet.polished);
        Supplier<BlockItem> polishedSlab = registerBlockItemAndTrack("polished_" + stoneName + "_slab", blockSet.polishedSlab);
        Supplier<BlockItem> polishedStairs = registerBlockItemAndTrack("polished_" + stoneName + "_stairs", blockSet.polishedStairs);
        Supplier<BlockItem> polishedWall = registerBlockItemAndTrack("polished_" + stoneName + "_wall", blockSet.polishedWall);
        
        // Register all block items for polished bricks variant
        Supplier<BlockItem> polishedBricks = registerBlockItemAndTrack("polished_" + stoneName + "_bricks", blockSet.polishedBricks);
        Supplier<BlockItem> polishedBricksSlab = registerBlockItemAndTrack("polished_" + stoneName + "_bricks_slab", blockSet.polishedBricksSlab);
        Supplier<BlockItem> polishedBricksStairs = registerBlockItemAndTrack("polished_" + stoneName + "_bricks_stairs", blockSet.polishedBricksStairs);
        Supplier<BlockItem> polishedBricksWall = registerBlockItemAndTrack("polished_" + stoneName + "_bricks_wall", blockSet.polishedBricksWall);
        
        // Register all block items for polished tiles variant
        Supplier<BlockItem> polishedTiles = registerBlockItemAndTrack("polished_" + stoneName + "_tiles", blockSet.polishedTiles);
        Supplier<BlockItem> polishedTilesSlab = registerBlockItemAndTrack("polished_" + stoneName + "_tiles_slab", blockSet.polishedTilesSlab);
        Supplier<BlockItem> polishedTilesStairs = registerBlockItemAndTrack("polished_" + stoneName + "_tiles_stairs", blockSet.polishedTilesStairs);
        Supplier<BlockItem> polishedTilesWall = registerBlockItemAndTrack("polished_" + stoneName + "_tiles_wall", blockSet.polishedTilesWall);
        
        // Register all block items for smooth variant
        Supplier<BlockItem> smooth = registerBlockItemAndTrack("smooth_" + stoneName, blockSet.smooth);
        Supplier<BlockItem> smoothSlab = registerBlockItemAndTrack("smooth_" + stoneName + "_slab", blockSet.smoothSlab);
        Supplier<BlockItem> smoothStairs = registerBlockItemAndTrack("smooth_" + stoneName + "_stairs", blockSet.smoothStairs);
        Supplier<BlockItem> smoothWall = registerBlockItemAndTrack("smooth_" + stoneName + "_wall", blockSet.smoothWall);
        
        // Store the complete set
        StoneItemSet itemSet = new StoneItemSet(
                base, baseSlab, baseStairs, baseWall,
                polished, polishedSlab, polishedStairs, polishedWall,
                polishedBricks, polishedBricksSlab, polishedBricksStairs, polishedBricksWall,
                polishedTiles, polishedTilesSlab, polishedTilesStairs, polishedTilesWall,
                smooth, smoothSlab, smoothStairs, smoothWall);
        
        STONE_ITEM_SETS.put(stoneType, itemSet);
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