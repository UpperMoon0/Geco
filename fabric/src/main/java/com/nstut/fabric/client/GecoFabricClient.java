package com.nstut.fabric.client;

import com.nstut.geco.common.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class GecoFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register blocks with appropriate render layers for transparency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_SAPLING.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_LEAVES.get(), RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_DOOR.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_TRAPDOOR.get(), RenderType.cutout());
    }
}