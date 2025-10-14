package com.nstut.geco.common.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nstut.geco.common.Geco;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GecoChunkGenerator extends ChunkGenerator {
    public static final MapCodec<GecoChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BiomeSource.CODEC.fieldOf("biome_source").forGetter(c -> c.biomeSource),
        NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(c -> c.settings)
    ).apply(instance, GecoChunkGenerator::new));

    private final BiomeSource biomeSource;
    private final Holder<NoiseGeneratorSettings> settings;

    public GecoChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource);
        this.biomeSource = biomeSource;
        this.settings = settings;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyBiomeDecoration(net.minecraft.world.level.WorldGenLevel level, ChunkAccess chunk, net.minecraft.world.level.StructureManager structureManager) {
        // Basic implementation - can be expanded later
    }

    @Override
    public void buildSurface(WorldGenRegion level, net.minecraft.world.level.StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
        // Generate marble veins instead of random replacement
        RandomSource random = RandomSource.create(chunk.getPos().x * 341873128712L + chunk.getPos().z * 132897987541L);
        int replacements = 0;

        // Generate multicolor marble veins
        replacements += generateMarbleVein(level, chunk, random, "geco:multicolor_marble", 0.02f); // 2% chance per chunk

        // Generate cream marble veins
        replacements += generateMarbleVein(level, chunk, random, "geco:cream_marble", 0.02f); // 2% chance per chunk
    }

    private int generateMarbleVein(WorldGenRegion level, ChunkAccess chunk, RandomSource random, String marbleBlockId, float veinChance) {
        int replacements = 0;

        // Check if we should generate a vein in this chunk
        if (random.nextFloat() >= veinChance) {
            return 0; // No vein in this chunk
        }

        // Generate vein center position (somewhere in this chunk or adjacent)
        int veinCenterX = chunk.getPos().getMinBlockX() + random.nextInt(16);
        int veinCenterZ = chunk.getPos().getMinBlockZ() + random.nextInt(16);
        int veinCenterY = 20 + random.nextInt(40); // Veins between y=20 and y=60

        // Vein size parameters
        int maxVeinSize = 8 + random.nextInt(12); // 8-20 blocks per vein
        float veinDensity = 0.3f + random.nextFloat() * 0.4f; // 30-70% density

        // Debug log for vein generation
        Geco.LOGGER.debug("GecoChunkGenerator: Generating {} vein at ({}, {}, {}) with size {}", marbleBlockId, veinCenterX, veinCenterY, veinCenterZ, maxVeinSize);

        try {
            BlockState marbleState = net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(marbleBlockId)
            ).defaultBlockState();

            // Generate vein blocks in a 3D sphere-like pattern around the center
            for (int x = -maxVeinSize; x <= maxVeinSize; x++) {
                for (int y = -maxVeinSize; y <= maxVeinSize; y++) {
                    for (int z = -maxVeinSize; z <= maxVeinSize; z++) {
                        // Calculate distance from vein center
                        double distance = Math.sqrt(x*x + y*y + z*z);

                        // Only place blocks within vein radius and with density chance
                        if (distance <= maxVeinSize && random.nextFloat() < veinDensity) {
                            BlockPos pos = new BlockPos(veinCenterX + x, veinCenterY + y, veinCenterZ + z);

                            // Check if position is within chunk bounds and is stone
                            if (level.getBounds().isInside(pos)) {
                                BlockState currentState = level.getBlockState(pos);
                                if (currentState.is(Blocks.STONE)) {
                                    level.setBlock(pos, marbleState, 2);
                                    replacements++;
                                }
                            }
                        }
                    }
                }
            }

            // Debug log for vein completion
            Geco.LOGGER.debug("GecoChunkGenerator: Generated {} blocks for {} vein", replacements, marbleBlockId);

        } catch (Exception e) {
            Geco.LOGGER.warn("GecoChunkGenerator: Failed to get marble block {}: {}", marbleBlockId, e.getMessage());
        }

        return replacements;
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }

    @Override
    public int getMinY() {
        return settings.value().noiseSettings().minY();
    }

    @Override
    public int getGenDepth() {
        return settings.value().noiseSettings().height();
    }

    @Override
    public int getBaseHeight(int x, int z, net.minecraft.world.level.levelgen.Heightmap.Types type, net.minecraft.world.level.LevelHeightAccessor level, RandomState state) {
        return getMinY() + 64; // Simple implementation
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, net.minecraft.world.level.LevelHeightAccessor level, RandomState state) {
        return new NoiseColumn(getMinY(), new BlockState[0]);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, net.minecraft.world.level.StructureManager structureManager, ChunkAccess chunk) {
        // Basic implementation - just return the chunk as-is for now
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public void createStructures(net.minecraft.core.RegistryAccess registryAccess, ChunkGeneratorStructureState structureState, net.minecraft.world.level.StructureManager structureManager, ChunkAccess chunk, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager templateManager) {
        // Basic implementation
    }

    @Override
    public void applyCarvers(WorldGenRegion level, long seed, RandomState state, BiomeManager biomeManager, StructureManager structureFeatureManager, ChunkAccess chunk, GenerationStep.Carving step) {
        // Basic implementation - can be expanded later
    }

    @Override
    @SuppressWarnings("deprecation")
    public void spawnOriginalMobs(WorldGenRegion level) {
        // Basic implementation - can be expanded later
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState state, BlockPos pos) {
        // Empty implementation
    }
}