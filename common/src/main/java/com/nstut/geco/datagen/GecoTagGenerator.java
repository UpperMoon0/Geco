package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.nstut.geco.common.wood.WoodType;
import com.nstut.geco.common.stone.StoneType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.reflect.TypeToken;

public class GecoTagGenerator {
    private final Path outputDir;
    private final Gson gson;

    public GecoTagGenerator(Path outputDir, Gson gson) {
        this.outputDir = outputDir;
        this.gson = gson;
    }

    public void generateWoodTagFiles(WoodType wood) throws IOException {
        String woodName = wood.getPath();
        // Generate block tags
        Map<String, Object> blockLogsTag = Map.of(
            "replace", false,
            "values", List.of(
                "geco:" + woodName + "_log",
                "geco:" + woodName + "_wood",
                "geco:stripped_" + woodName + "_log",
                "geco:stripped_" + woodName + "_wood"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/tags/block/" + woodName + "_logs.json"), blockLogsTag);

        // Generate item tags
        Map<String, Object> itemLogsTag = Map.of(
            "replace", false,
            "values", List.of(
                "geco:" + woodName + "_log",
                "geco:" + woodName + "_wood",
                "geco:stripped_" + woodName + "_log",
                "geco:stripped_" + woodName + "_wood"
            )
        );
        writeJsonFile(outputDir.resolve("data/geco/tags/item/" + woodName + "_logs.json"), itemLogsTag);
    }

    public void generateMinecraftTagFiles(WoodType wood) throws IOException {
        String woodName = wood.getPath();
        // Block Tags
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/doors.json"), List.of("geco:" + woodName + "_door"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/fence_gates.json"), List.of("geco:" + woodName + "_fence_gate"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/fences.json"), List.of("geco:" + woodName + "_fence"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/leaves.json"), List.of("geco:" + woodName + "_leaves"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/logs_that_burn.json"), List.of("#geco:" + woodName + "_logs"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/logs.json"), List.of("#geco:" + woodName + "_logs"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/planks.json"), List.of("geco:" + woodName + "_planks"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/trapdoors.json"), List.of("geco:" + woodName + "_trapdoor"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_buttons.json"), List.of("geco:" + woodName + "_button"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_doors.json"), List.of("geco:" + woodName + "_door"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_fences.json"), List.of("geco:" + woodName + "_fence"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_pressure_plates.json"), List.of("geco:" + woodName + "_pressure_plate"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_slabs.json"), List.of("geco:" + woodName + "_slab"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_stairs.json"), List.of("geco:" + woodName + "_stairs"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_trapdoors.json"), List.of("geco:" + woodName + "_trapdoor"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/mineable/axe.json"), List.of(
                "#geco:" + woodName + "_logs",
                "geco:" + woodName + "_sapling",
                "geco:" + woodName + "_planks",
                "geco:" + woodName + "_stairs",
                "geco:" + woodName + "_slab",
                "geco:" + woodName + "_fence",
                "geco:" + woodName + "_fence_gate",
                "geco:" + woodName + "_door",
                "geco:" + woodName + "_trapdoor",
                "geco:" + woodName + "_pressure_plate"
        ));

        // Item Tags
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/fence_gates.json"), List.of("geco:" + woodName + "_fence_gate"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/fences.json"), List.of("geco:" + woodName + "_fence"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/logs_that_burn.json"), List.of("#geco:" + woodName + "_logs"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/planks.json"), List.of("geco:" + woodName + "_planks"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_buttons.json"), List.of("geco:" + woodName + "_button"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_doors.json"), List.of("geco:" + woodName + "_door"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_fences.json"), List.of("geco:" + woodName + "_fence"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_pressure_plates.json"), List.of("geco:" + woodName + "_pressure_plate"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_slabs.json"), List.of("geco:" + woodName + "_slab"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_stairs.json"), List.of("geco:" + woodName + "_stairs"));
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_trapdoors.json"), List.of("geco:" + woodName + "_trapdoor"));
    }

    public void generateStoneTagFiles(StoneType stone) throws IOException {
        String stoneName = stone.getPath();
        String[] variantNames = {stoneName, "polished_" + stoneName, "polished_" + stoneName + "_bricks", "polished_" + stoneName + "_tiles", "smooth_" + stoneName};

        // Generate mod-specific block tags for the stone family
        List<String> allStoneBlocks = new ArrayList<>();
        List<String> allSlabs = new ArrayList<>();
        List<String> allStairs = new ArrayList<>();
        List<String> allWalls = new ArrayList<>();

        for (String variantName : variantNames) {
            allStoneBlocks.add("geco:" + variantName);
            allSlabs.add("geco:" + variantName + "_slab");
            allStairs.add("geco:" + variantName + "_stairs");
            allWalls.add("geco:" + variantName + "_wall");
        }

        // Generate geco-specific stone family tags
        Map<String, Object> stoneBlockTag = Map.of(
            "replace", false,
            "values", allStoneBlocks
        );
        writeJsonFile(outputDir.resolve("data/geco/tags/block/" + stoneName + "_stones.json"), stoneBlockTag);

        Map<String, Object> stoneItemTag = Map.of(
            "replace", false,
            "values", allStoneBlocks
        );
        writeJsonFile(outputDir.resolve("data/geco/tags/item/" + stoneName + "_stones.json"), stoneItemTag);
    }

    public void generateStoneMinecraftTagFiles(StoneType stone) throws IOException {
        String stoneName = stone.getPath();
        String[] variantNames = {stoneName, "polished_" + stoneName, "polished_" + stoneName + "_bricks", "polished_" + stoneName + "_tiles", "smooth_" + stoneName};

        List<String> allStoneBlocks = new ArrayList<>();
        List<String> allSlabs = new ArrayList<>();
        List<String> allStairs = new ArrayList<>();
        List<String> allWalls = new ArrayList<>();

        for (String variantName : variantNames) {
            allStoneBlocks.add("geco:" + variantName);
            allSlabs.add("geco:" + variantName + "_slab");
            allStairs.add("geco:" + variantName + "_stairs");
            allWalls.add("geco:" + variantName + "_wall");
        }

        // Block Tags
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/slabs.json"), allSlabs);
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/stairs.json"), allStairs);
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/walls.json"), allWalls);
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/block/mineable/pickaxe.json"), List.of(
                allStoneBlocks,
                allSlabs,
                allStairs,
                allWalls
        ).stream().flatMap(List::stream).toList());

        // Item Tags
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/slabs.json"), allSlabs);
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/stairs.json"), allStairs);
        appendToJsonFile(outputDir.resolve("data/minecraft/tags/item/walls.json"), allWalls);
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }

    private void appendToJsonFile(Path path, List<String> newValues) throws IOException {
        Files.createDirectories(path.getParent());
        Map<String, Object> existingData;

        if (Files.exists(path)) {
            try {
                existingData = gson.fromJson(Files.readString(path), new TypeToken<Map<String, Object>>() {}.getType());
            } catch (Exception e) {
                System.err.println("Error reading existing tag file " + path + ": " + e.getMessage());
                existingData = new LinkedHashMap<>();
                existingData.put("replace", false);
                existingData.put("values", new ArrayList<>());
            }
        } else {
            existingData = new LinkedHashMap<>();
            existingData.put("replace", false);
            existingData.put("values", new ArrayList<>());
        }

        List<String> values = new ArrayList<>();
        Object rawValues = existingData.get("values");
        if (rawValues instanceof List<?>) {
            for (Object item : (List<?>) rawValues) {
                if (item instanceof String) {
                    values.add((String) item);
                } else {
                    System.err.println("Warning: Non-string element found in 'values' list in " + path + ". Skipping element: " + item);
                }
            }
        }
        
        for (String newValue : newValues) {
            if (!values.contains(newValue)) {
                values.add(newValue);
            }
        }
        existingData.put("values", values);
        Files.writeString(path, gson.toJson(existingData));
    }
}