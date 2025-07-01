package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.nstut.geco.common.wood.WoodType;
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

    public void generateRecipeFiles(WoodType wood) throws IOException {
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
        Map<String, Object> slabRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "misc",
            "pattern", List.of(
                "PPP"
            ),
            "key", Map.of(
                "P", Map.of("item", "geco:" + woodName + "_planks")
            ),
            "result", Map.of(
                "id", "geco:" + woodName + "_slab",
                "count", 6
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipe/" + woodName + "_slab.json"), slabRecipe);

        // Generate stairs recipe
        Map<String, Object> stairsRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "category", "misc",
            "pattern", List.of(
                "P  ",
                "PP ",
                "PPP"
            ),
            "key", Map.of(
                "P", Map.of("item", "geco:" + woodName + "_planks")
            ),
            "result", Map.of(
                "id", "geco:" + woodName + "_stairs",
                "count", 4
            )
        );
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

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }
}