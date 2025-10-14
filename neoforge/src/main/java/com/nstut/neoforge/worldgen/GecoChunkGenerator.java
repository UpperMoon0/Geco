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

    static {
        com.nstut.geco.common.Geco.LOGGER.info("GecoChunkGenerator: Static initializer called - CODEC registered");
    }

    public GecoChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyBiomeDecoration(net.minecraft.world.level.WorldGenLevel level, ChunkAccess chunk, net.minecraft.world.level.StructureManager structureManager) {
        // DEBUG: Log that our chunk generator is being used
        com.nstut.geco.common.Geco.LOGGER.info("GecoChunkGenerator: applyBiomeDecoration called for chunk {}", chunk.getPos());

        // Call parent for normal decoration
        super.applyBiomeDecoration(level, chunk, structureManager);

        // Add our custom marble vein generation (fully code-driven, no JSON dependencies)
        generateMarbleVeins(level, chunk);

        // DEBUG: Add visible test marble blocks near spawn
        addTestMarbles(level, chunk);

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
        // DEBUG: Log marble generation
        com.nstut.geco.common.Geco.LOGGER.info("GecoChunkGenerator: Generating marble veins for chunk {}", chunk.getPos());

        // Simple marble vein generation logic
        var random = level.getRandom();
        var chunkPos = chunk.getPos();

        // Generate 3-8 large marble chunks per chunk (reduced count but much larger size)
        int veinCount = 3 + random.nextInt(6);
        com.nstut.geco.common.Geco.LOGGER.info("GecoChunkGenerator: Generating {} large marble chunks", veinCount);

        for (int i = 0; i < veinCount; i++) {
            // Random position within chunk
            int x = chunkPos.getMinBlockX() + random.nextInt(16);
            int z = chunkPos.getMinBlockZ() + random.nextInt(16);
            int y = -60 + random.nextInt(121); // Between y=-60 and y=60 (slightly reduced range)

            // Generate a large cluster of marble blocks
            generateMarbleCluster(level, new BlockPos(x, y, z), new java.util.Random(random.nextLong()));
        }
    }

    private void generateMarbleCluster(WorldGenLevel level, BlockPos center, java.util.Random random) {
        // Generate a large 7x7x7 cluster with some randomness (much bigger than 3x3x3)
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    // Higher chance to place marble in center, lower at edges
                    float distanceFromCenter = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
                    float placeChance = Math.max(0.3f, 0.8f - distanceFromCenter * 0.1f); // 80% at center, down to 30% at edges

                    if (random.nextFloat() < placeChance) {
                        BlockPos pos = center.offset(dx, dy, dz);

                        // Only replace stone-like blocks
                        var currentState = level.getBlockState(pos);
                        if (isStone(currentState) || isDirt(currentState) || currentState.isAir()) {
                            // Choose between cream and multi-color marble
                            var marbleBlock = random.nextBoolean() ?
                                com.nstut.geco.common.registry.ModBlocks.getStoneBlockSet(com.nstut.geco.common.registry.ModStoneTypes.CREAM_MARBLE).base.get() :
                                com.nstut.geco.common.registry.ModBlocks.getStoneBlockSet(com.nstut.geco.common.registry.ModStoneTypes.MULTICOLOR_MARBLE).base.get();

                            level.setBlock(pos, marbleBlock.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
    }

    private boolean isStone(net.minecraft.world.level.block.state.BlockState state) {
        return state.is(net.minecraft.tags.BlockTags.STONE_ORE_REPLACEABLES) ||
               state.is(net.minecraft.tags.BlockTags.BASE_STONE_OVERWORLD);
    }

    private boolean isDirt(net.minecraft.world.level.block.state.BlockState state) {
        return state.is(net.minecraft.tags.BlockTags.DIRT);
    }

    private void addTestMarbles(net.minecraft.world.level.WorldGenLevel level, ChunkAccess chunk) {
        var chunkPos = chunk.getPos();

        // DEBUG: Log test marble generation
        com.nstut.geco.common.Geco.LOGGER.info("GecoChunkGenerator: Adding test marbles for chunk {}", chunkPos);

        // Only add test marbles near spawn (chunk 0,0)
        if (Math.abs(chunkPos.x) > 2 || Math.abs(chunkPos.z) > 2) {
            return;
        }

        // Add visible test marble blocks at y=100 (surface level)
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BlockPos surfacePos = new BlockPos(
                    chunkPos.getMinBlockX() + x,
                    100,
                    chunkPos.getMinBlockZ() + z
                );

                // Place alternating cream and multi-color marble
                var marbleBlock = ((x + z) % 2 == 0) ?
                    com.nstut.geco.common.registry.ModBlocks.getStoneBlockSet(com.nstut.geco.common.registry.ModStoneTypes.CREAM_MARBLE).base.get() :
                    com.nstut.geco.common.registry.ModBlocks.getStoneBlockSet(com.nstut.geco.common.registry.ModStoneTypes.MULTICOLOR_MARBLE).base.get();

                level.setBlock(surfacePos, marbleBlock.defaultBlockState(), 2);
                com.nstut.geco.common.Geco.LOGGER.info("GecoChunkGenerator: Placed test marble at {}", surfacePos);
            }
        }
    }

}