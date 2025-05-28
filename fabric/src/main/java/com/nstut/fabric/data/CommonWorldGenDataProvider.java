package com.nstut.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class CommonWorldGenDataProvider extends FabricDynamicRegistryProvider {
    
    public CommonWorldGenDataProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        // Output to common module's generated resources
        super(createCommonOutput(output), registriesFuture);
    }
    
    private static FabricDataOutput createCommonOutput(FabricDataOutput fabricOutput) {
        // Create a custom output that points to the common module
        var commonPath = fabricOutput.getOutputFolder().getParent().getParent().resolve("common").resolve("src/generated/resources");
        return new FabricDataOutput(fabricOutput.getModContainer(), commonPath, fabricOutput.isStrictValidationEnabled());
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        // Generate data from our common data generators
        entries.addAll(registries.lookupOrThrow(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE));
        entries.addAll(registries.lookupOrThrow(net.minecraft.core.registries.Registries.PLACED_FEATURE));
    }

    @Override
    public String getName() {
        return "Geko WorldGen Data (Common)";
    }
}
