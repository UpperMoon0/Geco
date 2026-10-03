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
import com.nstut.geco.common.worldgen.*;
import com.nstut.geco.common.registry.ModWoodTypes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import java.util.function.Supplier;

public class GecoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Set up registry helpers before calling init
        setupRegistryHelpers();

        registerWorldgen();

        // Now safe to call init
        Geco.init();
        for (var wood : ModWoodTypes.REGISTERED_WOOD_TYPES) {
            var blocks = ModBlocks.getWoodBlockSet(wood);
            StrippableBlockRegistry.register(blocks.log.get(), blocks.strippedLog.get());
            StrippableBlockRegistry.register(blocks.wood.get(), blocks.strippedWood.get());
        }
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OVERWORLD),
                GenerationStep.Decoration.UNDERGROUND_ORES, GecoWorldgen.CREAM_MARBLE);
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OVERWORLD),
                GenerationStep.Decoration.UNDERGROUND_ORES, GecoWorldgen.MULTICOLOR_MARBLE);
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_SAVANNA),
                GenerationStep.Decoration.VEGETAL_DECORATION, GecoWorldgen.EBONY_TREES);
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
                    .icon(() -> {
                        // Use the first available log item as icon
                        if (!items.isEmpty()) {
                            return items.get(0).get().getDefaultInstance();
                        }
                        return net.minecraft.world.item.Items.OAK_LOG.getDefaultInstance();
                    })
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

    private void registerWorldgen() {
        var features = net.minecraft.core.registries.BuiltInRegistries.FEATURE;
        net.minecraft.core.Registry.register(features, Geco.id("marble"), new MarbleFeature());
        net.minecraft.core.Registry.register(features, Geco.id("ebony_template_tree"), new TemplateTreeFeature());
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.CHUNK_GENERATOR,
                Geco.id("overworld"), GecoWorldgen.LEGACY_OVERWORLD);
    }
}
