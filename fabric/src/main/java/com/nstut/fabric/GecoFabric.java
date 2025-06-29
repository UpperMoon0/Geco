package com.nstut.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.nstut.geco.common.Geco;
import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.registry.ModCreativeTabs;
import java.util.function.Supplier;

public class GecoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Set up registry helpers before calling init
        setupRegistryHelpers();
        
        // Now safe to call init
        Geco.init();
    }
    
    private void setupRegistryHelpers() {
        // Set up block registry helper
        ModBlocks.REGISTRY_HELPER = new ModBlocks.BlockRegistryHelper() {
            @Override
            public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, name);
                T registeredBlock = net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.BLOCK, id, block.get());
                return () -> registeredBlock;
            }
        };
        
        // Set up item registry helper
        ModItems.REGISTRY_HELPER = new ModItems.ItemRegistryHelper() {
            @Override
            public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, name);
                T registeredItem = net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ITEM, id, item.get());
                return () -> registeredItem;
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public <T extends BlockItem> Supplier<T> registerBlockItem(String name, Supplier<?> block) {
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, name);
                BlockItem blockItem = new BlockItem((Block) block.get(), new Item.Properties());
                T registeredItem = (T) net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ITEM, id, blockItem);
                return () -> registeredItem;
            }
        };
        
        // Set up creative tab registry helper
        ModCreativeTabs.REGISTRY_HELPER = new ModCreativeTabs.CreativeTabRegistryHelper() {
            @Override
            public void registerCreativeTab(String name, java.util.List<Supplier<? extends Item>> items) {
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, name);
                CreativeModeTab tab = FabricItemGroup.builder()
                    .title(net.minecraft.network.chat.Component.translatable("itemGroup.geco.geco_tab"))
                    .icon(() -> ModItems.EBONY_LOG.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Add all provided items
                        items.forEach(itemSupplier -> {
                            output.accept(itemSupplier.get());
                        });
                    })
                    .build();
                
                net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.CREATIVE_MODE_TAB, id, tab);
            }
        };
    }
}