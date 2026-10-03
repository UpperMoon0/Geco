package com.nstut.geco.common.worldgen;

import com.mojang.serialization.MapCodec;
import com.nstut.geco.common.Geco;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Shared IDs; biome attachment is performed by each loader's supported API. */
public final class GecoWorldgen {
    public static final ResourceKey<PlacedFeature> CREAM_MARBLE = placed("cream_marble");
    public static final ResourceKey<PlacedFeature> MULTICOLOR_MARBLE = placed("multicolor_marble");
    public static final ResourceKey<PlacedFeature> EBONY_TREES = placed("ebony_trees");
    // Decode worlds created by unreleased dev builds as vanilla noise terrain.
    // A distinct codec avoids registering vanilla's codec object twice; subsequent saves use minecraft:noise.
    public static final MapCodec<NoiseBasedChunkGenerator> LEGACY_OVERWORLD =
            NoiseBasedChunkGenerator.CODEC.xmap(generator -> generator, generator -> generator);
    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Geco.id(name));
    }
    private GecoWorldgen() {}
}
