package com.nstut.neoforge.client;

import com.nstut.geco.common.util.BoatTypeHelper;
import com.nstut.geco.common.registry.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "geco", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GecoNeoForgeClient {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Register blocks with appropriate render layers for transparency
        event.enqueueWork(() -> {
            // Register translucent blocks to correct render layers
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EBONY_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EBONY_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EBONY_DOOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EBONY_TRAPDOOR.get(), RenderType.cutout());
        });
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Register model layers for our custom ebony boat type
        if (BoatTypeHelper.getEbonyType() != null) {
            event.registerLayerDefinition(
                ModelLayers.createBoatModelName(BoatTypeHelper.getEbonyType()),
                BoatModel::createBodyModel
            );
            event.registerLayerDefinition(
                ModelLayers.createChestBoatModelName(BoatTypeHelper.getEbonyType()),
                ChestBoatModel::createBodyModel
            );
        }
    }
}