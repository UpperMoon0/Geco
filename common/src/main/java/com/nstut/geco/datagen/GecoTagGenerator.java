package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.nstut.geco.common.wood.WoodType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GecoTagGenerator {
    private final Path outputDir;
    private final Gson gson;

    public GecoTagGenerator(Path outputDir, Gson gson) {
        this.outputDir = outputDir;
        this.gson = gson;
    }

    public void generateTagFiles(WoodType wood) throws IOException {
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
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/doors.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_door")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/fence_gates.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_fence_gate")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/fences.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_fence")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/leaves.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_leaves")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/logs_that_burn.json"), Map.of(
            "replace", false,
            "values", List.of("#geco:" + woodName + "_logs")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/logs.json"), Map.of(
            "replace", false,
            "values", List.of("#geco:" + woodName + "_logs")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/planks.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_planks")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/trapdoors.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_trapdoor")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_buttons.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_button")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_doors.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_door")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_fences.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_fence")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_pressure_plates.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_pressure_plate")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_slabs.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_slab")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_stairs.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_stairs")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/wooden_trapdoors.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_trapdoor")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/block/mineable/axe.json"), Map.of(
            "replace", false,
            "values", List.of(
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
            )
        ));

        // Item Tags
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/fence_gates.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_fence_gate")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/fences.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_fence")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/logs_that_burn.json"), Map.of(
            "replace", false,
            "values", List.of("#geco:" + woodName + "_logs")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/planks.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_planks")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_buttons.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_button")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_doors.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_door")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_fences.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_fence")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_pressure_plates.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_pressure_plate")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_slabs.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_slab")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_stairs.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_stairs")
        ));
        writeJsonFile(outputDir.resolve("data/minecraft/tags/item/wooden_trapdoors.json"), Map.of(
            "replace", false,
            "values", List.of("geco:" + woodName + "_trapdoor")
        ));
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }
}