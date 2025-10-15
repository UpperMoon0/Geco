package com.nstut.neoforge.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import java.util.List;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GecoChunkGenerator extends NoiseBasedChunkGenerator {
    public static final MapCodec<GecoChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BiomeSource.CODEC.fieldOf("biome_source").forGetter(c -> c.biomeSource),
        NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(GecoChunkGenerator::getSettings)
    ).apply(instance, GecoChunkGenerator::new));


    public GecoChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyBiomeDecoration(net.minecraft.world.level.WorldGenLevel level, ChunkAccess chunk, net.minecraft.world.level.StructureManager structureManager) {
        // Call parent for normal decoration
        super.applyBiomeDecoration(level, chunk, structureManager);

        // Add our custom marble vein generation (fully code-driven, no JSON dependencies)
        generateMarbleVeins(level, chunk);

        // Force chunk to be marked as modified
        chunk.setUnsaved(true);
    }

    public Holder<NoiseGeneratorSettings> getSettings() {
        // Access the protected settings field from parent class
        try {
            java.lang.reflect.Field field = NoiseBasedChunkGenerator.class.getDeclaredField("settings");
            field.setAccessible(true);
            return (Holder<NoiseGeneratorSettings>) field.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access settings field", e);
        }
    }

    private void generateMarbleVeins(WorldGenLevel level, ChunkAccess chunk) {
        // Generate rare, large marble veins - each type in separate veins
        var random = level.getRandom();
        var chunkPos = chunk.getPos();

        // Much rarer generation - only 1 vein per chunk, but very large (30x30x30)
        // Each vein contains only one type of marble
        if (random.nextFloat() < 0.02f) { // 2% chance per chunk for any marble vein
            // Choose which marble type for this vein
            boolean isCreamMarble = random.nextBoolean();

            // Random position within chunk
            int x = chunkPos.getMinBlockX() + random.nextInt(16);
            int z = chunkPos.getMinBlockZ() + random.nextInt(16);
            int y = -60 + random.nextInt(121); // Between y=-60 and y=60

            // Generate a massive spherical vein of single marble type
            generateMarbleVein(level, new BlockPos(x, y, z), new java.util.Random(random.nextLong()), isCreamMarble);
        }
    }

    private void generateMarbleVein(WorldGenLevel level, BlockPos center, java.util.Random random, boolean isCreamMarble) {
        // Generate a massive 30x30x30 spherical vein with single marble type
        int radius = 15; // 30 blocks diameter

        // Choose the marble block type for this entire vein
        var marbleBlock = isCreamMarble ?
            com.nstut.geco.common.registry.ModBlocks.getStoneBlockSet(com.nstut.geco.common.registry.ModStoneTypes.CREAM_MARBLE).base.get() :
            com.nstut.geco.common.registry.ModBlocks.getStoneBlockSet(com.nstut.geco.common.registry.ModStoneTypes.MULTICOLOR_MARBLE).base.get();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    // Calculate spherical distance from center
                    double distanceFromCenter = Math.sqrt(dx*dx + dy*dy + dz*dz);

                    // Only place blocks within the sphere (distance <= radius)
                    if (distanceFromCenter <= radius) {
                        // Higher density in center, lower at edges (but still solid sphere)
                        float density = Math.max(0.4f, 0.9f - (float)distanceFromCenter * 0.02f); // 90% at center, down to 40% at edges

                        if (random.nextFloat() < density) {
                            BlockPos pos = center.offset(dx, dy, dz);

                            // Only replace stone, dirt, gravel, and deepslate - NOT air, water, or lava
                            var currentState = level.getBlockState(pos);
                            if (canReplaceForMarble(currentState)) {
                                level.setBlock(pos, marbleBlock.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canReplaceForMarble(net.minecraft.world.level.block.state.BlockState state) {
        // Can replace stone, dirt, gravel, deepslate, diorite, andesite, and granite - but NOT air, water, or lava
        return (state.is(net.minecraft.tags.BlockTags.STONE_ORE_REPLACEABLES) ||
                state.is(net.minecraft.tags.BlockTags.BASE_STONE_OVERWORLD) ||
                state.is(net.minecraft.tags.BlockTags.DIRT) ||
                state.is(net.minecraft.world.level.block.Blocks.GRAVEL) ||
                state.is(net.minecraft.world.level.block.Blocks.DEEPSLATE) ||
                state.is(net.minecraft.world.level.block.Blocks.DIORITE) ||
                state.is(net.minecraft.world.level.block.Blocks.ANDESITE) ||
                state.is(net.minecraft.world.level.block.Blocks.GRANITE)) &&
               !state.isAir() &&
               !state.is(net.minecraft.world.level.block.Blocks.WATER) &&
               !state.is(net.minecraft.world.level.block.Blocks.LAVA);
    }


}