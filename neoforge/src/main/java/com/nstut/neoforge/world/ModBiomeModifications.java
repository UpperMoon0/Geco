package com.nstut.neoforge.world;

import com.nstut.Geko;
import com.nstut.world.feature.ModPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModBiomeModifications {    
    public static final DeferredRegister<BiomeModifier> BIOME_MODIFIERS = 
        DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Geko.MOD_ID);

    // Helper method to create biome modifier
    private static Supplier<BiomeModifier> createSavannaTreeModifier(ResourceKey<PlacedFeature> placedFeatureKey) {
        return () -> {
            // Get the Biome Registry
            @SuppressWarnings("unchecked") // Cast is necessary due to the generic nature of BuiltInRegistries.REGISTRY.get()
            Registry<Biome> biomeRegistry = (Registry<Biome>) BuiltInRegistries.REGISTRY.get(Registries.BIOME.location());
            if (biomeRegistry == null) {
                throw new IllegalStateException("Biome registry (minecraft:biome) not found in BuiltInRegistries.");
            }

            // Get Holders for specific biomes
            Holder<Biome> savannaHolder = biomeRegistry.getHolderOrThrow(Biomes.SAVANNA);
            Holder<Biome> savannaPlateauHolder = biomeRegistry.getHolderOrThrow(Biomes.SAVANNA_PLATEAU);

            // Get the Placed Feature Registry
            @SuppressWarnings("unchecked") // Cast is necessary due to the generic nature of BuiltInRegistries.REGISTRY.get()
            Registry<PlacedFeature> placedFeatureRegistry = (Registry<PlacedFeature>) BuiltInRegistries.REGISTRY.get(Registries.PLACED_FEATURE.location());
            if (placedFeatureRegistry == null) {
                throw new IllegalStateException("Placed Feature registry (minecraft:placed_feature) not found in BuiltInRegistries.");
            }
            
            // Get Holder for the specific placed feature
            Holder<PlacedFeature> featureHolder = placedFeatureRegistry.getHolderOrThrow(placedFeatureKey);

            // Create HolderSets
            HolderSet<Biome> biomeSet = HolderSet.direct(savannaHolder, savannaPlateauHolder);
            HolderSet<PlacedFeature> featureSet = HolderSet.direct(featureHolder);

            return new BiomeModifiers.AddFeaturesBiomeModifier(
                biomeSet,
                featureSet,
                GenerationStep.Decoration.VEGETAL_DECORATION
            );
        };
    }
      // Register biome modifiers using DeferredRegister (Java-based approach)
    public static final Supplier<BiomeModifier> ADD_SMALL_EBONY_TREES = BIOME_MODIFIERS.register(
        "add_small_ebony_trees", 
        createSavannaTreeModifier(ModPlacedFeatures.SMALL_EBONY_TREES_PLACED)
    );
    
    public static final Supplier<BiomeModifier> ADD_REGULAR_EBONY_TREES = BIOME_MODIFIERS.register(
        "add_regular_ebony_trees", 
        createSavannaTreeModifier(ModPlacedFeatures.EBONY_TREES_PLACED)
    );
    
    public static final Supplier<BiomeModifier> ADD_SPARSE_EBONY_TREES = BIOME_MODIFIERS.register(
        "add_sparse_ebony_trees", 
        createSavannaTreeModifier(ModPlacedFeatures.SPARSE_EBONY_TREES_PLACED)
    );
    
    public static final Supplier<BiomeModifier> ADD_BENT_EBONY_TREES = BIOME_MODIFIERS.register(
        "add_bent_ebony_trees", 
        createSavannaTreeModifier(ModPlacedFeatures.BENT_EBONY_TREES_PLACED)
    );
    
    public static final Supplier<BiomeModifier> ADD_TALL_EBONY_TREES = BIOME_MODIFIERS.register(
        "add_tall_ebony_trees", 
        createSavannaTreeModifier(ModPlacedFeatures.TALL_EBONY_TREES_PLACED)
    );
    
    public static final Supplier<BiomeModifier> ADD_LARGE_EBONY_TREES = BIOME_MODIFIERS.register(
        "add_large_ebony_trees", 
        createSavannaTreeModifier(ModPlacedFeatures.RARE_LARGE_EBONY_TREES_PLACED)
    );
    
    /**
     * Register the biome modifiers - called from the main mod class
     */
    public static void register(IEventBus modEventBus) {
        BIOME_MODIFIERS.register(modEventBus);
    }
}
