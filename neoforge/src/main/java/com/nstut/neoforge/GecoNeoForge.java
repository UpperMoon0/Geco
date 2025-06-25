package com.nstut.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.network.chat.Component;
import com.nstut.geco.common.Geco;
import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.registry.ModCreativeTabs;
import java.util.function.Supplier;

@Mod(Geco.MOD_ID)
public class GecoNeoForge {
    
    // NeoForge registries
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Geco.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Geco.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Geco.MOD_ID);
    
    public GecoNeoForge(IEventBus modEventBus) {
        // Set up registry helpers before calling init
        setupRegistryHelpers();
        
        // Register the deferred registers
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        
        // Now safe to call init
        Geco.init();
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }
    
    private void setupRegistryHelpers() {
        // Set up block registry helper
        ModBlocks.REGISTRY_HELPER = new ModBlocks.BlockRegistryHelper() {
            @Override
            public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
                return BLOCKS.register(name, block);
            }
        };
        
        // Set up item registry helper
        ModItems.REGISTRY_HELPER = new ModItems.ItemRegistryHelper() {
            @Override
            public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
                return ITEMS.register(name, item);
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public <T extends BlockItem> Supplier<T> registerBlockItem(String name, Supplier<?> block) {
                return (Supplier<T>) ITEMS.register(name, () -> new BlockItem((Block) block.get(), new Item.Properties()));
            }
        };
        
        // Set up creative tab registry helper
        ModCreativeTabs.REGISTRY_HELPER = new ModCreativeTabs.CreativeTabRegistryHelper() {
            @Override
            public Supplier<CreativeModeTab> registerCreativeTab(String name) {
                return CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.geco.geco_tab"))
                    .icon(() -> ModItems.EBONY_LOG.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Add all items to the creative tab
                        output.accept(ModItems.EBONY_LOG.get());
                        output.accept(ModItems.STRIPPED_EBONY_LOG.get());
                        output.accept(ModItems.EBONY_WOOD.get());
                        output.accept(ModItems.STRIPPED_EBONY_WOOD.get());
                        output.accept(ModItems.EBONY_PLANKS.get());
                        output.accept(ModItems.EBONY_STAIRS.get());
                        output.accept(ModItems.EBONY_SLAB.get());
                        output.accept(ModItems.EBONY_FENCE.get());
                        output.accept(ModItems.EBONY_FENCE_GATE.get());
                        output.accept(ModItems.EBONY_DOOR.get());
                        output.accept(ModItems.EBONY_TRAPDOOR.get());
                        output.accept(ModItems.EBONY_PRESSURE_PLATE.get());
                        output.accept(ModItems.EBONY_BUTTON.get());
                        output.accept(ModItems.EBONY_SIGN.get());
                        output.accept(ModItems.EBONY_HANGING_SIGN.get());
                        output.accept(ModItems.EBONY_LEAVES.get());
                        output.accept(ModItems.EBONY_SAPLING.get());
                    })
                    .build());
            }
        };
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {

    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        // Client setup
    }
    
    public static class ClientModEvents {
        // Client-side event handlers
    }
}