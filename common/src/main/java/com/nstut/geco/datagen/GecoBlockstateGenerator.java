package com.nstut.geco.datagen;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GecoBlockstateGenerator {
    private final Path outputDir;
    private final Gson gson;

    public GecoBlockstateGenerator(Path outputDir, Gson gson) {
        this.outputDir = outputDir;
        this.gson = gson;
    }

    public void generateBlockstateFiles(GecoDataGenerator.WoodType wood) throws IOException {
        // Generate log blockstate
        Map<String, Object> logBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/" + wood.name() + "_log_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/" + wood.name() + "_log"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/" + wood.name() + "_log_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_log.json"), logBlockstate);

        // Generate stripped log blockstate
        Map<String, Object> strippedLogBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/stripped_" + wood.name() + "_log_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/stripped_" + wood.name() + "_log"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/stripped_" + wood.name() + "_log_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/stripped_" + wood.name() + "_log.json"), strippedLogBlockstate);

        // Generate wood blockstate
        Map<String, Object> woodBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/" + wood.name() + "_wood_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/" + wood.name() + "_wood"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/" + wood.name() + "_wood_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_wood.json"), woodBlockstate);

        // Generate stripped wood blockstate
        Map<String, Object> strippedWoodBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/stripped_" + wood.name() + "_wood_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/stripped_" + wood.name() + "_wood"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/stripped_" + wood.name() + "_wood_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/stripped_" + wood.name() + "_wood.json"), strippedWoodBlockstate);

        // Generate simple blockstates (planks, leaves, sapling, etc.)
        List<String> simpleBlocks = Arrays.asList(
            "planks", "leaves", "sapling"
        );
        
        for (String block : simpleBlocks) {
            Map<String, Object> blockstate = Map.of(
                "variants", Map.of(
                    "", Map.of("model", "geco:block/" + wood.name() + "_" + block)
                )
            );
            writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_" + block + ".json"), blockstate);
        }

        // Generate slab blockstate
        Map<String, Object> slabBlockstate = Map.of(
            "variants", Map.of(
                "type=bottom", Map.of("model", "geco:block/" + wood.name() + "_slab"),
                "type=double", Map.of("model", "geco:block/" + wood.name() + "_planks"),
                "type=top", Map.of("model", "geco:block/" + wood.name() + "_slab_top")
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_slab.json"), slabBlockstate);

        // Generate stairs blockstate
        Map<String, Object> stairsBlockstate = new HashMap<>();
        Map<String, Object> stairsVariants = new HashMap<>();
        stairsVariants.put("facing=east,half=bottom,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner"));
        stairsVariants.put("facing=east,half=bottom,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner"));
        stairsVariants.put("facing=east,half=bottom,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer"));
        stairsVariants.put("facing=east,half=bottom,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer"));
        stairsVariants.put("facing=east,half=bottom,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs"));
        stairsVariants.put("facing=east,half=top,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180, "y", 90));
        stairsVariants.put("facing=east,half=top,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180, "y", 90));
        stairsVariants.put("facing=east,half=top,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180, "y", 90));
        stairsVariants.put("facing=east,half=top,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180, "y", 90));
        stairsVariants.put("facing=east,half=top,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs", "x", 180, "y", 90));
        stairsVariants.put("facing=north,half=bottom,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "y", 270));
        stairsVariants.put("facing=north,half=bottom,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "y", 270));
        stairsVariants.put("facing=north,half=bottom,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "y", 270));
        stairsVariants.put("facing=north,half=bottom,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "y", 270));
        stairsVariants.put("facing=north,half=bottom,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs", "y", 270));
        stairsVariants.put("facing=north,half=top,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180));
        stairsVariants.put("facing=north,half=top,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180));
        stairsVariants.put("facing=north,half=top,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180));
        stairsVariants.put("facing=north,half=top,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180));
        stairsVariants.put("facing=north,half=top,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs", "x", 180));
        stairsVariants.put("facing=south,half=bottom,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "y", 90));
        stairsVariants.put("facing=south,half=bottom,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "y", 90));
        stairsVariants.put("facing=south,half=bottom,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "y", 90));
        stairsVariants.put("facing=south,half=bottom,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "y", 90));
        stairsVariants.put("facing=south,half=bottom,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs", "y", 90));
        stairsVariants.put("facing=south,half=top,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180, "y", 270));
        stairsVariants.put("facing=south,half=top,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180, "y", 270));
        stairsVariants.put("facing=south,half=top,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180, "y", 270));
        stairsVariants.put("facing=south,half=top,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180, "y", 270));
        stairsVariants.put("facing=south,half=top,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs", "x", 180, "y", 270));
        stairsVariants.put("facing=west,half=bottom,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "y", 180));
        stairsVariants.put("facing=west,half=bottom,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "y", 180));
        stairsVariants.put("facing=west,half=bottom,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "y", 180));
        stairsVariants.put("facing=west,half=bottom,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "y", 180));
        stairsVariants.put("facing=west,half=bottom,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs", "y", 180));
        stairsVariants.put("facing=west,half=top,shape=inner_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180, "y", 180));
        stairsVariants.put("facing=west,half=top,shape=inner_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_inner", "x", 180, "y", 180));
        stairsVariants.put("facing=west,half=top,shape=outer_left", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180, "y", 180));
        stairsVariants.put("facing=west,half=top,shape=outer_right", Map.of("model", "geco:block/" + wood.name() + "_stairs_outer", "x", 180, "y", 180));
        stairsVariants.put("facing=west,half=top,shape=straight", Map.of("model", "geco:block/" + wood.name() + "_stairs", "x", 180, "y", 180));
        stairsBlockstate.put("variants", stairsVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_stairs.json"), stairsBlockstate);

        // Generate fence blockstate
        Map<String, Object> fenceBlockstate = new HashMap<>();
        List<Map<String, Object>> fenceMultipart = new java.util.ArrayList<>();
        fenceMultipart.add(Map.of("apply", Map.of("model", "geco:block/" + wood.name() + "_fence_post")));
        fenceMultipart.add(Map.of(
            "when", Map.of("north", "true"),
            "apply", Map.of("model", "geco:block/" + wood.name() + "_fence_side", "uvlock", true)
        ));
        fenceMultipart.add(Map.of(
            "when", Map.of("east", "true"),
            "apply", Map.of("model", "geco:block/" + wood.name() + "_fence_side", "y", 90, "uvlock", true)
        ));
        fenceMultipart.add(Map.of(
            "when", Map.of("south", "true"),
            "apply", Map.of("model", "geco:block/" + wood.name() + "_fence_side", "y", 180, "uvlock", true)
        ));
        fenceMultipart.add(Map.of(
            "when", Map.of("west", "true"),
            "apply", Map.of("model", "geco:block/" + wood.name() + "_fence_side", "y", 270, "uvlock", true)
        ));
        fenceBlockstate.put("multipart", fenceMultipart);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_fence.json"), fenceBlockstate);

        // Generate fence gate blockstate
        Map<String, Object> fenceGateBlockstate = new HashMap<>();
        Map<String, Object> fenceGateVariants = new HashMap<>();
        fenceGateVariants.put("facing=east,in_wall=false,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate"));
        fenceGateVariants.put("facing=east,in_wall=false,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_open", "y", 90));
        fenceGateVariants.put("facing=east,in_wall=true,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall"));
        fenceGateVariants.put("facing=east,in_wall=true,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall_open", "y", 90));
        fenceGateVariants.put("facing=north,in_wall=false,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate", "y", 270));
        fenceGateVariants.put("facing=north,in_wall=false,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_open", "y", 180));
        fenceGateVariants.put("facing=north,in_wall=true,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall", "y", 270));
        fenceGateVariants.put("facing=north,in_wall=true,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall_open", "y", 180));
        fenceGateVariants.put("facing=south,in_wall=false,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate", "y", 90));
        fenceGateVariants.put("facing=south,in_wall=false,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_open"));
        fenceGateVariants.put("facing=south,in_wall=true,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall", "y", 90));
        fenceGateVariants.put("facing=south,in_wall=true,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall_open"));
        fenceGateVariants.put("facing=west,in_wall=false,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate", "y", 180));
        fenceGateVariants.put("facing=west,in_wall=false,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_open", "y", 270));
        fenceGateVariants.put("facing=west,in_wall=true,open=false", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall", "y", 180));
        fenceGateVariants.put("facing=west,in_wall=true,open=true", Map.of("model", "geco:block/" + wood.name() + "_fence_gate_wall_open", "y", 270));
        fenceGateBlockstate.put("variants", fenceGateVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_fence_gate.json"), fenceGateBlockstate);

        // Generate button blockstate
        Map<String, Object> buttonBlockstate = new HashMap<>();
        Map<String, Object> buttonVariants = new HashMap<>();
        buttonVariants.put("face=ceiling,facing=east,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "x", 90, "y", 270));
        buttonVariants.put("face=ceiling,facing=east,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "x", 90, "y", 270));
        buttonVariants.put("face=ceiling,facing=north,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "x", 90, "y", 180));
        buttonVariants.put("face=ceiling,facing=north,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "x", 90, "y", 180));
        buttonVariants.put("face=ceiling,facing=south,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "x", 90));
        buttonVariants.put("face=ceiling,facing=south,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "x", 90));
        buttonVariants.put("face=ceiling,facing=west,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "x", 90, "y", 90));
        buttonVariants.put("face=ceiling,facing=west,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "x", 90, "y", 90));
        buttonVariants.put("face=floor,facing=east,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "y", 90));
        buttonVariants.put("face=floor,facing=east,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "y", 90));
        buttonVariants.put("face=floor,facing=north,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button"));
        buttonVariants.put("face=floor,facing=north,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed"));
        buttonVariants.put("face=floor,facing=south,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "y", 180));
        buttonVariants.put("face=floor,facing=south,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "y", 180));
        buttonVariants.put("face=floor,facing=west,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "y", 270));
        buttonVariants.put("face=floor,facing=west,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "y", 270));
        buttonVariants.put("face=wall,facing=east,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "uvlock", true, "x", 90, "y", 90));
        buttonVariants.put("face=wall,facing=east,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "uvlock", true, "x", 90, "y", 90));
        buttonVariants.put("face=wall,facing=north,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "uvlock", true, "x", 90));
        buttonVariants.put("face=wall,facing=north,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "uvlock", true, "x", 90));
        buttonVariants.put("face=wall,facing=south,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "uvlock", true, "x", 90, "y", 180));
        buttonVariants.put("face=wall,facing=south,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "uvlock", true, "x", 90, "y", 180));
        buttonVariants.put("face=wall,facing=west,powered=false", Map.of("model", "geco:block/" + wood.name() + "_button", "uvlock", true, "x", 90, "y", 270));
        buttonVariants.put("face=wall,facing=west,powered=true", Map.of("model", "geco:block/" + wood.name() + "_button_pressed", "uvlock", true, "x", 90, "y", 270));
        buttonBlockstate.put("variants", buttonVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_button.json"), buttonBlockstate);

        // Generate pressure plate blockstate
        Map<String, Object> pressurePlateBlockstate = new HashMap<>();
        Map<String, Object> pressurePlateVariants = new HashMap<>();
        pressurePlateVariants.put("powered=false", Map.of("model", "geco:block/" + wood.name() + "_pressure_plate"));
        pressurePlateVariants.put("powered=true", Map.of("model", "geco:block/" + wood.name() + "_pressure_plate_down"));
        pressurePlateBlockstate.put("variants", pressurePlateVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_pressure_plate.json"), pressurePlateBlockstate);

        // Generate door blockstate
        Map<String, Object> doorBlockstate = new HashMap<>();
        Map<String, Object> doorVariants = new HashMap<>();
        doorVariants.put("facing=east,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left"));
        doorVariants.put("facing=east,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left_open", "y", 90));
        doorVariants.put("facing=east,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right"));
        doorVariants.put("facing=east,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right_open", "y", 270));
        doorVariants.put("facing=east,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_left"));
        doorVariants.put("facing=east,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_left_open", "y", 90));
        doorVariants.put("facing=east,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_right"));
        doorVariants.put("facing=east,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_right_open", "y", 270));
        doorVariants.put("facing=north,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left", "y", 270));
        doorVariants.put("facing=north,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left_open", "y", 180));
        doorVariants.put("facing=north,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right", "y", 270));
        doorVariants.put("facing=north,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right_open"));
        doorVariants.put("facing=north,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_left", "y", 270));
        doorVariants.put("facing=north,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_left_open", "y", 180));
        doorVariants.put("facing=north,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_right", "y", 270));
        doorVariants.put("facing=north,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_right_open"));
        doorVariants.put("facing=south,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left", "y", 90));
        doorVariants.put("facing=south,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left_open"));
        doorVariants.put("facing=south,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right", "y", 90));
        doorVariants.put("facing=south,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right_open", "y", 180));
        doorVariants.put("facing=south,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_left", "y", 90));
        doorVariants.put("facing=south,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_left_open"));
        doorVariants.put("facing=south,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_right", "y", 90));
        doorVariants.put("facing=south,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_right_open", "y", 180));
        doorVariants.put("facing=west,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left", "y", 180));
        doorVariants.put("facing=west,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_left_open", "y", 270));
        doorVariants.put("facing=west,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right", "y", 180));
        doorVariants.put("facing=west,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_bottom_right_open", "y", 90));
        doorVariants.put("facing=west,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_left", "y", 180));
        doorVariants.put("facing=west,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_left_open", "y", 270));
        doorVariants.put("facing=west,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + wood.name() + "_door_top_right", "y", 180));
        doorVariants.put("facing=west,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + wood.name() + "_door_top_right_open", "y", 90));
        doorBlockstate.put("variants", doorVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_door.json"), doorBlockstate);

        // Generate trapdoor blockstate
        Map<String, Object> trapdoorBlockstate = new HashMap<>();
        Map<String, Object> trapdoorVariants = new HashMap<>();
        trapdoorVariants.put("facing=east,half=bottom,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_bottom"));
        trapdoorVariants.put("facing=east,half=bottom,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open"));
        trapdoorVariants.put("facing=east,half=top,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_top"));
        trapdoorVariants.put("facing=east,half=top,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open", "x", 180));
        trapdoorVariants.put("facing=north,half=bottom,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_bottom", "y", 270));
        trapdoorVariants.put("facing=north,half=bottom,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open", "y", 270));
        trapdoorVariants.put("facing=north,half=top,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_top", "y", 270));
        trapdoorVariants.put("facing=north,half=top,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open", "x", 180, "y", 270));
        trapdoorVariants.put("facing=south,half=bottom,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_bottom", "y", 90));
        trapdoorVariants.put("facing=south,half=bottom,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open", "y", 90));
        trapdoorVariants.put("facing=south,half=top,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_top", "y", 90));
        trapdoorVariants.put("facing=south,half=top,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open", "x", 180, "y", 90));
        trapdoorVariants.put("facing=west,half=bottom,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_bottom", "y", 180));
        trapdoorVariants.put("facing=west,half=bottom,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open", "y", 180));
        trapdoorVariants.put("facing=west,half=top,open=false", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_top", "y", 180));
        trapdoorVariants.put("facing=west,half=top,open=true", Map.of("model", "geco:block/" + wood.name() + "_trapdoor_open", "x", 180, "y", 180));
        trapdoorBlockstate.put("variants", trapdoorVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + wood.name() + "_trapdoor.json"), trapdoorBlockstate);
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }
}