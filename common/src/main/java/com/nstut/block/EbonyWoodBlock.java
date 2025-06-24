package com.nstut.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.BlockHitResult;
import com.nstut.registry.ModBlocks;
import net.minecraft.world.entity.EquipmentSlot;

public class EbonyWoodBlock extends RotatedPillarBlock {
    public EbonyWoodBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() instanceof AxeItem && ModBlocks.STRIPPED_EBONY_WOOD.get() != null) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!level.isClientSide) {
                BlockState strippedState = ModBlocks.STRIPPED_EBONY_WOOD.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                level.setBlock(pos, strippedState, 11);
                itemStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}