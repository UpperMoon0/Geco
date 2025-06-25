package com.nstut.geco.common.registry;

import com.nstut.geco.common.block.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

public class ModBlocks {
    
    // Registry helper that will be set by the platform-specific code
    public static BlockRegistryHelper REGISTRY_HELPER;
    
    // Ebony Wood Blocks
    public static Supplier<Block> EBONY_LOG;
    public static Supplier<Block> STRIPPED_EBONY_LOG;
    public static Supplier<Block> EBONY_WOOD;
    public static Supplier<Block> STRIPPED_EBONY_WOOD;
    public static Supplier<Block> EBONY_PLANKS;
    public static Supplier<Block> EBONY_STAIRS;
    public static Supplier<Block> EBONY_SLAB;
    public static Supplier<Block> EBONY_FENCE;
    public static Supplier<Block> EBONY_FENCE_GATE;
    public static Supplier<Block> EBONY_DOOR;
    public static Supplier<Block> EBONY_TRAPDOOR;
    public static Supplier<Block> EBONY_PRESSURE_PLATE;
    public static Supplier<Block> EBONY_BUTTON;
    public static Supplier<Block> EBONY_SIGN;
    public static Supplier<Block> EBONY_WALL_SIGN;
    public static Supplier<Block> EBONY_HANGING_SIGN;
    public static Supplier<Block> EBONY_WALL_HANGING_SIGN;
    
    // Ebony Natural Blocks
    public static Supplier<Block> EBONY_LEAVES;
    public static Supplier<Block> EBONY_SAPLING;
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("BlockRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        EBONY_LOG = REGISTRY_HELPER.registerBlock("ebony_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()));
        
        STRIPPED_EBONY_LOG = REGISTRY_HELPER.registerBlock("stripped_ebony_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()));
        
        EBONY_WOOD = REGISTRY_HELPER.registerBlock("ebony_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()));
        
        STRIPPED_EBONY_WOOD = REGISTRY_HELPER.registerBlock("stripped_ebony_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()));
        
        EBONY_PLANKS = REGISTRY_HELPER.registerBlock("ebony_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()));
        
        EBONY_STAIRS = REGISTRY_HELPER.registerBlock("ebony_stairs",
            () -> new StairBlock(EBONY_PLANKS.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())));
        
        EBONY_SLAB = REGISTRY_HELPER.registerBlock("ebony_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())));
        
        EBONY_FENCE = REGISTRY_HELPER.registerBlock("ebony_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())));
        
        EBONY_FENCE_GATE = REGISTRY_HELPER.registerBlock("ebony_fence_gate",
            () -> new FenceGateBlock(ModWoodTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())));
        
        EBONY_DOOR = REGISTRY_HELPER.registerBlock("ebony_door",
            () -> new DoorBlock(ModBlockSetTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())
                .noOcclusion().pushReaction(PushReaction.DESTROY)));
        
        EBONY_TRAPDOOR = REGISTRY_HELPER.registerBlock("ebony_trapdoor",
            () -> new TrapDoorBlock(ModBlockSetTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())
                .noOcclusion()));
        
        EBONY_PRESSURE_PLATE = REGISTRY_HELPER.registerBlock("ebony_pressure_plate",
            () -> new PressurePlateBlock(ModBlockSetTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())
                .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
        
        EBONY_BUTTON = REGISTRY_HELPER.registerBlock("ebony_button",
            () -> new ButtonBlock(ModBlockSetTypes.EBONY, 30, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())
                .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
        
        EBONY_SIGN = REGISTRY_HELPER.registerBlock("ebony_sign",
            () -> new EbonySignBlock(ModWoodTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())
                .noCollission().strength(1.0F).ignitedByLava()));
        
        EBONY_WALL_SIGN = REGISTRY_HELPER.registerBlock("ebony_wall_sign",
            () -> new EbonyWallSignBlock(ModWoodTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_SIGN.get())
                .dropsLike(EBONY_SIGN.get())));
        
        EBONY_HANGING_SIGN = REGISTRY_HELPER.registerBlock("ebony_hanging_sign",
            () -> new EbonyHangingSignBlock(ModWoodTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())
                .noCollission().strength(1.0F).ignitedByLava()));
        
        EBONY_WALL_HANGING_SIGN = REGISTRY_HELPER.registerBlock("ebony_wall_hanging_sign",
            () -> new EbonyWallHangingSignBlock(ModWoodTypes.EBONY, BlockBehaviour.Properties.ofFullCopy(EBONY_HANGING_SIGN.get())
                .dropsLike(EBONY_HANGING_SIGN.get())));
        
        // Ebony Natural Blocks
        EBONY_LEAVES = REGISTRY_HELPER.registerBlock("ebony_leaves",
            () -> new EbonyLeavesBlock());
        
        EBONY_SAPLING = REGISTRY_HELPER.registerBlock("ebony_sapling",
            () -> new EbonySaplingBlock());
    }
    
    // Interface that platform-specific code must implement
    public interface BlockRegistryHelper {
        <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block);
    }
}