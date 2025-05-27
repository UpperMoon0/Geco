package com.nstut.neoforge.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nstut.Geko;
import com.nstut.world.feature.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ModBiomeModifierDataProvider implements DataProvider {
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public ModBiomeModifierDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return this.registries.thenCompose(provider -> {
            return CompletableFuture.allOf(
                this.generateBiomeModifier(cachedOutput, "add_ebony_trees", ModPlacedFeatures.EBONY_TREES_PLACED.location()),
                this.generateBiomeModifier(cachedOutput, "add_small_ebony_trees", ModPlacedFeatures.SMALL_EBONY_TREES_PLACED.location()),
                this.generateBiomeModifier(cachedOutput, "add_tall_ebony_trees", ModPlacedFeatures.TALL_EBONY_TREES_PLACED.location()),
                this.generateBiomeModifier(cachedOutput, "add_bent_ebony_trees", ModPlacedFeatures.BENT_EBONY_TREES_PLACED.location()),
                this.generateBiomeModifier(cachedOutput, "add_sparse_ebony_trees", ModPlacedFeatures.SPARSE_EBONY_TREES_PLACED.location()),
                this.generateBiomeModifier(cachedOutput, "add_rare_large_ebony_trees", ModPlacedFeatures.RARE_LARGE_EBONY_TREES_PLACED.location())
            );
        });
    }

    private CompletableFuture<?> generateBiomeModifier(CachedOutput cachedOutput, String name, ResourceLocation placedFeature) {
        JsonObject biomeModifier = new JsonObject();
        biomeModifier.addProperty("type", "neoforge:add_features");
        
        // Add biomes array (savanna biomes)
        JsonArray biomes = new JsonArray();
        biomes.add("minecraft:savanna");
        biomes.add("minecraft:savanna_plateau");
        biomes.add("minecraft:windswept_savanna");
        biomeModifier.add("biomes", biomes);
        
        // Add features array
        JsonArray features = new JsonArray();
        features.add(placedFeature.toString());
        biomeModifier.add("features", features);
        
        // Add step
        biomeModifier.addProperty("step", "vegetal_decoration");

        Path path = this.output.getOutputFolder()
                .resolve("data")
                .resolve(Geko.MOD_ID)
                .resolve("neoforge")
                .resolve("biome_modifier")
                .resolve(name + ".json");

        return DataProvider.saveStable(cachedOutput, biomeModifier, path);
    }

    @Override
    public String getName() {
        return "Biome Modifiers: " + Geko.MOD_ID;
    }
}
