package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nstut.geco.common.registry.ModWoodTypes;
import com.nstut.geco.common.registry.ModStoneTypes;
import com.nstut.geco.common.wood.WoodType;
import com.nstut.geco.common.stone.StoneType;
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
            // Initialize ModWoodTypes and ModStoneTypes to register all types
            ModWoodTypes.init();
            ModStoneTypes.init();
            
            // Create output directories
            Files.createDirectories(OUTPUT_DIR);
            
            // Get all registered types dynamically
            List<WoodType> woodTypes = ModWoodTypes.REGISTERED_WOOD_TYPES;
            List<StoneType> stoneTypes = ModStoneTypes.REGISTERED_STONE_TYPES;
            System.out.println("Generating data for " + woodTypes.size() + " wood types: " +
                woodTypes.stream().map(w -> w.getPath()).toList());
            System.out.println("Generating data for " + stoneTypes.size() + " stone types: " +
                stoneTypes.stream().map(s -> s.getPath()).toList());
            
            // Generate files for each type
            GecoBlockstateGenerator blockstateGenerator = new GecoBlockstateGenerator(OUTPUT_DIR, GSON);
            GecoModelGenerator modelGenerator = new GecoModelGenerator(OUTPUT_DIR, GSON);
            GecoRecipeGenerator recipeGenerator = new GecoRecipeGenerator(OUTPUT_DIR, GSON);
            GecoLootTableGenerator lootTableGenerator = new GecoLootTableGenerator(OUTPUT_DIR, GSON);
            GecoTagGenerator tagGenerator = new GecoTagGenerator(OUTPUT_DIR, GSON);

            // Generate files for each wood type
            for (WoodType wood : woodTypes) {
                System.out.println("Generating data for wood type: " + wood.getPath());
                blockstateGenerator.generateWoodBlockstateFiles(wood);
                modelGenerator.generateWoodBlockModelFiles(wood);
                modelGenerator.generateWoodItemModelFiles(wood);
                recipeGenerator.generateWoodRecipeFiles(wood);
                lootTableGenerator.generateWoodLootTableFiles(wood);
                tagGenerator.generateWoodTagFiles(wood);
                tagGenerator.generateMinecraftTagFiles(wood);
            }
            
            // Generate files for each stone type
            for (StoneType stone : stoneTypes) {
                System.out.println("Generating data for stone type: " + stone.getPath());
                blockstateGenerator.generateStoneBlockstateFiles(stone);
                modelGenerator.generateStoneBlockModelFiles(stone);
                modelGenerator.generateStoneItemModelFiles(stone);
                recipeGenerator.generateStoneRecipeFiles(stone);
                tagGenerator.generateStoneTagFiles(stone);
                tagGenerator.generateStoneMinecraftTagFiles(stone);
                lootTableGenerator.generateStoneLootTableFiles(stone);
            }
            
            // Generate language file
            generateLanguageFile(woodTypes, stoneTypes);
            
            System.out.println("Data generation completed successfully!");
        } catch (IOException e) {
            System.err.println("Error during data generation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void generateLanguageFile(List<WoodType> woodTypes, List<StoneType> stoneTypes) throws IOException {
        Map<String, String> langEntries = new HashMap<>();
        
        // Add common entries
        langEntries.put("itemGroup.geco.geco_tab", "Geco Mod");
        
        // Add wood-specific entries
        for (WoodType wood : woodTypes) {
            String woodName = wood.getPath();
            String capitalized = capitalizeWords(woodName.replace("_", " "));
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
        
        // Add stone-specific entries
        for (StoneType stone : stoneTypes) {
            String stoneName = stone.getPath();
            String capitalized = capitalizeWords(stoneName.replace("_", " "));
            
            // Base variant
            langEntries.put("block.geco." + stoneName, capitalized);
            langEntries.put("block.geco." + stoneName + "_slab", capitalized + " Slab");
            langEntries.put("block.geco." + stoneName + "_stairs", capitalized + " Stairs");
            langEntries.put("block.geco." + stoneName + "_wall", capitalized + " Wall");
            
            // Polished variant
            langEntries.put("block.geco.polished_" + stoneName, "Polished " + capitalized);
            langEntries.put("block.geco.polished_" + stoneName + "_slab", "Polished " + capitalized + " Slab");
            langEntries.put("block.geco.polished_" + stoneName + "_stairs", "Polished " + capitalized + " Stairs");
            langEntries.put("block.geco.polished_" + stoneName + "_wall", "Polished " + capitalized + " Wall");
            
            // Polished bricks variant
            langEntries.put("block.geco.polished_" + stoneName + "_bricks", "Polished " + capitalized + " Bricks");
            langEntries.put("block.geco.polished_" + stoneName + "_bricks_slab", "Polished " + capitalized + " Brick Slab");
            langEntries.put("block.geco.polished_" + stoneName + "_bricks_stairs", "Polished " + capitalized + " Brick Stairs");
            langEntries.put("block.geco.polished_" + stoneName + "_bricks_wall", "Polished " + capitalized + " Brick Wall");
            
            // Polished tiles variant
            langEntries.put("block.geco.polished_" + stoneName + "_tiles", "Polished " + capitalized + " Tiles");
            langEntries.put("block.geco.polished_" + stoneName + "_tiles_slab", "Polished " + capitalized + " Tile Slab");
            langEntries.put("block.geco.polished_" + stoneName + "_tiles_stairs", "Polished " + capitalized + " Tile Stairs");
            langEntries.put("block.geco.polished_" + stoneName + "_tiles_wall", "Polished " + capitalized + " Tile Wall");
            
            // Smooth variant
            langEntries.put("block.geco.smooth_" + stoneName, "Smooth " + capitalized);
            langEntries.put("block.geco.smooth_" + stoneName + "_slab", "Smooth " + capitalized + " Slab");
            langEntries.put("block.geco.smooth_" + stoneName + "_stairs", "Smooth " + capitalized + " Stairs");
            langEntries.put("block.geco.smooth_" + stoneName + "_wall", "Smooth " + capitalized + " Wall");
        }
        
        writeJsonFile(OUTPUT_DIR.resolve("assets/geco/lang/en_us.json"), langEntries);
    }
    
    private static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) return input;
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            String word = words[i];
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    private static void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, GSON.toJson(data));
    }
}