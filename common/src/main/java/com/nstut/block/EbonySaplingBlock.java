package com.nstut.block;

import com.nstut.world.tree.EbonyTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class EbonySaplingBlock extends SaplingBlock {
    public EbonySaplingBlock() {
        super(
                EbonyTreeGrower.EBONY,
                Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.GRASS)
        );
    }
}
