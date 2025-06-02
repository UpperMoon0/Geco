package com.nstut.fabric.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.nstut.Geco;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class NeoForgeBiomeModifierDataProvider implements DataProvider {
    
    private final FabricDataOutput output;
    
    public NeoForgeBiomeModifierDataProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        // TODO: Add biome modifier data generation for the new Type A ebony tree variant
        return CompletableFuture.completedFuture(null);
    }
    
    private CompletableFuture<?> createBiomeModifier(CachedOutput cache, String name, String featureName) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "neoforge:add_features");
        
        JsonArray biomes = new JsonArray();
        biomes.add("minecraft:savanna");
        biomes.add("minecraft:savanna_plateau");
        json.add("biomes", biomes);
        
        json.addProperty("features", featureName);
        json.addProperty("step", "vegetal_decoration");
        
        // Output to the neoforge module's generated resources directory
        Path neoforgeModulePath = output.getOutputFolder().getParent().getParent().getParent().getParent().resolve("neoforge");
        Path outputPath = neoforgeModulePath
            .resolve("src/generated/resources/data")
            .resolve(Geco.MOD_ID)
            .resolve("neoforge/biome_modifier")
            .resolve(name + ".json");
            
        return DataProvider.saveStable(cache, json, outputPath);
    }

    @Override
    public String getName() {
        return "NeoForge Biome Modifiers (from Fabric)";
    }
}
