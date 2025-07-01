package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GecoDataGenerator {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Path OUTPUT_DIR = Paths.get("generated", "resources");

    public record WoodType(String name) {
        public String capitalized() {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Geco data generation...");
        
        try {
            // Create output directories
            Files.createDirectories(OUTPUT_DIR);
            
            // Define wood types (currently only Ebony)
            List<WoodType> woodTypes = List.of(new WoodType("ebony"));
            
            // Generate files for each wood type
            GecoBlockstateGenerator blockstateGenerator = new GecoBlockstateGenerator(OUTPUT_DIR, GSON);
            GecoModelGenerator modelGenerator = new GecoModelGenerator(OUTPUT_DIR, GSON);
            GecoRecipeGenerator recipeGenerator = new GecoRecipeGenerator(OUTPUT_DIR, GSON);
            GecoLootTableGenerator lootTableGenerator = new GecoLootTableGenerator(OUTPUT_DIR, GSON);
            GecoTagGenerator tagGenerator = new GecoTagGenerator(OUTPUT_DIR, GSON);

            // Generate files for each wood type
            for (WoodType wood : woodTypes) {
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
            String capitalized = wood.capitalized();
            langEntries.put("block.geco." + wood.name() + "_log", capitalized + " Log");
            langEntries.put("block.geco." + wood.name() + "_planks", capitalized + " Planks");
            langEntries.put("block.geco." + wood.name() + "_wood", capitalized + " Wood");
            langEntries.put("block.geco.stripped_" + wood.name() + "_log", "Stripped " + capitalized + " Log");
            langEntries.put("block.geco.stripped_" + wood.name() + "_wood", "Stripped " + capitalized + " Wood");
            langEntries.put("block.geco." + wood.name() + "_leaves", capitalized + " Leaves");
            langEntries.put("block.geco." + wood.name() + "_sapling", capitalized + " Sapling");
            langEntries.put("block.geco." + wood.name() + "_slab", capitalized + " Slab");
            langEntries.put("block.geco." + wood.name() + "_stairs", capitalized + " Stairs");
            langEntries.put("block.geco." + wood.name() + "_fence", capitalized + " Fence");
            langEntries.put("block.geco." + wood.name() + "_fence_gate", capitalized + " Fence Gate");
            langEntries.put("block.geco." + wood.name() + "_button", capitalized + " Button");
            langEntries.put("block.geco." + wood.name() + "_pressure_plate", capitalized + " Pressure Plate");
            langEntries.put("block.geco." + wood.name() + "_door", capitalized + " Door");
            langEntries.put("block.geco." + wood.name() + "_trapdoor", capitalized + " Trapdoor");
        }
        
        writeJsonFile(OUTPUT_DIR.resolve("assets/geco/lang/en_us.json"), langEntries);
    }

    private static void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, GSON.toJson(data));
    }
}