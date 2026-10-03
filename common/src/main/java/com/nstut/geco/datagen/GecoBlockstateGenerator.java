package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.nstut.geco.common.wood.WoodType;
import com.nstut.geco.common.stone.StoneType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public void generateWoodBlockstateFiles(WoodType wood) throws IOException {
        String woodName = wood.getPath();
        // Generate log blockstate
        Map<String, Object> logBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/" + woodName + "_log_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/" + woodName + "_log"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/" + woodName + "_log_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_log.json"), logBlockstate);

        // Generate stripped log blockstate
        Map<String, Object> strippedLogBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/stripped_" + woodName + "_log_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/stripped_" + woodName + "_log"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/stripped_" + woodName + "_log_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/stripped_" + woodName + "_log.json"), strippedLogBlockstate);

        // Generate wood blockstate
        Map<String, Object> woodBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/" + woodName + "_wood_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/" + woodName + "_wood"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/" + woodName + "_wood_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_wood.json"), woodBlockstate);

        // Generate stripped wood blockstate
        Map<String, Object> strippedWoodBlockstate = Map.of(
            "variants", Map.of(
                "axis=x", Map.of(
                    "model", "geco:block/stripped_" + woodName + "_wood_horizontal",
                    "x", 90,
                    "y", 90
                ),
                "axis=y", Map.of(
                    "model", "geco:block/stripped_" + woodName + "_wood"
                ),
                "axis=z", Map.of(
                    "model", "geco:block/stripped_" + woodName + "_wood_horizontal",
                    "x", 90
                )
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/stripped_" + woodName + "_wood.json"), strippedWoodBlockstate);

        // Generate planks blockstate
        Map<String, Object> planksBlockstate = Map.of(
            "variants", Map.of(
                "", Map.of("model", "geco:block/" + woodName + "_planks")
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_planks.json"), planksBlockstate);

        // Generate leaves blockstate with all properties
        Map<String, Object> leavesBlockstate = new HashMap<>();
        Map<String, Object> leavesVariants = new HashMap<>();

        // Generate all combinations of distance (1-7), persistent (true/false), waterlogged (true/false)
        for (int distance = 1; distance <= 7; distance++) {
            for (boolean persistent : new boolean[]{false, true}) {
                for (boolean waterlogged : new boolean[]{false, true}) {
                    String key = String.format("distance=%d,persistent=%b,waterlogged=%b", distance, persistent, waterlogged);
                    leavesVariants.put(key, Map.of("model", "geco:block/" + woodName + "_leaves"));
                }
            }
        }
        leavesBlockstate.put("variants", leavesVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_leaves.json"), leavesBlockstate);

        // Generate sapling blockstate with stage property
        Map<String, Object> saplingBlockstate = Map.of(
            "variants", Map.of(
                "stage=0", Map.of("model", "geco:block/" + woodName + "_sapling"),
                "stage=1", Map.of("model", "geco:block/" + woodName + "_sapling")
            )
        );
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_sapling.json"), saplingBlockstate);

        // Generate slab blockstate
        Map<String, Object> slabBlockstate = generateSlabBlockstate(woodName, woodName + "_planks");
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_slab.json"), slabBlockstate);

        // Generate stairs blockstate
        Map<String, Object> stairsBlockstate = generateStairsBlockstate(woodName);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_stairs.json"), stairsBlockstate);

        // Generate fence blockstate
        Map<String, Object> fenceBlockstate = new HashMap<>();
        List<Map<String, Object>> fenceMultipart = new java.util.ArrayList<>();
        fenceMultipart.add(Map.of("apply", Map.of("model", "geco:block/" + woodName + "_fence_post")));
        fenceMultipart.add(Map.of(
            "when", Map.of("north", "true"),
            "apply", Map.of("model", "geco:block/" + woodName + "_fence_side", "uvlock", true)
        ));
        fenceMultipart.add(Map.of(
            "when", Map.of("east", "true"),
            "apply", Map.of("model", "geco:block/" + woodName + "_fence_side", "y", 90, "uvlock", true)
        ));
        fenceMultipart.add(Map.of(
            "when", Map.of("south", "true"),
            "apply", Map.of("model", "geco:block/" + woodName + "_fence_side", "y", 180, "uvlock", true)
        ));
        fenceMultipart.add(Map.of(
            "when", Map.of("west", "true"),
            "apply", Map.of("model", "geco:block/" + woodName + "_fence_side", "y", 270, "uvlock", true)
        ));
        fenceBlockstate.put("multipart", fenceMultipart);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_fence.json"), fenceBlockstate);

        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_fence_gate.json"), generateFenceGateBlockstate(woodName));

        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_button.json"), generateButtonBlockstate(woodName));

        // Generate pressure plate blockstate
        Map<String, Object> pressurePlateBlockstate = new HashMap<>();
        Map<String, Object> pressurePlateVariants = new HashMap<>();
        pressurePlateVariants.put("powered=false", Map.of("model", "geco:block/" + woodName + "_pressure_plate"));
        pressurePlateVariants.put("powered=true", Map.of("model", "geco:block/" + woodName + "_pressure_plate_down"));
        pressurePlateBlockstate.put("variants", pressurePlateVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_pressure_plate.json"), pressurePlateBlockstate);

        // Generate door blockstate (matching reference exactly)
        Map<String, Object> doorBlockstate = new HashMap<>();
        Map<String, Object> doorVariants = new HashMap<>();

        // East facing
        doorVariants.put("facing=east,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_left"));
        doorVariants.put("facing=east,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_left_open", "y", 90));
        doorVariants.put("facing=east,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_right"));
        doorVariants.put("facing=east,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_right_open", "y", 270));
        doorVariants.put("facing=east,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_left"));
        doorVariants.put("facing=east,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_left_open", "y", 90));
        doorVariants.put("facing=east,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_right"));
        doorVariants.put("facing=east,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_right_open", "y", 270));

        // North facing
        doorVariants.put("facing=north,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_left", "y", 270));
        doorVariants.put("facing=north,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_left_open"));
        doorVariants.put("facing=north,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_right", "y", 270));
        doorVariants.put("facing=north,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_right_open", "y", 180));
        doorVariants.put("facing=north,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_left", "y", 270));
        doorVariants.put("facing=north,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_left_open"));
        doorVariants.put("facing=north,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_right", "y", 270));
        doorVariants.put("facing=north,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_right_open", "y", 180));

        // South facing
        doorVariants.put("facing=south,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_left", "y", 90));
        doorVariants.put("facing=south,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_left_open", "y", 180));
        doorVariants.put("facing=south,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_right", "y", 90));
        doorVariants.put("facing=south,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_right_open"));
        doorVariants.put("facing=south,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_left", "y", 90));
        doorVariants.put("facing=south,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_left_open", "y", 180));
        doorVariants.put("facing=south,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_right", "y", 90));
        doorVariants.put("facing=south,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_right_open"));

        // West facing
        doorVariants.put("facing=west,half=lower,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_left", "y", 180));
        doorVariants.put("facing=west,half=lower,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_left_open", "y", 270));
        doorVariants.put("facing=west,half=lower,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_bottom_right", "y", 180));
        doorVariants.put("facing=west,half=lower,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_bottom_right_open", "y", 90));
        doorVariants.put("facing=west,half=upper,hinge=left,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_left", "y", 180));
        doorVariants.put("facing=west,half=upper,hinge=left,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_left_open", "y", 270));
        doorVariants.put("facing=west,half=upper,hinge=right,open=false", Map.of("model", "geco:block/" + woodName + "_door_top_right", "y", 180));
        doorVariants.put("facing=west,half=upper,hinge=right,open=true", Map.of("model", "geco:block/" + woodName + "_door_top_right_open", "y", 90));

        doorBlockstate.put("variants", doorVariants);
        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_door.json"), doorBlockstate);

        writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + woodName + "_trapdoor.json"), generateTrapdoorBlockstate(woodName));

    }

    public void generateStoneBlockstateFiles(StoneType stone) throws IOException {
        String stoneName = stone.getPath();
        String[] variantNames = {stoneName, "polished_" + stoneName, "polished_" + stoneName + "_bricks", "polished_" + stoneName + "_tiles", "smooth_" + stoneName};

        for (String variantName : variantNames) {
            // Generate base block blockstate
            Map<String, Object> baseBlockstate = Map.of(
                "variants", Map.of("", Map.of("model", "geco:block/" + variantName))
            );
            writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + variantName + ".json"), baseBlockstate);

            // Generate slab blockstate (for stone, double uses the base block)
            Map<String, Object> slabBlockstate = generateSlabBlockstate(variantName, variantName);
            writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + variantName + "_slab.json"), slabBlockstate);

            // Generate stairs blockstate
            Map<String, Object> stairsBlockstate = generateStairsBlockstate(variantName);
            writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + variantName + "_stairs.json"), stairsBlockstate);

            // Generate wall blockstate
            Map<String, Object> wallBlockstate = generateWallBlockstate(variantName);
            writeJsonFile(outputDir.resolve("assets/geco/blockstates/" + variantName + "_wall.json"), wallBlockstate);
        }
    }

    // Helper methods to generate common blockstate patterns
    private Map<String, Object> generateSlabBlockstate(String baseName, String doubleBlockName) {
        return Map.of(
            "variants", Map.of(
                "type=bottom", Map.of("model", "geco:block/" + baseName + "_slab"),
                "type=double", Map.of("model", "geco:block/" + doubleBlockName),
                "type=top", Map.of("model", "geco:block/" + baseName + "_slab_top")
            )
        );
    }

    // Mirrors 1.21.1 BlockModelGenerators createStairs/createButton/createFenceGate/createTrapdoor.
    private Map<String, Object> variant(String model, int x, int y, boolean uvlock) {
        Map<String, Object> value = new HashMap<>();
        value.put("model", "geco:block/" + model);
        if (x != 0) value.put("x", x);
        if (y != 0) value.put("y", y);
        if (uvlock) value.put("uvlock", true);
        return value;
    }

    private Map<String, Object> generateStairsBlockstate(String baseName) {
        Map<String, Object> variants = new HashMap<>();
        String[] directions = {"east", "south", "west", "north"};
        for (int i = 0; i < directions.length; i++) {
            for (String half : new String[]{"bottom", "top"}) {
                boolean top = half.equals("top");
                for (String shape : new String[]{"straight", "inner_left", "inner_right", "outer_left", "outer_right"}) {
                    int y = i * 90;
                    if (shape.endsWith("_left")) y += 270;
                    if (top && !shape.equals("straight")) y += 90;
                    y %= 360;
                    String suffix = shape.startsWith("inner") ? "_inner" : shape.startsWith("outer") ? "_outer" : "";
                    variants.put("facing=" + directions[i] + ",half=" + half + ",shape=" + shape,
                            variant(baseName + "_stairs" + suffix, top ? 180 : 0, y, top || y != 0));
                }
            }
        }
        return Map.of("variants", variants);
    }

    private Map<String, Object> generateFenceGateBlockstate(String baseName) {
        Map<String, Object> variants = new HashMap<>();
        String[] directions = {"south", "west", "north", "east"};
        for (int i = 0; i < directions.length; i++)
            for (boolean wall : new boolean[]{false, true})
                for (boolean open : new boolean[]{false, true})
                    variants.put("facing=" + directions[i] + ",in_wall=" + wall + ",open=" + open,
                            variant(baseName + "_fence_gate" + (wall ? "_wall" : "") + (open ? "_open" : ""), 0, i * 90, true));
        return Map.of("variants", variants);
    }

    private Map<String, Object> generateButtonBlockstate(String baseName) {
        Map<String, Object> variants = new HashMap<>();
        String[] directions = {"north", "east", "south", "west"};
        for (int i = 0; i < directions.length; i++)
            for (String face : new String[]{"floor", "wall", "ceiling"})
                for (boolean powered : new boolean[]{false, true}) {
                    int x = face.equals("wall") ? 90 : face.equals("ceiling") ? 180 : 0;
                    int y = (i * 90 + (face.equals("ceiling") ? 180 : 0)) % 360;
                    variants.put("face=" + face + ",facing=" + directions[i] + ",powered=" + powered,
                            variant(baseName + "_button" + (powered ? "_pressed" : ""), x, y, face.equals("wall")));
                }
        return Map.of("variants", variants);
    }

    private Map<String, Object> generateTrapdoorBlockstate(String baseName) {
        Map<String, Object> variants = new HashMap<>();
        String[] directions = {"north", "east", "south", "west"};
        for (int i = 0; i < directions.length; i++)
            for (String half : new String[]{"bottom", "top"})
                for (boolean open : new boolean[]{false, true})
                    variants.put("facing=" + directions[i] + ",half=" + half + ",open=" + open,
                            variant(baseName + "_trapdoor_" + (open ? "open" : half), 0, open ? i * 90 : 0, false));
        return Map.of("variants", variants);
    }

    private Map<String, Object> generateWallBlockstate(String baseName) {
        Map<String, Object> wallBlockstate = new HashMap<>();
        List<Map<String, Object>> wallMultipart = new java.util.ArrayList<>();

        // Post when up=true
        wallMultipart.add(Map.of(
            "when", Map.of("up", "true"),
            "apply", Map.of("model", "geco:block/" + baseName + "_wall_post")
        ));

        // Low and tall connections for each direction
        String[] directions = {"north", "east", "south", "west"};
        int[] yRotations = {0, 90, 180, 270};

        for (int i = 0; i < directions.length; i++) {
            String direction = directions[i];
            int yRot = yRotations[i];

            wallMultipart.add(Map.of(
                "when", Map.of(direction, "low"),
                "apply", Map.of("model", "geco:block/" + baseName + "_wall_side", "y", yRot, "uvlock", true)
            ));

            wallMultipart.add(Map.of(
                "when", Map.of(direction, "tall"),
                "apply", Map.of("model", "geco:block/" + baseName + "_wall_side_tall", "y", yRot, "uvlock", true)
            ));
        }

        wallBlockstate.put("multipart", wallMultipart);
        return wallBlockstate;
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        CanonicalJson.write(path, data, gson);
    }
}
