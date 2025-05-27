package com.nstut.neoforge.datagen;

import com.nstut.Geko;
import com.nstut.world.feature.ModPlacedFeatures;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifierProvider {
    
    // Resource keys for biome modifiers
    public static final ResourceKey<BiomeModifier> ADD_EBONY_TREES = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "add_ebony_trees")
    );
    
    public static final ResourceKey<BiomeModifier> ADD_SMALL_EBONY_TREES = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "add_small_ebony_trees")
    );
    
    public static final ResourceKey<BiomeModifier> ADD_TALL_EBONY_TREES = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "add_tall_ebony_trees")
    );
    
    public static final ResourceKey<BiomeModifier> ADD_BENT_EBONY_TREES = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "add_bent_ebony_trees")
    );
    
    public static final ResourceKey<BiomeModifier> ADD_SPARSE_EBONY_TREES = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "add_sparse_ebony_trees")
    );
    
    public static final ResourceKey<BiomeModifier> ADD_RARE_LARGE_EBONY_TREES = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            ResourceLocation.fromNamespaceAndPath(Geko.MOD_ID, "add_rare_large_ebony_trees")
    );      public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        System.out.println("DEBUG: ModBiomeModifierProvider.bootstrap() called!");
        try {
            var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
            var biomes = context.lookup(Registries.BIOME);
            System.out.println("DEBUG: Successfully got registry lookups");
        
            // Get savanna biomes - you can customize this to target specific biomes
            HolderSet<Biome> savannaBiomes = HolderSet.direct(
                    biomes.getOrThrow(Biomes.SAVANNA),
                    biomes.getOrThrow(Biomes.SAVANNA_PLATEAU),
                    biomes.getOrThrow(Biomes.WINDSWEPT_SAVANNA)
            );
            System.out.println("DEBUG: Successfully got savanna biomes");
        
            // Register biome modifiers for each tree type
            System.out.println("DEBUG: About to register first biome modifier...");
            context.register(ADD_EBONY_TREES, new BiomeModifiers.AddFeaturesBiomeModifier(
                    savannaBiomes,
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EBONY_TREES_PLACED)),
                    GenerationStep.Decoration.VEGETAL_DECORATION
            ));
            System.out.println("DEBUG: Successfully registered ADD_EBONY_TREES");
        
            context.register(ADD_SMALL_EBONY_TREES, new BiomeModifiers.AddFeaturesBiomeModifier(
                    savannaBiomes,
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SMALL_EBONY_TREES_PLACED)),
                    GenerationStep.Decoration.VEGETAL_DECORATION
            ));
            System.out.println("DEBUG: Successfully registered ADD_SMALL_EBONY_TREES");
        
            context.register(ADD_TALL_EBONY_TREES, new BiomeModifiers.AddFeaturesBiomeModifier(
                    savannaBiomes,
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.TALL_EBONY_TREES_PLACED)),
                    GenerationStep.Decoration.VEGETAL_DECORATION
            ));
            System.out.println("DEBUG: Successfully registered ADD_TALL_EBONY_TREES");
        
            context.register(ADD_BENT_EBONY_TREES, new BiomeModifiers.AddFeaturesBiomeModifier(
                    savannaBiomes,
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BENT_EBONY_TREES_PLACED)),
                    GenerationStep.Decoration.VEGETAL_DECORATION
            ));
            System.out.println("DEBUG: Successfully registered ADD_BENT_EBONY_TREES");
        
            context.register(ADD_SPARSE_EBONY_TREES, new BiomeModifiers.AddFeaturesBiomeModifier(
                    savannaBiomes,
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SPARSE_EBONY_TREES_PLACED)),
                    GenerationStep.Decoration.VEGETAL_DECORATION
            ));
            System.out.println("DEBUG: Successfully registered ADD_SPARSE_EBONY_TREES");
        
            context.register(ADD_RARE_LARGE_EBONY_TREES, new BiomeModifiers.AddFeaturesBiomeModifier(
                    savannaBiomes,
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.RARE_LARGE_EBONY_TREES_PLACED)),
                    GenerationStep.Decoration.VEGETAL_DECORATION
            ));
            System.out.println("DEBUG: Successfully registered ADD_RARE_LARGE_EBONY_TREES");
            System.out.println("DEBUG: All biome modifiers registered successfully!");
          } catch (Exception e) {
            System.err.println("ERROR in ModBiomeModifierProvider.bootstrap(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
