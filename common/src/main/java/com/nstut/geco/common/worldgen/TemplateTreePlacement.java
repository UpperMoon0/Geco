package com.nstut.geco.common.worldgen;

import com.nstut.geco.common.Geco;
import com.nstut.geco.common.wood.WoodType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/** The occupied template cells are pasted exactly, without procedural foliage or random processors. */
public final class TemplateTreePlacement {
    private TemplateTreePlacement() {}
    public static List<ResourceLocation> templates(WoodType wood) {
        return java.util.stream.IntStream.rangeClosed(1, 4)
                .filter(number -> !wood.isModelBlacklisted(number))
                .mapToObj(number -> Geco.id(wood.getPath() + "_tree_m" + number)).toList();
    }
    public static boolean place(WorldGenLevel level, BlockPos pos, RandomSource random,
                                WoodType wood, BlockState sapling) {
        List<ResourceLocation> choices = templates(wood);
        if (choices.isEmpty()) return false;
        var template = level.getLevel().getStructureManager().get(choices.get(random.nextInt(choices.size())));
        return template.isPresent() && placeTemplate(level, pos, template.get(), sapling);
    }
    public static boolean placeTemplate(WorldGenLevel level, BlockPos pos, StructureTemplate template, BlockState sapling) {
        if (!available(level, pos) || !available(level, pos.below())) return false;
        BlockState soil = level.getBlockState(pos.below());
        if (soil.hasBlockEntity() || !(soil.is(BlockTags.DIRT) || soil.is(Blocks.FARMLAND))) return false;
        // StructureTemplate.save/load's public NBT format avoids private-field reflection or access wideners.
        CompoundTag data = template.save(new CompoundTag());
        if (data.contains("palettes") || !data.getList("entities", Tag.TAG_COMPOUND).isEmpty()) return false;
        ListTag paletteTags = data.getList("palette", Tag.TAG_COMPOUND);
        List<BlockState> palette = new ArrayList<>();
        for (int i = 0; i < paletteTags.size(); i++)
            palette.add(NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), paletteTags.getCompound(i)));
        ListTag blocks = data.getList("blocks", Tag.TAG_COMPOUND);
        if (palette.isEmpty() || blocks.isEmpty()) return false;
        // Anchor the lowest log to the sapling, rather than assuming the bounding box is centered.
        BlockPos anchor = null;
        for (int i = 0; i < blocks.size(); i++) {
            CompoundTag block = blocks.getCompound(i);
            int state = block.getInt("state");
            if (state < 0 || state >= palette.size()) return false;
            BlockPos local = position(block);
            if (local == null) return false;
            if (palette.get(state).is(BlockTags.LOGS) && (anchor == null || local.getY() < anchor.getY()))
                anchor = local;
        }
        if (anchor == null) return false;
        BlockPos origin = pos.subtract(anchor);
        List<PlacementTransaction.Change<BlockPos, BlockState>> changes = new ArrayList<>();
        for (int i = 0; i < blocks.size(); i++) {
            CompoundTag block = blocks.getCompound(i);
            BlockState target = palette.get(block.getInt("state"));
            if (target.isAir() || target.is(Blocks.STRUCTURE_VOID)) continue;
            if (target.hasBlockEntity() || block.contains("nbt")) return false;
            BlockPos destination = origin.offset(position(block));
            if (!available(level, destination)) return false;
            BlockState previous = level.getBlockState(destination);
            if (previous.hasBlockEntity() || !previous.getFluidState().isEmpty()) return false;
            boolean originalSapling = destination.equals(pos) && sapling != null && previous.equals(sapling);
            if (!originalSapling && !(previous.isAir() || previous.canBeReplaced() || previous.is(BlockTags.LEAVES)))
                return false;
            changes.add(new PlacementTransaction.Change<>(destination, previous, target));
        }
        if (changes.isEmpty()) return false;
        boolean placed = PlacementTransaction.apply(new PlacementTransaction.World<BlockPos, BlockState>() {
            public BlockState read(BlockPos position) { return level.getBlockState(position); }
            public boolean write(BlockPos position, BlockState state) {
                return level.setBlock(position, state, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            }
        }, changes);
        if (placed) {
            for (var change : changes) {
                change.after().updateNeighbourShapes(level, change.position(), Block.UPDATE_ALL);
                level.blockUpdated(change.position(), change.after().getBlock());
            }
        }
        return placed;
    }
    private static BlockPos position(CompoundTag block) {
        ListTag coordinates = block.getList("pos", Tag.TAG_INT);
        return coordinates.size() == 3 ? new BlockPos(coordinates.getInt(0), coordinates.getInt(1), coordinates.getInt(2)) : null;
    }
    private static boolean available(WorldGenLevel level, BlockPos pos) {
        return !level.isOutsideBuildHeight(pos) && level.ensureCanWrite(pos) && level.hasChunk(pos.getX() >> 4, pos.getZ() >> 4);
    }
}
