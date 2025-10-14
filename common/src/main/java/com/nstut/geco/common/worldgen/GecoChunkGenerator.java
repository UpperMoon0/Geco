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
        // Generate marble formations more realistically - large continuous bodies rather than small veins
        RandomSource random = RandomSource.create(chunk.getPos().x * 341873128712L + chunk.getPos().z * 132897987541L);
        int replacements = 0;

        // Generate multicolor marble formations (larger, more continuous)
        replacements += generateMarbleFormation(level, chunk, random, "geco:multicolor_marble", 0.015f); // 1.5% chance per chunk

        // Generate cream marble formations (larger, more continuous)
        replacements += generateMarbleFormation(level, chunk, random, "geco:cream_marble", 0.015f); // 1.5% chance per chunk
    }

    private int generateMarbleFormation(WorldGenRegion level, ChunkAccess chunk, RandomSource random, String marbleBlockId, float formationChance) {
        int replacements = 0;

        // Check if we should generate a formation in this chunk
        if (random.nextFloat() >= formationChance) {
            return 0; // No formation in this chunk
        }

        // Generate formation center position (somewhere in this chunk or adjacent)
        int formationCenterX = chunk.getPos().getMinBlockX() + random.nextInt(16);
        int formationCenterZ = chunk.getPos().getMinBlockZ() + random.nextInt(16);
        int formationCenterY = 15 + random.nextInt(35); // Formations between y=15 and y=50

        // Formation size parameters - much larger for realistic marble deposits
        int maxFormationSize = 15 + random.nextInt(25); // 15-40 blocks per formation (larger than veins)
        float formationDensity = 0.4f + random.nextFloat() * 0.3f; // 40-70% density (more continuous)

        // Debug log for formation generation
        Geco.LOGGER.debug("GecoChunkGenerator: Generating {} formation at ({}, {}, {}) with size {}", marbleBlockId, formationCenterX, formationCenterY, formationCenterZ, maxFormationSize);

        try {
            // Split the marbleBlockId into namespace and path
            String[] parts = marbleBlockId.split(":");
            String namespace = parts.length > 1 ? parts[0] : "geco";
            String path = parts.length > 1 ? parts[1] : marbleBlockId;

            BlockState marbleState = net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(namespace, path)
            ).defaultBlockState();

            // Generate formation blocks in a more continuous, layered pattern (like real marble)
            // Use multiple layers with varying thickness for more realistic appearance
            int layers = 3 + random.nextInt(4); // 3-6 layers

            for (int layer = 0; layer < layers; layer++) {
                // Each layer has its own center slightly offset from the main center
                int layerOffsetX = random.nextInt(7) - 3; // -3 to +3 offset
                int layerOffsetZ = random.nextInt(7) - 3;
                int layerOffsetY = (layer - layers/2) * (2 + random.nextInt(3)); // Vertical stacking

                int layerCenterX = formationCenterX + layerOffsetX;
                int layerCenterY = formationCenterY + layerOffsetY;
                int layerCenterZ = formationCenterZ + layerOffsetZ;

                // Each layer has slightly different size
                int layerSize = maxFormationSize - random.nextInt(5);

                // Generate blocks in this layer with a flatter, more sheet-like distribution
                for (int x = -layerSize; x <= layerSize; x++) {
                    for (int y = -layerSize/3; y <= layerSize/3; y++) { // Thinner vertically for sheet-like appearance
                        for (int z = -layerSize; z <= layerSize; z++) {
                            // Calculate distance from layer center (more elliptical for sheet-like formations)
                            double distance = Math.sqrt(x*x + (y*3)* (y*3) + z*z); // y distance weighted more for flatter shape

                            // Only place blocks within formation radius and with density chance
                            if (distance <= layerSize && random.nextFloat() < formationDensity) {
                                BlockPos pos = new BlockPos(layerCenterX + x, layerCenterY + y, layerCenterZ + z);

                                // Check if position is within chunk bounds and is stone
                                if (chunk.getPos().getMinBlockX() <= pos.getX() && pos.getX() < chunk.getPos().getMaxBlockX() &&
                                    chunk.getPos().getMinBlockZ() <= pos.getZ() && pos.getZ() < chunk.getPos().getMaxBlockZ() &&
                                    pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight()) {
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
            }

            // Debug log for formation completion
            Geco.LOGGER.debug("GecoChunkGenerator: Generated {} blocks for {} formation", replacements, marbleBlockId);

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