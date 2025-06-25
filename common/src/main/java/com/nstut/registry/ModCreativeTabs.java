package com.nstut.registry;

import com.nstut.geco.common.Geco;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Geco.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> GECO_TAB = CREATIVE_MODE_TABS.register("geco_tab", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0) // Added Row.TOP and 0
            .icon(() -> new ItemStack(ModBlocks.EBONY_LOG.get())) // Example icon, can be changed
            .title(Component.translatable("itemGroup." + Geco.MOD_ID + ".geco_tab"))
            .displayItems((params, output) -> {
                // Add items here
                output.accept(ModBlocks.EBONY_LOG.get());
                output.accept(ModBlocks.STRIPPED_EBONY_LOG.get());
                output.accept(ModBlocks.EBONY_WOOD.get());
                output.accept(ModBlocks.STRIPPED_EBONY_WOOD.get());
                output.accept(ModBlocks.EBONY_LEAVES.get());
                output.accept(ModBlocks.EBONY_SAPLING.get());
                output.accept(ModBlocks.EBONY_PLANKS.get()); 
                output.accept(ModBlocks.EBONY_STAIRS.get());
                output.accept(ModBlocks.EBONY_SLAB.get());
                output.accept(ModBlocks.EBONY_FENCE.get());
                output.accept(ModBlocks.EBONY_FENCE_GATE.get());
                output.accept(ModBlocks.EBONY_BUTTON.get());
                output.accept(ModBlocks.EBONY_PRESSURE_PLATE.get());
                output.accept(ModBlocks.EBONY_DOOR.get());
                output.accept(ModBlocks.EBONY_TRAPDOOR.get());
            })
            .build());

    public static void register() {
        CREATIVE_MODE_TABS.register();
    }
}
