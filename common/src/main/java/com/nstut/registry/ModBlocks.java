package com.nstut.registry;

import com.nstut.Geco;
import com.nstut.block.EbonyHangingSignBlock;
import com.nstut.block.EbonyLeavesBlock;
import com.nstut.block.EbonyLogBlock;
import com.nstut.block.EbonyPlanksBlock;
import com.nstut.block.EbonySaplingBlock;
import com.nstut.block.EbonySignBlock;
import com.nstut.block.EbonyWallHangingSignBlock;
import com.nstut.block.EbonyWallSignBlock;
import com.nstut.block.EbonyWoodBlock;
import com.nstut.block.StrippedEbonyLogBlock;
import com.nstut.block.StrippedEbonyWoodBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.SignItem;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Geco.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Geco.MOD_ID, Registries.ITEM);

    // Ebony Wood Type & SetType
    public static final BlockSetType EBONY_BLOCK_SET_TYPE = new BlockSetType(
            Geco.MOD_ID + ":ebony", 
            true,                    
            true,                    
            true,                    
            BlockSetType.PressurePlateSensitivity.EVERYTHING, 
            SoundType.WOOD,         
            SoundEvents.WOODEN_DOOR_CLOSE,     
            SoundEvents.WOODEN_DOOR_OPEN,      
            SoundEvents.WOODEN_TRAPDOOR_CLOSE, 
            SoundEvents.WOODEN_TRAPDOOR_OPEN,  
            SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, 
            SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,  
            SoundEvents.WOODEN_BUTTON_CLICK_OFF, 
            SoundEvents.WOODEN_BUTTON_CLICK_ON   
    );
    public static final WoodType EBONY_WOOD_TYPE = new WoodType(Geco.MOD_ID + ":ebony", EBONY_BLOCK_SET_TYPE);
    
    // Ebony Wood Set
    public static final RegistrySupplier<Block> EBONY_LOG = registerBlock("ebony_log", EbonyLogBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> STRIPPED_EBONY_LOG = registerBlock("stripped_ebony_log", StrippedEbonyLogBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_WOOD = registerBlock("ebony_wood", EbonyWoodBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> STRIPPED_EBONY_WOOD = registerBlock("stripped_ebony_wood", StrippedEbonyWoodBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_LEAVES = registerBlock("ebony_leaves", EbonyLeavesBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_SAPLING = registerBlock("ebony_sapling", EbonySaplingBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_PLANKS = registerBlock("ebony_planks", EbonyPlanksBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_STAIRS = registerBlock("ebony_stairs", () -> new StairBlock(EBONY_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_SLAB = registerBlock("ebony_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_FENCE = registerBlock("ebony_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_FENCE_GATE = registerBlock("ebony_fence_gate", () -> new FenceGateBlock(EBONY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get())), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_BUTTON = registerBlock("ebony_button", () -> new ButtonBlock(EBONY_BLOCK_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion()), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_PRESSURE_PLATE = registerBlock("ebony_pressure_plate", () -> new PressurePlateBlock(EBONY_BLOCK_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion()), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_DOOR = registerBlock("ebony_door", () -> new DoorBlock(EBONY_BLOCK_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion().strength(3.0f)), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_TRAPDOOR = registerBlock("ebony_trapdoor", () -> new TrapDoorBlock(EBONY_BLOCK_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion().strength(3.0f)), new Item.Properties());
    
    // Signs
    public static final RegistrySupplier<Block> EBONY_SIGN = BLOCKS.register("ebony_sign", () -> new EbonySignBlock(EBONY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion()));
    public static final RegistrySupplier<Block> EBONY_WALL_SIGN = BLOCKS.register("ebony_wall_sign", () -> new EbonyWallSignBlock(EBONY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion()));
    public static final RegistrySupplier<Block> EBONY_HANGING_SIGN = BLOCKS.register("ebony_hanging_sign", () -> new EbonyHangingSignBlock(EBONY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion()));
    public static final RegistrySupplier<Block> EBONY_WALL_HANGING_SIGN = BLOCKS.register("ebony_wall_hanging_sign", () -> new EbonyWallHangingSignBlock(EBONY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(EBONY_PLANKS.get()).noOcclusion()));
    
    // Sign Items
    static {
        ITEMS.register("ebony_sign", () -> new SignItem(new Item.Properties().stacksTo(16), EBONY_SIGN.get(), EBONY_WALL_SIGN.get()));
        ITEMS.register("ebony_hanging_sign", () -> new HangingSignItem(EBONY_HANGING_SIGN.get(), EBONY_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    }

    // Helper method to register blocks and their items
    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> blockSupplier, Item.Properties properties) {
        RegistrySupplier<T> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
    }
}
