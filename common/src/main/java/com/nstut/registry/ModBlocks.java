package com.nstut.registry;

import com.nstut.Geko;
import com.nstut.block.EbonyLeavesBlock;
import com.nstut.block.EbonyLogBlock;
import com.nstut.block.EbonyPlanksBlock;
import com.nstut.block.EbonySaplingBlock;
import com.nstut.block.StrippedEbonyLogBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairsBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Geko.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Geko.MOD_ID, Registries.ITEM);

    // Ebony Wood Type & SetType
    public static final BlockSetType EBONY_BLOCK_SET_TYPE = BlockSetType.register(new BlockSetType(
            Geko.MOD_ID + ":ebony", true, SoundType.WOOD, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
            SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON
    ));
    public static final WoodType EBONY_WOOD_TYPE = WoodType.register(new WoodType(Geko.MOD_ID + ":ebony", EBONY_BLOCK_SET_TYPE));

    // Ebony Wood Set
    public static final RegistrySupplier<Block> EBONY_LOG = registerBlock("ebony_log", EbonyLogBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> STRIPPED_EBONY_LOG = registerBlock("stripped_ebony_log", StrippedEbonyLogBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_LEAVES = registerBlock("ebony_leaves", EbonyLeavesBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_SAPLING = registerBlock("ebony_sapling", EbonySaplingBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_PLANKS = registerBlock("ebony_planks", EbonyPlanksBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_STAIRS = registerBlock("ebony_stairs", () -> new StairsBlock(EBONY_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(EBONY_PLANKS.get())), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_SLAB = registerBlock("ebony_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(EBONY_PLANKS.get())), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_FENCE = registerBlock("ebony_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(EBONY_PLANKS.get())), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_FENCE_GATE = registerBlock("ebony_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(EBONY_PLANKS.get()), EBONY_WOOD_TYPE), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_BUTTON = registerBlock("ebony_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(EBONY_PLANKS.get()).noCollission(), EBONY_WOOD_TYPE, 30), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_PRESSURE_PLATE = registerBlock("ebony_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(EBONY_PLANKS.get()).noCollission(), EBONY_WOOD_TYPE), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_SIGN = registerBlock("ebony_sign", () -> new StandingSignBlock(BlockBehaviour.Properties.copy(EBONY_PLANKS.get()).noCollission().strength(1.0f), EBONY_WOOD_TYPE), new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_WALL_SIGN = registerBlock("ebony_wall_sign", () -> new WallSignBlock(BlockBehaviour.Properties.copy(EBONY_PLANKS.get()).noCollission().strength(1.0f).dropsLike(EBONY_SIGN.get()), EBONY_WOOD_TYPE), new Item.Properties());

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
