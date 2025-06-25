package com.nstut.fabric.client;

import com.nstut.geco.common.util.BoatTypeHelper;
import com.nstut.geco.common.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class GecoFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register blocks with appropriate render layers for transparency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_SAPLING.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_LEAVES.get(), RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_DOOR.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_TRAPDOOR.get(), RenderType.cutout());
        
        // Register model layers for our custom ebony boat type
        if (BoatTypeHelper.getEbonyType() != null) {
            EntityModelLayerRegistry.registerModelLayer(
                ModelLayers.createBoatModelName(BoatTypeHelper.getEbonyType()),
                BoatModel::createBodyModel
            );
            EntityModelLayerRegistry.registerModelLayer(
                ModelLayers.createChestBoatModelName(BoatTypeHelper.getEbonyType()),
                ChestBoatModel::createBodyModel
            );
        }
    }
}