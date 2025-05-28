package com.nstut.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class FabricWorldGenDataProvider extends FabricDynamicRegistryProvider {
    
    public FabricWorldGenDataProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        // Add all configured features from the vanilla registry
        entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
        
        // Add all placed features from the vanilla registry  
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
    }

    @Override
    public String getName() {
        return "Geko WorldGen Data";
    }
}
