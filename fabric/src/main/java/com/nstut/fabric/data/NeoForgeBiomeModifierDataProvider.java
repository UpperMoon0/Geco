package com.nstut.fabric.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.nstut.Geko;
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
        return CompletableFuture.allOf(
            createBiomeModifier(cache, "add_small_ebony_trees", "geko:small_ebony_trees_placed"),
            createBiomeModifier(cache, "add_regular_ebony_trees", "geko:ebony_trees_placed"),
            createBiomeModifier(cache, "add_sparse_ebony_trees", "geko:sparse_ebony_trees_placed"),
            createBiomeModifier(cache, "add_bent_ebony_trees", "geko:bent_ebony_trees_placed"),
            createBiomeModifier(cache, "add_tall_ebony_trees", "geko:tall_ebony_trees_placed"),
            createBiomeModifier(cache, "add_large_ebony_trees", "geko:rare_large_ebony_trees_placed")
        );
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
        
        // Output directly to the neoforge module's resources directory
        Path neoforgeModulePath = output.getOutputFolder().getParent().getParent().resolve("neoforge");
        Path outputPath = neoforgeModulePath
            .resolve("src/main/resources/data")
            .resolve(Geko.MOD_ID)
            .resolve("neoforge/biome_modifier")
            .resolve(name + ".json");
            
        return DataProvider.saveStable(cache, json, outputPath);
    }

    @Override
    public String getName() {
        return "NeoForge Biome Modifiers (from Fabric)";
    }
}
