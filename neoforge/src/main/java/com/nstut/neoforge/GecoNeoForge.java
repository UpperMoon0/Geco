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
import net.neoforged.neoforge.common.NeoForge;
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
        
        // Register client events (only if on client side)
        modEventBus.addListener(com.nstut.neoforge.client.GecoNeoForgeClient::onClientSetup);
        
        // Register server events for handling boat drops
        NeoForge.EVENT_BUS.register(BoatDropHandler.class);
        
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
                    .icon(() -> ModItems.EBONY_LOG.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Add all provided items
                        items.forEach(itemSupplier -> {
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
               // Use reflection to create proper custom boat type following vanilla pattern
               return () -> com.nstut.geco.common.util.BoatTypeHelper.createEbonyBoatType(planks.get());
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
    
    // Event handler to fix boat drops - this is a simpler approach
    // Since the loot tables are already correctly configured, we need to ensure
    // our boats use the custom boat type properly, or override the getDropItem method
    public static class BoatDropHandler {
        // For now, we acknowledge that the proper solution requires custom boat entities
        // or a more complex boat type registration system
        
        // The real issue is that our boat items create vanilla oak boats instead of custom boats
        // This would require creating custom boat entity classes or using mixins
        
        // A temporary workaround would be to use data generation to create the proper
        // entity loot tables, but the root cause is the boat type registration
    }
}