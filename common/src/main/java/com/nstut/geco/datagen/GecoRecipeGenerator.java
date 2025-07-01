package com.nstut.geco.datagen;

import com.google.gson.Gson;
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

    public void generateRecipeFiles(GecoDataGenerator.WoodType wood) throws IOException {
        // Generate planks recipe
        Map<String, Object> planksRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "category", "building",
            "ingredients", List.of(Map.of(
                "tag", "geco:" + wood.name() + "_logs"
            )),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_planks",
                "count", 4
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_planks.json"), planksRecipe);

        // Generate wood recipe
        Map<String, Object> woodRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "category", "building",
            "ingredients", List.of(Map.of(
                "item", "geco:" + wood.name() + "_log"
            )),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_wood",
                "count", 1
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_wood.json"), woodRecipe);

        // Generate stripped wood recipe
        Map<String, Object> strippedWoodRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "category", "building",
            "ingredients", List.of(Map.of(
                "item", "geco:stripped_" + wood.name() + "_log"
            )),
            "result", Map.of(
                "item", "geco:stripped_" + wood.name() + "_wood",
                "count", 1
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/stripped_" + wood.name() + "_wood.json"), strippedWoodRecipe);

        // Generate slab recipe
        Map<String, Object> slabRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "pattern", List.of(
                "###"
            ),
            "key", Map.of(
                "#", Map.of("item", "geco:" + wood.name() + "_planks")
            ),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_slab",
                "count", 6
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_slab.json"), slabRecipe);

        // Generate stairs recipe
        Map<String, Object> stairsRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "pattern", List.of(
                "#  ",
                "## ",
                "###"
            ),
            "key", Map.of(
                "#", Map.of("item", "geco:" + wood.name() + "_planks")
            ),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_stairs",
                "count", 4
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_stairs.json"), stairsRecipe);

        // Generate fence recipe
        Map<String, Object> fenceRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "pattern", List.of(
                "W#W",
                "W#W"
            ),
            "key", Map.of(
                "W", Map.of("item", "geco:" + wood.name() + "_planks"),
                "#", Map.of("item", "minecraft:stick")
            ),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_fence",
                "count", 3
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_fence.json"), fenceRecipe);

        // Generate fence gate recipe
        Map<String, Object> fenceGateRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "pattern", List.of(
                "#W#",
                "#W#"
            ),
            "key", Map.of(
                "W", Map.of("item", "geco:" + wood.name() + "_planks"),
                "#", Map.of("item", "minecraft:stick")
            ),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_fence_gate"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_fence_gate.json"), fenceGateRecipe);

        // Generate button recipe
        Map<String, Object> buttonRecipe = Map.of(
            "type", "minecraft:crafting_shapeless",
            "ingredients", List.of(Map.of(
                "item", "geco:" + wood.name() + "_planks"
            )),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_button"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_button.json"), buttonRecipe);

        // Generate pressure plate recipe
        Map<String, Object> pressurePlateRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "pattern", List.of(
                "##"
            ),
            "key", Map.of(
                "#", Map.of("item", "geco:" + wood.name() + "_planks")
            ),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_pressure_plate"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_pressure_plate.json"), pressurePlateRecipe);

        // Generate door recipe
        Map<String, Object> doorRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "pattern", List.of(
                "##",
                "##",
                "##"
            ),
            "key", Map.of(
                "#", Map.of("item", "geco:" + wood.name() + "_planks")
            ),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_door",
                "count", 3
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_door.json"), doorRecipe);

        // Generate trapdoor recipe
        Map<String, Object> trapdoorRecipe = Map.of(
            "type", "minecraft:crafting_shaped",
            "pattern", List.of(
                "###",
                "###"
            ),
            "key", Map.of(
                "#", Map.of("item", "geco:" + wood.name() + "_planks")
            ),
            "result", Map.of(
                "item", "geco:" + wood.name() + "_trapdoor"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/recipes/" + wood.name() + "_trapdoor.json"), trapdoorRecipe);
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }
}