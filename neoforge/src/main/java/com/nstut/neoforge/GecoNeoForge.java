package com.nstut.neoforge;

import com.nstut.neoforge.worldgen.GecoNeoForgeWorldgen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.network.chat.Component;
import com.mojang.serialization.MapCodec;
import com.nstut.geco.common.Geco;
import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.registry.ModCreativeTabs;
import com.nstut.geco.common.registry.ModStoneTypes;
import com.nstut.neoforge.worldgen.GecoChunkGenerator;
import java.util.function.Supplier;

@Mod(Geco.MOD_ID)
public class GecoNeoForge {
    // NeoForge registries
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Geco.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Geco.MOD_ID);
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Geco.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Geco.MOD_ID);
    // Chunk generator registration - TFC style
    public static final DeferredRegister<MapCodec<? extends net.minecraft.world.level.chunk.ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, Geco.MOD_ID);

    // Chunk generator registration - temporarily disabled
    /*
    static {
        CHUNK_GENERATORS.register("geco_overworld", () -> GecoChunkGenerator.CODEC);
    }
    */

    // Chunk generator registration - TFC style (moved to top)

    static {
        CHUNK_GENERATORS.register("overworld", () -> GecoChunkGenerator.CODEC);
        Geco.LOGGER.info("Geco: Registered chunk generator 'geco:overworld'");
        Geco.LOGGER.info("Geco: Chunk generator registry key: {}", CHUNK_GENERATORS.getRegistryKey());
        Geco.LOGGER.info("Geco: Chunk generator registry name: {}", CHUNK_GENERATORS.getRegistryName());
    }
    
    public GecoNeoForge(IEventBus modEventBus) {
        Geco.LOGGER.info("GecoNeoForge: Constructor called");

        // Set up registry helpers before calling init
        setupRegistryHelpers();

        // Register the deferred registers
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        FEATURES.register(modEventBus);
        CHUNK_GENERATORS.register(modEventBus); // DEBUG: Force enabled for testing
        GecoNeoForgeWorldgen.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        Geco.LOGGER.info("GecoNeoForge: Deferred registers registered");

        // Now safe to call init
        Geco.init();

        // Add features - temporarily disabled until NeoForge API is fixed
        // GecoNeoForgeWorldgen.addFeatures();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // Register client events (only if on client side)
        modEventBus.addListener(com.nstut.neoforge.client.GecoNeoForgeClient::onClientSetup);

        Geco.LOGGER.info("GecoNeoForge: Initialization complete");
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
            public void registerCreativeTab(String name, java.util.List<Supplier<? extends Item>> items) {
                CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.geco.geco_tab"))
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