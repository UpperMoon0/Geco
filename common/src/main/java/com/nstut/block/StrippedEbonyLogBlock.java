package com.nstut.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class StrippedEbonyLogBlock extends RotatedPillarBlock {
    public StrippedEbonyLogBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLACK) // Ebony is dark, stripped might be slightly lighter or different hue
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
    }
}
