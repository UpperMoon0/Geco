package com.nstut.neoforge.client;

import com.nstut.geco.common.registry.ModBlocks;
import com.nstut.geco.common.registry.ModWoodTypes;
import com.nstut.geco.common.wood.WoodType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "geco", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GecoNeoForgeClient {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Register blocks with appropriate render layers for transparency
        event.enqueueWork(() -> {
            // Register translucent blocks to correct render layers
            for (WoodType woodType : ModWoodTypes.REGISTERED_WOOD_TYPES) {
                ModBlocks.WoodBlockSet blockSet = ModBlocks.getWoodBlockSet(woodType);
                if (blockSet != null) {
                    ItemBlockRenderTypes.setRenderLayer(blockSet.sapling.get(), RenderType.cutout());
                    ItemBlockRenderTypes.setRenderLayer(blockSet.leaves.get(), RenderType.cutoutMipped());
                    ItemBlockRenderTypes.setRenderLayer(blockSet.door.get(), RenderType.cutout());
                    ItemBlockRenderTypes.setRenderLayer(blockSet.trapdoor.get(), RenderType.cutout());
                }
            }
        });
    }
}