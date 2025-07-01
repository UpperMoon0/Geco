package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nstut.geco.common.registry.ModWoodTypes;
import com.nstut.geco.common.wood.WoodType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GecoDataGenerator {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Path OUTPUT_DIR = Paths.get("src", "generated", "resources");


    public static void main(String[] args) {
        System.out.println("Starting Geco data generation...");
        
        try {
            // Initialize ModWoodTypes to register all wood types
            ModWoodTypes.init();
            
            // Create output directories
            Files.createDirectories(OUTPUT_DIR);
            
            // Get all registered wood types dynamically
            List<WoodType> woodTypes = ModWoodTypes.REGISTERED_WOOD_TYPES;
            System.out.println("Generating data for " + woodTypes.size() + " wood types: " +
                woodTypes.stream().map(w -> w.getPath()).toList());
            
            // Generate files for each wood type
            GecoBlockstateGenerator blockstateGenerator = new GecoBlockstateGenerator(OUTPUT_DIR, GSON);
            GecoModelGenerator modelGenerator = new GecoModelGenerator(OUTPUT_DIR, GSON);
            GecoRecipeGenerator recipeGenerator = new GecoRecipeGenerator(OUTPUT_DIR, GSON);
            GecoLootTableGenerator lootTableGenerator = new GecoLootTableGenerator(OUTPUT_DIR, GSON);
            GecoTagGenerator tagGenerator = new GecoTagGenerator(OUTPUT_DIR, GSON);

            // Generate files for each wood type
            for (WoodType wood : woodTypes) {
                System.out.println("Generating data for wood type: " + wood.getPath());
                blockstateGenerator.generateBlockstateFiles(wood);
                modelGenerator.generateBlockModelFiles(wood);
                modelGenerator.generateItemModelFiles(wood);
                recipeGenerator.generateRecipeFiles(wood);
                lootTableGenerator.generateLootTableFiles(wood);
                tagGenerator.generateTagFiles(wood);
                tagGenerator.generateMinecraftTagFiles(wood);
            }
            
            // Generate language file
            generateLanguageFile(woodTypes);
            
            System.out.println("Data generation completed successfully!");
        } catch (IOException e) {
            System.err.println("Error during data generation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void generateLanguageFile(List<WoodType> woodTypes) throws IOException {
        Map<String, String> langEntries = new HashMap<>();
        
        // Add common entries
        langEntries.put("itemGroup.geco.geco_tab", "Geco Mod");
        
        // Add wood-specific entries
        for (WoodType wood : woodTypes) {
            String woodName = wood.getPath();
            String capitalized = woodName.substring(0, 1).toUpperCase() + woodName.substring(1);
            langEntries.put("block.geco." + woodName + "_log", capitalized + " Log");
            langEntries.put("block.geco." + woodName + "_planks", capitalized + " Planks");
            langEntries.put("block.geco." + woodName + "_wood", capitalized + " Wood");
            langEntries.put("block.geco.stripped_" + woodName + "_log", "Stripped " + capitalized + " Log");
            langEntries.put("block.geco.stripped_" + woodName + "_wood", "Stripped " + capitalized + " Wood");
            langEntries.put("block.geco." + woodName + "_leaves", capitalized + " Leaves");
            langEntries.put("block.geco." + woodName + "_sapling", capitalized + " Sapling");
            langEntries.put("block.geco." + woodName + "_slab", capitalized + " Slab");
            langEntries.put("block.geco." + woodName + "_stairs", capitalized + " Stairs");
            langEntries.put("block.geco." + woodName + "_fence", capitalized + " Fence");
            langEntries.put("block.geco." + woodName + "_fence_gate", capitalized + " Fence Gate");
            langEntries.put("block.geco." + woodName + "_button", capitalized + " Button");
            langEntries.put("block.geco." + woodName + "_pressure_plate", capitalized + " Pressure Plate");
            langEntries.put("block.geco." + woodName + "_door", capitalized + " Door");
            langEntries.put("block.geco." + woodName + "_trapdoor", capitalized + " Trapdoor");
        }
        
        writeJsonFile(OUTPUT_DIR.resolve("assets/geco/lang/en_us.json"), langEntries);
    }

    private static void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, GSON.toJson(data));
    }
}