package com.nstut.world.feature;

import com.nstut.Geco;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import com.nstut.registry.ModBlocks;
import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> EBONY_TREE_TYPE_A_PLACED = registerKey("ebony_tree_type_a_placed");

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, EBONY_TREE_TYPE_A_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.EBONY_TREE_TYPE_A),
                List.of(RarityFilter.onAverageOnceEvery(5), // chance
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, name));
    }
}
