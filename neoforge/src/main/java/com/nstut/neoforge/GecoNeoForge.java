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
import com.nstut.geco.common.registry.ModBoatTypes;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
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
                        // Automatically add all registered mod items
                        ModItems.getAllItems().forEach(itemSupplier -> {
                            output.accept(itemSupplier.get());
                        });
                    })
                    .build());
           }
       };
       
       // Set up boat type registry helper
       ModBoatTypes.REGISTRY_HELPER = new ModBoatTypes.BoatTypeRegistryHelper() {
           @Override
           public Supplier<Boat.Type> registerBoatType(String name, Supplier<Block> planks) {
               // For NeoForge, we'll create a custom boat type
               // For now, return a dummy implementation - this will need platform-specific boat type creation
               return () -> Boat.Type.OAK; // Temporary placeholder
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