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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Geko.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Geko.MOD_ID, Registries.ITEM);

    // Ebony Wood Set
    public static final RegistrySupplier<Block> EBONY_LOG = registerBlock("ebony_log", EbonyLogBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> STRIPPED_EBONY_LOG = registerBlock("stripped_ebony_log", StrippedEbonyLogBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_LEAVES = registerBlock("ebony_leaves", EbonyLeavesBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_SAPLING = registerBlock("ebony_sapling", EbonySaplingBlock::new, new Item.Properties());
    public static final RegistrySupplier<Block> EBONY_PLANKS = registerBlock("ebony_planks", EbonyPlanksBlock::new, new Item.Properties());

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
