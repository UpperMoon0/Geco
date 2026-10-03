package com.nstut.geco.common.registry;

import com.nstut.geco.common.wood.WoodType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class ModBlockSetTypes {
    public static BlockSetType EBONY;
    public static BlockSetType getBlockSetType(WoodType woodType) { return BlockSetType.OAK; }
    public static void init() { EBONY = BlockSetType.OAK; }
}
