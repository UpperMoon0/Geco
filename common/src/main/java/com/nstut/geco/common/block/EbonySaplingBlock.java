package com.nstut.geco.common.block;

import com.nstut.geco.common.world.EbonyTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class EbonySaplingBlock extends SaplingBlock {
    public EbonySaplingBlock() {
        super(EbonyTreeGrower.EBONY_TREE_GROWER, BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
    }
}