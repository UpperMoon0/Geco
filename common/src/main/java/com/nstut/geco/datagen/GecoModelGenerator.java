package com.nstut.geco.datagen;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GecoModelGenerator {
    private final Path outputDir;
    private final Gson gson;

    public GecoModelGenerator(Path outputDir, Gson gson) {
        this.outputDir = outputDir;
        this.gson = gson;
    }

    public void generateBlockModelFiles(GecoDataGenerator.WoodType wood) throws IOException {
        // Generate vertical log model
        Map<String, Object> logModel = new HashMap<>();
        logModel.put("parent", "minecraft:block/cube_column");
        logModel.put("textures", Map.of(
            "end", "geco:block/" + wood.name() + "_log_top",
            "side", "geco:block/" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_log.json"), logModel);

        // Generate horizontal log model
        Map<String, Object> logHorizontalModel = new HashMap<>();
        logHorizontalModel.put("parent", "minecraft:block/cube_column_horizontal");
        logHorizontalModel.put("textures", Map.of(
            "end", "geco:block/" + wood.name() + "_log_top",
            "side", "geco:block/" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_log_horizontal.json"), logHorizontalModel);

        // Generate stripped vertical log model
        Map<String, Object> strippedLogModel = new HashMap<>();
        strippedLogModel.put("parent", "minecraft:block/cube_column");
        strippedLogModel.put("textures", Map.of(
            "end", "geco:block/stripped_" + wood.name() + "_log_top",
            "side", "geco:block/stripped_" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/stripped_" + wood.name() + "_log.json"), strippedLogModel);

        // Generate stripped horizontal log model
        Map<String, Object> strippedLogHorizontalModel = new HashMap<>();
        strippedLogHorizontalModel.put("parent", "minecraft:block/cube_column_horizontal");
        strippedLogHorizontalModel.put("textures", Map.of(
            "end", "geco:block/stripped_" + wood.name() + "_log_top",
            "side", "geco:block/stripped_" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/stripped_" + wood.name() + "_log_horizontal.json"), strippedLogHorizontalModel);

        // Generate vertical wood model
        Map<String, Object> woodModel = new HashMap<>();
        woodModel.put("parent", "minecraft:block/cube_column");
        woodModel.put("textures", Map.of(
            "end", "geco:block/" + wood.name() + "_log",
            "side", "geco:block/" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_wood.json"), woodModel);

        // Generate horizontal wood model
        Map<String, Object> woodHorizontalModel = new HashMap<>();
        woodHorizontalModel.put("parent", "minecraft:block/cube_column_horizontal");
        woodHorizontalModel.put("textures", Map.of(
            "end", "geco:block/" + wood.name() + "_log",
            "side", "geco:block/" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_wood_horizontal.json"), woodHorizontalModel);

        // Generate stripped vertical wood model
        Map<String, Object> strippedWoodModel = new HashMap<>();
        strippedWoodModel.put("parent", "minecraft:block/cube_column");
        strippedWoodModel.put("textures", Map.of(
            "end", "geco:block/stripped_" + wood.name() + "_log",
            "side", "geco:block/stripped_" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/stripped_" + wood.name() + "_wood.json"), strippedWoodModel);

        // Generate stripped horizontal wood model
        Map<String, Object> strippedWoodHorizontalModel = new HashMap<>();
        strippedWoodHorizontalModel.put("parent", "minecraft:block/cube_column_horizontal");
        strippedWoodHorizontalModel.put("textures", Map.of(
            "end", "geco:block/stripped_" + wood.name() + "_log",
            "side", "geco:block/stripped_" + wood.name() + "_log"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/stripped_" + wood.name() + "_wood_horizontal.json"), strippedWoodHorizontalModel);

        // Generate planks model
        Map<String, Object> planksModel = new HashMap<>();
        planksModel.put("parent", "minecraft:block/cube_all");
        planksModel.put("textures", Map.of(
            "all", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_planks.json"), planksModel);

        // Generate leaves model
        Map<String, Object> leavesModel = new HashMap<>();
        leavesModel.put("parent", "minecraft:block/cube_all");
        leavesModel.put("textures", Map.of(
            "all", "geco:block/" + wood.name() + "_leaves"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_leaves.json"), leavesModel);

        // Generate sapling model
        Map<String, Object> saplingModel = new HashMap<>();
        saplingModel.put("parent", "minecraft:block/cross");
        saplingModel.put("textures", Map.of(
            "cross", "geco:block/" + wood.name() + "_sapling"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_sapling.json"), saplingModel);

        // Generate slab models
        Map<String, Object> slabModel = new HashMap<>();
        slabModel.put("parent", "minecraft:block/slab");
        slabModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_planks",
            "top", "geco:block/" + wood.name() + "_planks",
            "side", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_slab.json"), slabModel);

        Map<String, Object> slabTopModel = new HashMap<>();
        slabTopModel.put("parent", "minecraft:block/slab_top");
        slabTopModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_planks",
            "top", "geco:block/" + wood.name() + "_planks",
            "side", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_slab_top.json"), slabTopModel);

        // Generate stairs models
        Map<String, Object> stairsModel = new HashMap<>();
        stairsModel.put("parent", "minecraft:block/stairs");
        stairsModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_planks",
            "top", "geco:block/" + wood.name() + "_planks",
            "side", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_stairs.json"), stairsModel);

        Map<String, Object> stairsInnerModel = new HashMap<>();
        stairsInnerModel.put("parent", "minecraft:block/inner_stairs");
        stairsInnerModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_planks",
            "top", "geco:block/" + wood.name() + "_planks",
            "side", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_stairs_inner.json"), stairsInnerModel);

        Map<String, Object> stairsOuterModel = new HashMap<>();
        stairsOuterModel.put("parent", "minecraft:block/outer_stairs");
        stairsOuterModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_planks",
            "top", "geco:block/" + wood.name() + "_planks",
            "side", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_stairs_outer.json"), stairsOuterModel);

        // Generate fence models
        Map<String, Object> fencePostModel = new HashMap<>();
        fencePostModel.put("parent", "minecraft:block/fence_post");
        fencePostModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_fence_post.json"), fencePostModel);

        Map<String, Object> fenceSideModel = new HashMap<>();
        fenceSideModel.put("parent", "minecraft:block/fence_side");
        fenceSideModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_fence_side.json"), fenceSideModel);

        // Generate fence inventory model
        Map<String, Object> fenceInventoryModel = new HashMap<>();
        fenceInventoryModel.put("parent", "minecraft:block/fence_inventory");
        fenceInventoryModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_fence_inventory.json"), fenceInventoryModel);

        // Generate fence gate models
        Map<String, Object> fenceGateModel = new HashMap<>();
        fenceGateModel.put("parent", "minecraft:block/template_fence_gate");
        fenceGateModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_fence_gate.json"), fenceGateModel);

        Map<String, Object> fenceGateOpenModel = new HashMap<>();
        fenceGateOpenModel.put("parent", "minecraft:block/template_fence_gate_open");
        fenceGateOpenModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_fence_gate_open.json"), fenceGateOpenModel);

        Map<String, Object> fenceGateWallModel = new HashMap<>();
        fenceGateWallModel.put("parent", "minecraft:block/template_fence_gate_wall");
        fenceGateWallModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_fence_gate_wall.json"), fenceGateWallModel);

        Map<String, Object> fenceGateWallOpenModel = new HashMap<>();
        fenceGateWallOpenModel.put("parent", "minecraft:block/template_fence_gate_wall_open");
        fenceGateWallOpenModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_fence_gate_wall_open.json"), fenceGateWallOpenModel);

        // Generate button models
        Map<String, Object> buttonModel = new HashMap<>();
        buttonModel.put("parent", "minecraft:block/button");
        buttonModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_button.json"), buttonModel);

        Map<String, Object> buttonPressedModel = new HashMap<>();
        buttonPressedModel.put("parent", "minecraft:block/button_pressed");
        buttonPressedModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_button_pressed.json"), buttonPressedModel);

        // Generate pressure plate models
        Map<String, Object> pressurePlateModel = new HashMap<>();
        pressurePlateModel.put("parent", "minecraft:block/pressure_plate_up");
        pressurePlateModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_pressure_plate.json"), pressurePlateModel);

        Map<String, Object> pressurePlateDownModel = new HashMap<>();
        pressurePlateDownModel.put("parent", "minecraft:block/pressure_plate_down");
        pressurePlateDownModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_pressure_plate_down.json"), pressurePlateDownModel);

        // Generate door models
        Map<String, Object> doorBottomLeftModel = new HashMap<>();
        doorBottomLeftModel.put("parent", "minecraft:block/door_bottom_left");
        doorBottomLeftModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_bottom_left.json"), doorBottomLeftModel);

        Map<String, Object> doorBottomLeftOpenModel = new HashMap<>();
        doorBottomLeftOpenModel.put("parent", "minecraft:block/door_bottom_left_open");
        doorBottomLeftOpenModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_bottom_left_open.json"), doorBottomLeftOpenModel);

        Map<String, Object> doorBottomRightModel = new HashMap<>();
        doorBottomRightModel.put("parent", "minecraft:block/door_bottom_right");
        doorBottomRightModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_bottom_right.json"), doorBottomRightModel);

        Map<String, Object> doorBottomRightOpenModel = new HashMap<>();
        doorBottomRightOpenModel.put("parent", "minecraft:block/door_bottom_right_open");
        doorBottomRightOpenModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_bottom_right_open.json"), doorBottomRightOpenModel);

        Map<String, Object> doorTopLeftModel = new HashMap<>();
        doorTopLeftModel.put("parent", "minecraft:block/door_top_left");
        doorTopLeftModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_top_left.json"), doorTopLeftModel);

        Map<String, Object> doorTopLeftOpenModel = new HashMap<>();
        doorTopLeftOpenModel.put("parent", "minecraft:block/door_top_left_open");
        doorTopLeftOpenModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_top_left_open.json"), doorTopLeftOpenModel);

        Map<String, Object> doorTopRightModel = new HashMap<>();
        doorTopRightModel.put("parent", "minecraft:block/door_top_right");
        doorTopRightModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_top_right.json"), doorTopRightModel);

        Map<String, Object> doorTopRightOpenModel = new HashMap<>();
        doorTopRightOpenModel.put("parent", "minecraft:block/door_top_right_open");
        doorTopRightOpenModel.put("textures", Map.of(
            "bottom", "geco:block/" + wood.name() + "_door_bottom",
            "top", "geco:block/" + wood.name() + "_door_top"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_door_top_right_open.json"), doorTopRightOpenModel);

        // Generate trapdoor models
        Map<String, Object> trapdoorBottomModel = new HashMap<>();
        trapdoorBottomModel.put("parent", "minecraft:block/template_trapdoor_bottom");
        trapdoorBottomModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_trapdoor"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_trapdoor_bottom.json"), trapdoorBottomModel);

        Map<String, Object> trapdoorOpenModel = new HashMap<>();
        trapdoorOpenModel.put("parent", "minecraft:block/template_trapdoor_open");
        trapdoorOpenModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_trapdoor"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_trapdoor_open.json"), trapdoorOpenModel);

        Map<String, Object> trapdoorTopModel = new HashMap<>();
        trapdoorTopModel.put("parent", "minecraft:block/template_trapdoor_top");
        trapdoorTopModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_trapdoor"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/block/" + wood.name() + "_trapdoor_top.json"), trapdoorTopModel);
    }

    public void generateItemModelFiles(GecoDataGenerator.WoodType wood) throws IOException {
        // Generate log item model
        Map<String, Object> logItemModel = new HashMap<>();
        logItemModel.put("parent", "geco:block/" + wood.name() + "_log");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_log.json"), logItemModel);

        // Generate stripped log item model
        Map<String, Object> strippedLogItemModel = new HashMap<>();
        strippedLogItemModel.put("parent", "geco:block/stripped_" + wood.name() + "_log");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/stripped_" + wood.name() + "_log.json"), strippedLogItemModel);

        // Generate wood item model
        Map<String, Object> woodItemModel = new HashMap<>();
        woodItemModel.put("parent", "geco:block/" + wood.name() + "_wood");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_wood.json"), woodItemModel);

        // Generate stripped wood item model
        Map<String, Object> strippedWoodItemModel = new HashMap<>();
        strippedWoodItemModel.put("parent", "geco:block/stripped_" + wood.name() + "_wood");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/stripped_" + wood.name() + "_wood.json"), strippedWoodItemModel);

        // Generate planks item model
        Map<String, Object> planksItemModel = new HashMap<>();
        planksItemModel.put("parent", "geco:block/" + wood.name() + "_planks");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_planks.json"), planksItemModel);

        // Generate leaves item model
        Map<String, Object> leavesItemModel = new HashMap<>();
        leavesItemModel.put("parent", "geco:block/" + wood.name() + "_leaves");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_leaves.json"), leavesItemModel);

        // Generate sapling item model
        Map<String, Object> saplingItemModel = new HashMap<>();
        saplingItemModel.put("parent", "geco:block/" + wood.name() + "_sapling");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_sapling.json"), saplingItemModel);

        // Generate slab item model
        Map<String, Object> slabItemModel = new HashMap<>();
        slabItemModel.put("parent", "geco:block/" + wood.name() + "_slab");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_slab.json"), slabItemModel);

        // Generate stairs item model
        Map<String, Object> stairsItemModel = new HashMap<>();
        stairsItemModel.put("parent", "geco:block/" + wood.name() + "_stairs");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_stairs.json"), stairsItemModel);

        // Generate fence item model
        Map<String, Object> fenceItemModel = new HashMap<>();
        fenceItemModel.put("parent", "geco:block/" + wood.name() + "_fence_inventory");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_fence.json"), fenceItemModel);

        // Generate fence gate item model
        Map<String, Object> fenceGateItemModel = new HashMap<>();
        fenceGateItemModel.put("parent", "geco:block/" + wood.name() + "_fence_gate");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_fence_gate.json"), fenceGateItemModel);

        // Generate button item model
        Map<String, Object> buttonItemModel = new HashMap<>();
        buttonItemModel.put("parent", "minecraft:block/button_inventory");
        buttonItemModel.put("textures", Map.of(
            "texture", "geco:block/" + wood.name() + "_planks"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_button.json"), buttonItemModel);

        // Generate pressure plate item model
        Map<String, Object> pressurePlateItemModel = new HashMap<>();
        pressurePlateItemModel.put("parent", "geco:block/" + wood.name() + "_pressure_plate");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_pressure_plate.json"), pressurePlateItemModel);

        // Generate door item model
        Map<String, Object> doorItemModel = new HashMap<>();
        doorItemModel.put("parent", "minecraft:item/generated");
        doorItemModel.put("textures", Map.of(
            "layer0", "geco:item/" + wood.name() + "_door"
        ));
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_door.json"), doorItemModel);

        // Generate trapdoor item model
        Map<String, Object> trapdoorItemModel = new HashMap<>();
        trapdoorItemModel.put("parent", "geco:block/" + wood.name() + "_trapdoor_bottom");
        writeJsonFile(outputDir.resolve("assets/geco/models/item/" + wood.name() + "_trapdoor.json"), trapdoorItemModel);
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }
}