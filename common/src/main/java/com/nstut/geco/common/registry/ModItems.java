package com.nstut.geco.common.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.SignItem;

import java.util.function.Supplier;

public class ModItems {
    
    // Registry helper that will be set by the platform-specific code
    public static ItemRegistryHelper REGISTRY_HELPER;
    
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
    
    // Sign Items
    public static Supplier<SignItem> EBONY_SIGN;
    public static Supplier<HangingSignItem> EBONY_HANGING_SIGN;
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("ItemRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        // Block Items
        EBONY_LOG = REGISTRY_HELPER.registerBlockItem("ebony_log", ModBlocks.EBONY_LOG);
        STRIPPED_EBONY_LOG = REGISTRY_HELPER.registerBlockItem("stripped_ebony_log", ModBlocks.STRIPPED_EBONY_LOG);
        EBONY_WOOD = REGISTRY_HELPER.registerBlockItem("ebony_wood", ModBlocks.EBONY_WOOD);
        STRIPPED_EBONY_WOOD = REGISTRY_HELPER.registerBlockItem("stripped_ebony_wood", ModBlocks.STRIPPED_EBONY_WOOD);
        EBONY_PLANKS = REGISTRY_HELPER.registerBlockItem("ebony_planks", ModBlocks.EBONY_PLANKS);
        EBONY_STAIRS = REGISTRY_HELPER.registerBlockItem("ebony_stairs", ModBlocks.EBONY_STAIRS);
        EBONY_SLAB = REGISTRY_HELPER.registerBlockItem("ebony_slab", ModBlocks.EBONY_SLAB);
        EBONY_FENCE = REGISTRY_HELPER.registerBlockItem("ebony_fence", ModBlocks.EBONY_FENCE);
        EBONY_FENCE_GATE = REGISTRY_HELPER.registerBlockItem("ebony_fence_gate", ModBlocks.EBONY_FENCE_GATE);
        EBONY_DOOR = REGISTRY_HELPER.registerBlockItem("ebony_door", ModBlocks.EBONY_DOOR);
        EBONY_TRAPDOOR = REGISTRY_HELPER.registerBlockItem("ebony_trapdoor", ModBlocks.EBONY_TRAPDOOR);
        EBONY_PRESSURE_PLATE = REGISTRY_HELPER.registerBlockItem("ebony_pressure_plate", ModBlocks.EBONY_PRESSURE_PLATE);
        EBONY_BUTTON = REGISTRY_HELPER.registerBlockItem("ebony_button", ModBlocks.EBONY_BUTTON);
        
        // Sign Items
        EBONY_SIGN = REGISTRY_HELPER.registerItem("ebony_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.EBONY_SIGN.get(), ModBlocks.EBONY_WALL_SIGN.get()));
        
        EBONY_HANGING_SIGN = REGISTRY_HELPER.registerItem("ebony_hanging_sign",
            () -> new HangingSignItem(ModBlocks.EBONY_HANGING_SIGN.get(), ModBlocks.EBONY_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    }
    
    // Interface that platform-specific code must implement
    public interface ItemRegistryHelper {
        <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);
        <T extends BlockItem> Supplier<T> registerBlockItem(String name, Supplier<?> block);
    }
}