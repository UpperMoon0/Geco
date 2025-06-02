package com.nstut.neoforge;

import com.nstut.neoforge.world.ModBiomeModifications;
import com.nstut.registry.ModBlockEntities;
import com.nstut.registry.ModBlocks;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber; // Added import
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
// import net.neoforged.neoforge.client.event.ModelEvent; // ModelEvent does not have RegisterLayerDefinitions

import com.nstut.Geco;

@Mod(Geco.MOD_ID)
public final class GekoNeoForge {
    public GekoNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Geco.init();
        
        // Register biome modifications for NeoForge
        ModBiomeModifications.register(modEventBus);
    }

    @EventBusSubscriber(modid = Geco.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        public static final ModelLayerLocation EBONY_SIGN_MODEL_LAYER =
                new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, "sign/ebony"), "main");

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(EBONY_SIGN_MODEL_LAYER, SignRenderer::createSignLayer);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            // Comment out the renderer registration temporarily until we resolve the rendering model issue
            // The sign will still function as a block entity but won't have custom rendering
            // event.registerBlockEntityRenderer(ModBlockEntities.EBONY_SIGN_BLOCK_ENTITY.get(), SignRenderer::new);
        }
    }
}
