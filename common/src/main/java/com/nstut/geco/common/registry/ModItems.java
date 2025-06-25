package com.nstut.geco.common.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.BoatItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    
    // Registry helper that will be set by the platform-specific code
    public static ItemRegistryHelper REGISTRY_HELPER;
    
    // List to track all registered items for automatic creative tab population
    private static final List<Supplier<? extends Item>> ALL_ITEMS = new ArrayList<>();
    
    // Block Items
    public static Supplier<BlockItem> EBONY_LOG;
    public static Supplier<BlockItem> STRIPPED_EBONY_LOG;
    public static Supplier<BlockItem> EBONY_WOOD;
    public static Supplier<BlockItem> STRIPPED_EBONY_WOOD;
    public static Supplier<BlockItem> EBONY_PLANKS;
    public static Supplier<BlockItem> EBONY_STAIRS;
    public static Supplier<BlockItem> EBONY_SLAB;
    public static Supplier<BlockItem> EBONY_FENCE;
    public static Supplier<BlockItem> EBONY_FENCE_GATE;
    public static Supplier<BlockItem> EBONY_DOOR;
    public static Supplier<BlockItem> EBONY_TRAPDOOR;
    public static Supplier<BlockItem> EBONY_PRESSURE_PLATE;
    public static Supplier<BlockItem> EBONY_BUTTON;
    public static Supplier<BlockItem> EBONY_LEAVES;
    public static Supplier<BlockItem> EBONY_SAPLING;
    
    // Sign Items
    public static Supplier<SignItem> EBONY_SIGN;
    public static Supplier<HangingSignItem> EBONY_HANGING_SIGN;
    
    // Boat Items
    public static Supplier<BoatItem> EBONY_BOAT;
    public static Supplier<BoatItem> EBONY_CHEST_BOAT;
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("ItemRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        // Block Items
        EBONY_LOG = registerBlockItemAndTrack("ebony_log", ModBlocks.EBONY_LOG);
        STRIPPED_EBONY_LOG = registerBlockItemAndTrack("stripped_ebony_log", ModBlocks.STRIPPED_EBONY_LOG);
        EBONY_WOOD = registerBlockItemAndTrack("ebony_wood", ModBlocks.EBONY_WOOD);
        STRIPPED_EBONY_WOOD = registerBlockItemAndTrack("stripped_ebony_wood", ModBlocks.STRIPPED_EBONY_WOOD);
        EBONY_PLANKS = registerBlockItemAndTrack("ebony_planks", ModBlocks.EBONY_PLANKS);
        EBONY_STAIRS = registerBlockItemAndTrack("ebony_stairs", ModBlocks.EBONY_STAIRS);
        EBONY_SLAB = registerBlockItemAndTrack("ebony_slab", ModBlocks.EBONY_SLAB);
        EBONY_FENCE = registerBlockItemAndTrack("ebony_fence", ModBlocks.EBONY_FENCE);
        EBONY_FENCE_GATE = registerBlockItemAndTrack("ebony_fence_gate", ModBlocks.EBONY_FENCE_GATE);
        EBONY_DOOR = registerBlockItemAndTrack("ebony_door", ModBlocks.EBONY_DOOR);
        EBONY_TRAPDOOR = registerBlockItemAndTrack("ebony_trapdoor", ModBlocks.EBONY_TRAPDOOR);
        EBONY_PRESSURE_PLATE = registerBlockItemAndTrack("ebony_pressure_plate", ModBlocks.EBONY_PRESSURE_PLATE);
        EBONY_BUTTON = registerBlockItemAndTrack("ebony_button", ModBlocks.EBONY_BUTTON);
        EBONY_LEAVES = registerBlockItemAndTrack("ebony_leaves", ModBlocks.EBONY_LEAVES);
        EBONY_SAPLING = registerBlockItemAndTrack("ebony_sapling", ModBlocks.EBONY_SAPLING);
        
        // Sign Items
        EBONY_SIGN = registerAndTrack("ebony_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.EBONY_SIGN.get(), ModBlocks.EBONY_WALL_SIGN.get()));
        
        EBONY_HANGING_SIGN = registerAndTrack("ebony_hanging_sign",
            () -> new HangingSignItem(ModBlocks.EBONY_HANGING_SIGN.get(), ModBlocks.EBONY_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
        
        // Boat Items
        EBONY_BOAT = registerAndTrack("ebony_boat",
            () -> new BoatItem(false, ModBoatTypes.EBONY.get(), new Item.Properties().stacksTo(1)));
        
        EBONY_CHEST_BOAT = registerAndTrack("ebony_chest_boat",
            () -> new BoatItem(true, ModBoatTypes.EBONY.get(), new Item.Properties().stacksTo(1)));
    }
    
    // Helper methods to register items and automatically track them
    private static <T extends Item> Supplier<T> registerAndTrack(String name, Supplier<T> item) {
        Supplier<T> registeredItem = REGISTRY_HELPER.registerItem(name, item);
        ALL_ITEMS.add(registeredItem);
        return registeredItem;
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