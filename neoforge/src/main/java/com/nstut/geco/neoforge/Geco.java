package com.nstut.geco.neoforge;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Geco.MODID)
public class Geco {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "geco";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "geco" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "geco" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "geco" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates the main Geco creative tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GECO_TAB = CREATIVE_MODE_TABS.register("geco_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.geco")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS)
            .icon(() -> com.nstut.geco.common.registry.ModItems.EBONY_PLANKS.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // Add all mod items to the tab
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_LOG.get());
                output.accept(com.nstut.geco.common.registry.ModItems.STRIPPED_EBONY_LOG.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_WOOD.get());
                output.accept(com.nstut.geco.common.registry.ModItems.STRIPPED_EBONY_WOOD.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_PLANKS.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_STAIRS.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_SLAB.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_FENCE.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_FENCE_GATE.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_DOOR.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_TRAPDOOR.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_PRESSURE_PLATE.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_BUTTON.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_SIGN.get());
                output.accept(com.nstut.geco.common.registry.ModItems.EBONY_HANGING_SIGN.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Geco(IEventBus modEventBus, ModContainer modContainer) {
        // Set up the registry helpers for the common module BEFORE registering anything
        com.nstut.geco.common.registry.ModBlocks.REGISTRY_HELPER = new com.nstut.geco.common.registry.ModBlocks.BlockRegistryHelper() {
            @Override
            public <T extends Block> java.util.function.Supplier<T> registerBlock(String name, java.util.function.Supplier<T> supplier) {
                DeferredBlock<T> deferred = BLOCKS.register(name, supplier);
                return deferred;
            }
        };
        
        // Set up the item registry helper for the common module
        com.nstut.geco.common.registry.ModItems.REGISTRY_HELPER = new com.nstut.geco.common.registry.ModItems.ItemRegistryHelper() {
            @Override
            public <T extends Item> java.util.function.Supplier<T> registerItem(String name, java.util.function.Supplier<T> supplier) {
                DeferredItem<T> deferred = ITEMS.register(name, supplier);
                return deferred;
            }
            
            @Override
            public <T extends BlockItem> java.util.function.Supplier<T> registerBlockItem(String name, java.util.function.Supplier<?> block) {
                DeferredItem<BlockItem> deferred = ITEMS.registerSimpleBlockItem(name, () -> (Block) block.get());
                return (java.util.function.Supplier<T>) deferred;
            }
        };
        
        // Set up the creative tab registry helper for the common module
        com.nstut.geco.common.registry.ModCreativeTabs.REGISTRY_HELPER = new com.nstut.geco.common.registry.ModCreativeTabs.CreativeTabRegistryHelper() {
            @Override
            public java.util.function.Supplier<CreativeModeTab> registerCreativeTab(String name) {
                return GECO_TAB;
            }
        };
        
        // Initialize common module NOW (during constructor when registrations are allowed)
        com.nstut.geco.common.Geco.init();
        LOGGER.info("Common module initialized successfully");

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Geco) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add items to existing creative tabs if needed
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // This method can be used to add items to vanilla creative tabs if desired
        // For now, all items are in our custom tab
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
