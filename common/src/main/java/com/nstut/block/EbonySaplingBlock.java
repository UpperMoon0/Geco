package com.nstut.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.MapColor;

import java.util.Optional;

public class EbonySaplingBlock extends SaplingBlock {
    public EbonySaplingBlock() {
        super(
                new TreeGrower(
                        "ebony", // A unique name for this tree type
                        // Optional<ResourceKey<ConfiguredFeature<?, ?>>> treeFeature, // regular tree
                        Optional.empty(),
                        // Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowerFeature, // tree with flowers
                        Optional.empty(),
                        // Optional<ResourceKey<ConfiguredFeature<?, ?>>> largeTreeFeature // large tree (e.g. 2x2)
                        Optional.empty()
                ),
                Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.GRASS)
        );
    }
}
