package com.nstut.neoforge.datagen;

import com.nstut.Geko;
import com.nstut.datagen.provider.ModDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Geko.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDatagenInitializer {
    
        @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        // Create registry set builder with both shared and NeoForge-specific data
        RegistrySetBuilder registrySetBuilder = ModDataProvider.getBaseRegistrySetBuilder()
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifierProvider::bootstrap);
        
        // Add our world generation data provider
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                packOutput, 
                lookupProvider, 
                registrySetBuilder, 
                Set.of("minecraft", "geko")
        ));
    }
}
