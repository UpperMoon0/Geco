package com.nstut.registry;

import com.nstut.geco.common.Geco;
import com.nstut.geco.common.block.entity.EbonyHangingSignBlockEntity;
import com.nstut.geco.common.block.entity.EbonySignBlockEntity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Geco.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<EbonySignBlockEntity>> EBONY_SIGN = BLOCK_ENTITIES.register("ebony_sign",
        () -> BlockEntityType.Builder.of(EbonySignBlockEntity::new,
            ModBlocks.EBONY_SIGN.get(),
            ModBlocks.EBONY_WALL_SIGN.get()).build(null));
            
    public static final RegistrySupplier<BlockEntityType<EbonyHangingSignBlockEntity>> EBONY_HANGING_SIGN = BLOCK_ENTITIES.register("ebony_hanging_sign",
        () -> BlockEntityType.Builder.of(EbonyHangingSignBlockEntity::new,
            ModBlocks.EBONY_HANGING_SIGN.get(),
            ModBlocks.EBONY_WALL_HANGING_SIGN.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}