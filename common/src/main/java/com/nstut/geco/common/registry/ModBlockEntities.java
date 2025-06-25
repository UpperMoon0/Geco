package com.nstut.geco.common.registry;

import com.nstut.geco.common.Geco;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Geco.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static void init() {
        BLOCK_ENTITIES.register();
    }
}