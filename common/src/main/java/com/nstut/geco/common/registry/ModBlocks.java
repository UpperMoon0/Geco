package com.nstut.geco.common.registry;

import com.nstut.geco.common.block.*;
import com.nstut.geco.common.wood.WoodType;
import com.nstut.geco.common.stone.StoneType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.SoundType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    
    // Registry helper that will be set by the platform-specific code
    public static BlockRegistryHelper REGISTRY_HELPER;
    
    // Maps to store dynamically registered blocks
    private static final Map<WoodType, WoodBlockSet> WOOD_BLOCK_SETS = new HashMap<>();
    private static final Map<StoneType, StoneBlockSet> STONE_BLOCK_SETS = new HashMap<>();
    
    /**
     * Container class to hold all blocks for a wood type
     */
    public static class WoodBlockSet {
        public final Supplier<Block> log;
        public final Supplier<Block> strippedLog;
        public final Supplier<Block> wood;
        public final Supplier<Block> strippedWood;
        public final Supplier<Block> planks;
        public final Supplier<Block> stairs;
        public final Supplier<Block> slab;
        public final Supplier<Block> fence;
        public final Supplier<Block> fenceGate;
        public final Supplier<Block> door;
        public final Supplier<Block> trapdoor;
        public final Supplier<Block> pressurePlate;
        public final Supplier<Block> button;
        public final Supplier<Block> leaves;
        public final Supplier<Block> sapling;
        
        public WoodBlockSet(Supplier<Block> log, Supplier<Block> strippedLog, Supplier<Block> wood,
                           Supplier<Block> strippedWood, Supplier<Block> planks, Supplier<Block> stairs,
                           Supplier<Block> slab, Supplier<Block> fence, Supplier<Block> fenceGate,
                           Supplier<Block> door, Supplier<Block> trapdoor, Supplier<Block> pressurePlate,
                           Supplier<Block> button, Supplier<Block> leaves, Supplier<Block> sapling) {
            this.log = log;
            this.strippedLog = strippedLog;
            this.wood = wood;
            this.strippedWood = strippedWood;
            this.planks = planks;
            this.stairs = stairs;
            this.slab = slab;
            this.fence = fence;
            this.fenceGate = fenceGate;
            this.door = door;
            this.trapdoor = trapdoor;
            this.pressurePlate = pressurePlate;
            this.button = button;
            this.leaves = leaves;
            this.sapling = sapling;
        }
    }
    
    /**
     * Container class to hold all blocks for a stone type
     */
    public static class StoneBlockSet {
        // Default variant
        public final Supplier<Block> base;
        public final Supplier<Block> baseSlab;
        public final Supplier<Block> baseStairs;
        public final Supplier<Block> baseWall;
        
        // Polished variant
        public final Supplier<Block> polished;
        public final Supplier<Block> polishedSlab;
        public final Supplier<Block> polishedStairs;
        public final Supplier<Block> polishedWall;
        
        // Polished bricks variant
        public final Supplier<Block> polishedBricks;
        public final Supplier<Block> polishedBricksSlab;
        public final Supplier<Block> polishedBricksStairs;
        public final Supplier<Block> polishedBricksWall;
        
        // Polished tiles variant
        public final Supplier<Block> polishedTiles;
        public final Supplier<Block> polishedTilesSlab;
        public final Supplier<Block> polishedTilesStairs;
        public final Supplier<Block> polishedTilesWall;
        
        // Smooth variant
        public final Supplier<Block> smooth;
        public final Supplier<Block> smoothSlab;
        public final Supplier<Block> smoothStairs;
        public final Supplier<Block> smoothWall;
        
        public StoneBlockSet(
                Supplier<Block> base, Supplier<Block> baseSlab, Supplier<Block> baseStairs, Supplier<Block> baseWall,
                Supplier<Block> polished, Supplier<Block> polishedSlab, Supplier<Block> polishedStairs, Supplier<Block> polishedWall,
                Supplier<Block> polishedBricks, Supplier<Block> polishedBricksSlab, Supplier<Block> polishedBricksStairs, Supplier<Block> polishedBricksWall,
                Supplier<Block> polishedTiles, Supplier<Block> polishedTilesSlab, Supplier<Block> polishedTilesStairs, Supplier<Block> polishedTilesWall,
                Supplier<Block> smooth, Supplier<Block> smoothSlab, Supplier<Block> smoothStairs, Supplier<Block> smoothWall) {
            this.base = base;
            this.baseSlab = baseSlab;
            this.baseStairs = baseStairs;
            this.baseWall = baseWall;
            this.polished = polished;
            this.polishedSlab = polishedSlab;
            this.polishedStairs = polishedStairs;
            this.polishedWall = polishedWall;
            this.polishedBricks = polishedBricks;
            this.polishedBricksSlab = polishedBricksSlab;
            this.polishedBricksStairs = polishedBricksStairs;
            this.polishedBricksWall = polishedBricksWall;
            this.polishedTiles = polishedTiles;
            this.polishedTilesSlab = polishedTilesSlab;
            this.polishedTilesStairs = polishedTilesStairs;
            this.polishedTilesWall = polishedTilesWall;
            this.smooth = smooth;
            this.smoothSlab = smoothSlab;
            this.smoothStairs = smoothStairs;
            this.smoothWall = smoothWall;
        }
    }
    
    /**
     * Gets the WoodBlockSet for a specific wood type.
     *
     * @param woodType The wood type to get blocks for
     * @return The WoodBlockSet containing all blocks for this wood type
     */
    public static WoodBlockSet getWoodBlockSet(WoodType woodType) {
        return WOOD_BLOCK_SETS.get(woodType);
    }
    
    /**
     * Gets the StoneBlockSet for a specific stone type.
     *
     * @param stoneType The stone type to get blocks for
     * @return The StoneBlockSet containing all blocks for this stone type
     */
    public static StoneBlockSet getStoneBlockSet(StoneType stoneType) {
        return STONE_BLOCK_SETS.get(stoneType);
    }
    
    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("BlockRegistryHelper not set! Platform-specific code must set this before calling init()");
        }
        
        // Dynamically register blocks for all wood types
        for (WoodType woodType : ModWoodTypes.REGISTERED_WOOD_TYPES) {
            registerWoodBlockSet(woodType);
        }
        
        // Dynamically register blocks for all stone types
        for (StoneType stoneType : ModStoneTypes.REGISTERED_STONE_TYPES) {
            registerStoneBlockSet(stoneType);
        }
    }
    
    /**
     * Registers all blocks for a specific wood type.
     *
     * @param woodType The wood type to register blocks for
     */
    private static void registerWoodBlockSet(WoodType woodType) {
        String woodName = woodType.getPath();
        
        // Register log blocks
        Supplier<Block> log = REGISTRY_HELPER.registerBlock(woodName + "_log",
            () -> new RotatedPillarBlock(getLogProperties()));
        
        Supplier<Block> strippedLog = REGISTRY_HELPER.registerBlock("stripped_" + woodName + "_log",
            () -> new RotatedPillarBlock(getLogProperties()));
        
        Supplier<Block> wood = REGISTRY_HELPER.registerBlock(woodName + "_wood",
            () -> new RotatedPillarBlock(getLogProperties()));
        
        Supplier<Block> strippedWood = REGISTRY_HELPER.registerBlock("stripped_" + woodName + "_wood",
            () -> new RotatedPillarBlock(getLogProperties()));
        
        // Register plank and derived blocks
        Supplier<Block> planks = REGISTRY_HELPER.registerBlock(woodName + "_planks",
            () -> new Block(getPlankProperties()));
        
        Supplier<Block> stairs = REGISTRY_HELPER.registerBlock(woodName + "_stairs",
            () -> new StairBlock(planks.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(planks.get())));
        
        Supplier<Block> slab = REGISTRY_HELPER.registerBlock(woodName + "_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(planks.get())));
        
        Supplier<Block> fence = REGISTRY_HELPER.registerBlock(woodName + "_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(planks.get())));
        
        Supplier<Block> fenceGate = REGISTRY_HELPER.registerBlock(woodName + "_fence_gate",
            () -> new FenceGateBlock(getMinecraftWoodType(woodType), BlockBehaviour.Properties.ofFullCopy(planks.get())));
        
        // Blocks that need BlockSetType
        Supplier<Block> door = REGISTRY_HELPER.registerBlock(woodName + "_door",
            () -> new DoorBlock(ModBlockSetTypes.getBlockSetType(woodType),
                BlockBehaviour.Properties.ofFullCopy(planks.get())
                    .noOcclusion().pushReaction(PushReaction.DESTROY)));
        
        Supplier<Block> trapdoor = REGISTRY_HELPER.registerBlock(woodName + "_trapdoor",
            () -> new TrapDoorBlock(ModBlockSetTypes.getBlockSetType(woodType),
                BlockBehaviour.Properties.ofFullCopy(planks.get()).noOcclusion()));
        
        Supplier<Block> pressurePlate = REGISTRY_HELPER.registerBlock(woodName + "_pressure_plate",
            () -> new PressurePlateBlock(ModBlockSetTypes.getBlockSetType(woodType),
                BlockBehaviour.Properties.ofFullCopy(planks.get())
                    .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
        
        Supplier<Block> button = REGISTRY_HELPER.registerBlock(woodName + "_button",
            () -> new ButtonBlock(ModBlockSetTypes.getBlockSetType(woodType), 30,
                BlockBehaviour.Properties.ofFullCopy(planks.get())
                    .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
        
        // Natural blocks - these might need specific implementations per wood type
        Supplier<Block> leaves = REGISTRY_HELPER.registerBlock(woodName + "_leaves",
            () -> createLeavesBlock(woodType));
        
        Supplier<Block> sapling = REGISTRY_HELPER.registerBlock(woodName + "_sapling",
            () -> createSaplingBlock(woodType));
        
        // Store the complete set
        WoodBlockSet blockSet = new WoodBlockSet(log, strippedLog, wood, strippedWood, planks,
                stairs, slab, fence, fenceGate, door, trapdoor, pressurePlate, button, leaves, sapling);
        
        WOOD_BLOCK_SETS.put(woodType, blockSet);
    }
    
    /**
     * Registers all blocks for a specific stone type.
     *
     * @param stoneType The stone type to register blocks for
     */
    private static void registerStoneBlockSet(StoneType stoneType) {
        String stoneName = stoneType.getPath();
        
        // Register base variant (no prefix)
        Supplier<Block> base = REGISTRY_HELPER.registerBlock(stoneName,
            () -> new Block(getStoneProperties()));
        Supplier<Block> baseSlab = REGISTRY_HELPER.registerBlock(stoneName + "_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(base.get())));
        Supplier<Block> baseStairs = REGISTRY_HELPER.registerBlock(stoneName + "_stairs",
            () -> new StairBlock(base.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(base.get())));
        Supplier<Block> baseWall = REGISTRY_HELPER.registerBlock(stoneName + "_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(base.get())));
        
        // Register polished variant
        Supplier<Block> polished = REGISTRY_HELPER.registerBlock("polished_" + stoneName,
            () -> new Block(getStoneProperties()));
        Supplier<Block> polishedSlab = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(polished.get())));
        Supplier<Block> polishedStairs = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_stairs",
            () -> new StairBlock(polished.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(polished.get())));
        Supplier<Block> polishedWall = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(polished.get())));
        
        // Register polished bricks variant
        Supplier<Block> polishedBricks = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_bricks",
            () -> new Block(getStoneProperties()));
        Supplier<Block> polishedBricksSlab = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(polishedBricks.get())));
        Supplier<Block> polishedBricksStairs = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_bricks_stairs",
            () -> new StairBlock(polishedBricks.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(polishedBricks.get())));
        Supplier<Block> polishedBricksWall = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(polishedBricks.get())));
        
        // Register polished tiles variant
        Supplier<Block> polishedTiles = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_tiles",
            () -> new Block(getStoneProperties()));
        Supplier<Block> polishedTilesSlab = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_tiles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(polishedTiles.get())));
        Supplier<Block> polishedTilesStairs = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_tiles_stairs",
            () -> new StairBlock(polishedTiles.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(polishedTiles.get())));
        Supplier<Block> polishedTilesWall = REGISTRY_HELPER.registerBlock("polished_" + stoneName + "_tiles_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(polishedTiles.get())));
        
        // Register smooth variant
        Supplier<Block> smooth = REGISTRY_HELPER.registerBlock("smooth_" + stoneName,
            () -> new Block(getStoneProperties()));
        Supplier<Block> smoothSlab = REGISTRY_HELPER.registerBlock("smooth_" + stoneName + "_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(smooth.get())));
        Supplier<Block> smoothStairs = REGISTRY_HELPER.registerBlock("smooth_" + stoneName + "_stairs",
            () -> new StairBlock(smooth.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(smooth.get())));
        Supplier<Block> smoothWall = REGISTRY_HELPER.registerBlock("smooth_" + stoneName + "_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(smooth.get())));
        
        // Store the complete set
        StoneBlockSet blockSet = new StoneBlockSet(
                base, baseSlab, baseStairs, baseWall,
                polished, polishedSlab, polishedStairs, polishedWall,
                polishedBricks, polishedBricksSlab, polishedBricksStairs, polishedBricksWall,
                polishedTiles, polishedTilesSlab, polishedTilesStairs, polishedTilesWall,
                smooth, smoothSlab, smoothStairs, smoothWall);
        
        STONE_BLOCK_SETS.put(stoneType, blockSet);
    }
    
    /**
     * Creates standard log block properties.
     */
    private static BlockBehaviour.Properties getLogProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava();
    }
    
    /**
     * Creates standard plank block properties.
     */
    private static BlockBehaviour.Properties getPlankProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava();
    }
    
    /**
     * Creates standard stone block properties.
     */
    private static BlockBehaviour.Properties getStoneProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .sound(SoundType.STONE);
    }
    
    /**
     * Creates a leaves block for the given wood type.
     * Uses standard LeavesBlock with appropriate properties.
     */
    private static Block createLeavesBlock(WoodType woodType) {
        return new LeavesBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn((state, level, pos, type) -> false)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((state, level, pos) -> false));
    }
    
    /**
     * Creates a sapling block for the given wood type.
     * Uses GecoSaplingBlock which can handle different wood types.
     */
    private static Block createSaplingBlock(WoodType woodType) {
        return new GecoSaplingBlock(woodType);
    }
    
    /**
     * Map to store Minecraft WoodType instances for each of our WoodTypes
     */
    private static final Map<WoodType, net.minecraft.world.level.block.state.properties.WoodType> MINECRAFT_WOOD_TYPES = new HashMap<>();
    
    /**
     * Converts our custom WoodType to Minecraft's WoodType for fence gates.
     * Creates a new Minecraft WoodType if needed.
     */
    private static net.minecraft.world.level.block.state.properties.WoodType getMinecraftWoodType(WoodType woodType) {
        return MINECRAFT_WOOD_TYPES.computeIfAbsent(woodType, wt ->
            new net.minecraft.world.level.block.state.properties.WoodType(
                wt.getName().toString(),
                ModBlockSetTypes.getBlockSetType(wt)
            )
        );
    }
    
    // Interface that platform-specific code must implement
    public interface BlockRegistryHelper {
        <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block);
    }
}