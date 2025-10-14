package com.nstut.neoforge.worldgen;

import com.nstut.geco.common.Geco;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;

public class GecoNeoForgeWorldgen {
    // Custom marble generation - fully code-driven approach
    // No JSON files needed, everything is registered in code

    public static void register(IEventBus eventBus) {
        // No deferred registers needed for pure code-driven approach
    }


    public static void addFeatures() {
        // Ebony tree (working)
        // Temporarily disabled until NeoForge API is fixed
        /*
        BiomeModifiers.addProperties(context -> context.hasTag(BiomeTags.IS_OVERWORLD), (context, properties) -> {
            properties.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, Geco.id("ebony_tree")));
        });
        */

        // Marble features - fully custom approach using chunk generator
        // No vanilla placed features needed - handled by GecoChunkGenerator
    }

}