package com.nstut.neoforge.datagen;

import com.nstut.Geko;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Geko.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDatagenInitializer {
      @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        System.out.println("DEBUG: ModDatagenInitializer.gatherData() called!");
        
        // Use our custom data generation system instead of NeoForge's
        System.out.println("DEBUG: Running custom data generation...");
        CustomDataGenerator.generateAllData();
        
        System.out.println("DEBUG: Custom data generation completed!");
    }
}
