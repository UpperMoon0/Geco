package com.nstut.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;

public class EbonyLeavesBlock extends LeavesBlock {
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED; // Added for consistency

    public EbonyLeavesBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT) // Typical for leaves
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                // .requiresCorrectToolForDrops() // if you want them to drop with shears
                .ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(DISTANCE, 7)
            .setValue(PERSISTENT, false)
            .setValue(WATERLOGGED, false)); // Added for consistency
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED); // Added for consistency
    }

    // copied from vanilla LeavesBlock to ensure waterlogging works as expected
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockstate = this.defaultBlockState().setValue(PERSISTENT, true).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        return updateDistance(blockstate, context.getLevel(), context.getClickedPos());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        // Logic from LeavesBlock#updateShape
        int i = getDistanceAt(facingState) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            level.scheduleTick(currentPos, this, getDecayTickDelay(level));
        }
        return state;
    }

    private static int getDistanceAt(BlockState neighbor) {
        if (neighbor.is(net.minecraft.tags.BlockTags.LOGS)) { // Use vanilla tag for any log
            return 0;
        }
        return neighbor.getBlock() instanceof LeavesBlock ? neighbor.getValue(DISTANCE) : 7;
    }

    // Copied from vanilla LeavesBlock to ensure decay works as expected
    protected BlockState updateDistance(BlockState p_221372_, LevelAccessor p_221373_, BlockPos p_221374_) {
        int i = 7;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(p_221374_, direction);
            i = Math.min(i, getDistanceAt(p_221373_.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }
        return p_221372_.setValue(DISTANCE, i);
    }

    private int getDecayTickDelay(LevelAccessor level) {
        // Return a value between 20 and 40, similar to vanilla leaves
        return 20 + level.getRandom().nextInt(20);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            BlockState updatedState = updateDistance(state, level, pos);
            if (updatedState.getValue(DISTANCE) != state.getValue(DISTANCE)) { // if distance changed, update it
                 level.setBlock(pos, updatedState, 3);
            }
            if (!updatedState.getValue(PERSISTENT) && updatedState.getValue(DISTANCE) == 7) {
                dropResources(updatedState, level, pos);
                level.removeBlock(pos, false);
            }
    }

    // To make leaves flammable like other leaves
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }
}
