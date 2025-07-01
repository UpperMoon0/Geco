package com.nstut.fabric.client;

import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModWoodTypes;
import com.nstut.geco.common.wood.WoodType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class GecoFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register blocks with appropriate render layers for transparency
        for (WoodType woodType : ModWoodTypes.REGISTERED_WOOD_TYPES) {
            ModBlocks.WoodBlockSet blockSet = ModBlocks.getWoodBlockSet(woodType);
            if (blockSet != null) {
                BlockRenderLayerMap.INSTANCE.putBlock(blockSet.sapling.get(), RenderType.cutout());
                BlockRenderLayerMap.INSTANCE.putBlock(blockSet.leaves.get(), RenderType.cutoutMipped());
                BlockRenderLayerMap.INSTANCE.putBlock(blockSet.door.get(), RenderType.cutout());
                BlockRenderLayerMap.INSTANCE.putBlock(blockSet.trapdoor.get(), RenderType.cutout());
            }
        }
    }
}