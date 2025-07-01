package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.nstut.geco.common.wood.WoodType;
import com.nstut.geco.common.stone.StoneType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GecoRecipeGenerator {
    private final Path outputDir;
    private final Gson gson;

    public GecoRecipeGenerator(Path outputDir, Gson gson) {
        this.outputDir = outputDir;
        this.gson = gson;
    }

    public void generateWoodRecipeFiles(WoodType wood) throws IOException {
        String woodName = wood.getPath();
        // Generate planks recipe
        Map<String, Object> planksRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "category", "misc",
            "ingredients", List.of(Map.of(
                "tag", "geco:" + woodName + "_logs"
            )),
            "result", Map.of(
                "id", "geco:" + woodName + "_planks",
                "count", 4
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_planks.json"), planksRecipe);

        // Generate wood recipe
        Map<String, Object> woodRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "category", "misc",
            "ingredients", List.of(Map.of(
                "item", "geco:" + woodName + "_log"
            )),
            "result", Map.of(
                "id", "geco:" + woodName + "_wood",
                "count", 1
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_wood.json"), woodRecipe);

        // Generate stripped wood recipe
        Map<String, Object> strippedWoodRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "category", "misc",
            "ingredients", List.of(Map.of(
                "item", "geco:stripped_" + woodName + "_log"
            )),
            "result", Map.of(
                "id", "geco:stripped_" + woodName + "_wood",
                "count", 1
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/stripped_" + woodName + "_wood.json"), strippedWoodRecipe);

        // Generate slab recipe
        Map<String, Object> slabRecipe = generateSlabRecipe(woodName + "_planks", woodName + "_slab", "misc");
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_slab.json"), slabRecipe);

        // Generate stairs recipe
        Map<String, Object> stairsRecipe = generateStairsRecipe(woodName + "_planks", woodName + "_stairs", "misc");
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_stairs.json"), stairsRecipe);

        // Generate fence recipe
        Map<String, Object> fenceRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "misc",
            "pattern", List.of(
                "PSP",
                "PSP"
            ),
            "key", Map.of(
                "P", Map.of("item", "geco:" + woodName + "_planks"),
                "S", Map.of("item", "minecraft:stick")
            ),
            "result", Map.of(
                "id", "geco:" + woodName + "_fence",
                "count", 3
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_fence.json"), fenceRecipe);

        // Generate fence gate recipe
        Map<String, Object> fenceGateRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "misc",
            "pattern", List.of(
                "SPS",
                "SPS"
            ),
            "key", Map.of(
                "P", Map.of("item", "geco:" + woodName + "_planks"),
                "S", Map.of("item", "minecraft:stick")
            ),
            "result", Map.of(
                "id", "geco:" + woodName + "_fence_gate"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_fence_gate.json"), fenceGateRecipe);

        // Generate button recipe
        Map<String, Object> buttonRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "category", "misc",
            "ingredients", List.of(Map.of(
                "item", "geco:" + woodName + "_planks"
            )),
            "result", Map.of(
                "id", "geco:" + woodName + "_button"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_button.json"), buttonRecipe);

        // Generate pressure plate recipe
        Map<String, Object> pressurePlateRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "misc",
            "pattern", List.of(
                "PP"
            ),
            "key", Map.of(
                "P", Map.of("item", "geco:" + woodName + "_planks")
            ),
            "result", Map.of(
                "id", "geco:" + woodName + "_pressure_plate"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_pressure_plate.json"), pressurePlateRecipe);

        // Generate door recipe
        Map<String, Object> doorRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "misc",
            "pattern", List.of(
                "PP",
                "PP",
                "PP"
            ),
            "key", Map.of(
                "P", Map.of("item", "geco:" + woodName + "_planks")
            ),
            "result", Map.of(
                "id", "geco:" + woodName + "_door",
                "count", 3
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_door.json"), doorRecipe);

        // Generate trapdoor recipe
        Map<String, Object> trapdoorRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "misc",
            "pattern", List.of(
                "PPP",
                "PPP"
            ),
            "key", Map.of(
                "P", Map.of("item", "geco:" + woodName + "_planks")
            ),
            "result", Map.of(
                "id", "geco:" + woodName + "_trapdoor"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_trapdoor.json"), trapdoorRecipe);
    }

    public void generateStoneRecipeFiles(StoneType stone) throws IOException {
        String stoneName = stone.getPath();
        String[] variantNames = {stoneName, "polished_" + stoneName, "polished_" + stoneName + "_bricks", "polished_" + stoneName + "_tiles", "smooth_" + stoneName};

        for (String variantName : variantNames) {
            // Generate crafting recipes using helper methods
            Map<String, Object> slabRecipe = generateSlabRecipe(variantName, variantName + "_slab", "building");
            writeJsonFile(outputDir.resolve("data/geco/recipe/" + variantName + "_slab.json"), slabRecipe);

            Map<String, Object> stairsRecipe = generateStairsRecipe(variantName, variantName + "_stairs", "building");
            writeJsonFile(outputDir.resolve("data/geco/recipe/" + variantName + "_stairs.json"), stairsRecipe);

            Map<String, Object> wallRecipe = generateWallRecipe(variantName, variantName + "_wall");
            writeJsonFile(outputDir.resolve("data/geco/recipe/" + variantName + "_wall.json"), wallRecipe);

            // Generate stonecutting recipes
            Map<String, Object> slabStonecutting = generateStonecuttingRecipe(variantName, variantName + "_slab", 2);
            writeJsonFile(outputDir.resolve("data/geco/recipe/" + variantName + "_slab_from_" + variantName + "_stonecutting.json"), slabStonecutting);

            Map<String, Object> stairsStonecutting = generateStonecuttingRecipe(variantName, variantName + "_stairs", 1);
            writeJsonFile(outputDir.resolve("data/geco/recipe/" + variantName + "_stairs_from_" + variantName + "_stonecutting.json"), stairsStonecutting);

            Map<String, Object> wallStonecutting = generateStonecuttingRecipe(variantName, variantName + "_wall", 1);
            writeJsonFile(outputDir.resolve("data/geco/recipe/" + variantName + "_wall_from_" + variantName + "_stonecutting.json"), wallStonecutting);
        }

        // Generate variant conversion recipes
        Map<String, Object> polishedStonecutting = generateStonecuttingRecipe(stoneName, "polished_" + stoneName, 1);
        writeJsonFile(outputDir.resolve("data/geco/recipe/polished_" + stoneName + "_from_" + stoneName + "_stonecutting.json"), polishedStonecutting);

        Map<String, Object> bricksStonecutting = generateStonecuttingRecipe("polished_" + stoneName, "polished_" + stoneName + "_bricks", 1);
        writeJsonFile(outputDir.resolve("data/geco/recipe/polished_" + stoneName + "_bricks_from_polished_" + stoneName + "_stonecutting.json"), bricksStonecutting);

        Map<String, Object> tilesStonecutting = generateStonecuttingRecipe("polished_" + stoneName, "polished_" + stoneName + "_tiles", 1);
        writeJsonFile(outputDir.resolve("data/geco/recipe/polished_" + stoneName + "_tiles_from_polished_" + stoneName + "_stonecutting.json"), tilesStonecutting);

        Map<String, Object> smoothSmelting = generateSmeltingRecipe(stoneName, "smooth_" + stoneName);
        writeJsonFile(outputDir.resolve("data/geco/recipe/smooth_" + stoneName + "_from_" + stoneName + "_smelting.json"), smoothSmelting);

        // Generate crafting table recipes for stone variants
        Map<String, Object> polishedCrafting = generatePolishedStoneCraftingRecipe(stoneName, "polished_" + stoneName);
        writeJsonFile(outputDir.resolve("data/geco/recipe/polished_" + stoneName + ".json"), polishedCrafting);

        Map<String, Object> bricksCrafting = generatePolishedStoneCraftingRecipe("polished_" + stoneName, "polished_" + stoneName + "_bricks");
        writeJsonFile(outputDir.resolve("data/geco/recipe/polished_" + stoneName + "_bricks.json"), bricksCrafting);

        Map<String, Object> tilesCrafting = generatePolishedTilesCraftingRecipe("polished_" + stoneName, "polished_" + stoneName + "_tiles");
        writeJsonFile(outputDir.resolve("data/geco/recipe/polished_" + stoneName + "_tiles.json"), tilesCrafting);
    }

    // Helper methods for common recipe patterns
    private Map<String, Object> generateSlabRecipe(String ingredient, String result, String category) {
        return Map.of(
            "type", "minecraft:crafting_shaped",
            "category", category,
            "pattern", List.of("BBB"),
            "key", Map.of("B", Map.of("item", "geco:" + ingredient)),
            "result", Map.of("id", "geco:" + result, "count", 6)
        );
    }

    private Map<String, Object> generateStairsRecipe(String ingredient, String result, String category) {
        return Map.of(
            "type", "minecraft:crafting_shaped",
            "category", category,
            "pattern", List.of("B  ", "BB ", "BBB"),
            "key", Map.of("B", Map.of("item", "geco:" + ingredient)),
            "result", Map.of("id", "geco:" + result, "count", 4)
        );
    }

    private Map<String, Object> generateWallRecipe(String ingredient, String result) {
        return Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "building",
            "pattern", List.of("BBB", "BBB"),
            "key", Map.of("B", Map.of("item", "geco:" + ingredient)),
            "result", Map.of("id", "geco:" + result, "count", 6)
        );
    }

    private Map<String, Object> generateStonecuttingRecipe(String ingredient, String result, int count) {
        return Map.of(
            "type", "minecraft:stonecutting",
            "ingredient", Map.of("item", "geco:" + ingredient),
            "result", Map.of("id", "geco:" + result, "count", count)
        );
    }

    private Map<String, Object> generateSmeltingRecipe(String ingredient, String result) {
        return Map.of(
            "type", "minecraft:smelting",
            "category", "building",
            "ingredient", Map.of("item", "geco:" + ingredient),
            "result", Map.of("id", "geco:" + result, "count", 1),
            "experience", 0.1,
            "cookingtime", 200
        );
    }

    private Map<String, Object> generatePolishedStoneCraftingRecipe(String ingredient, String result) {
        return Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "building",
            "pattern", List.of("BB", "BB"),
            "key", Map.of("B", Map.of("item", "geco:" + ingredient)),
            "result", Map.of("id", "geco:" + result, "count", 4)
        );
    }

    private Map<String, Object> generatePolishedTilesCraftingRecipe(String ingredient, String result) {
        return Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "building",
            "pattern", List.of("BBB", "B B", "BBB"),
            "key", Map.of("B", Map.of("item", "geco:" + ingredient)),
            "result", Map.of("id", "geco:" + result, "count", 8)
        );
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }
}