package com.nstut.geco.datagen;

import com.google.gson.Gson;
import com.nstut.geco.common.wood.WoodType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GecoLootTableGenerator {
    private final Path outputDir;
    private final Gson gson;

    public GecoLootTableGenerator(Path outputDir, Gson gson) {
        this.outputDir = outputDir;
        this.gson = gson;
    }

    public void generateLootTableFiles(WoodType wood) throws IOException {
        String woodName = wood.getPath();
        // Generate log loot table
        Map<String, Object> logLootTable = Map.of(
            "type", "minecraft:block",
            "pools", List.of(Map.of(
                "bonus_rolls", 0.0,
                "conditions", List.of(Map.of(
                    "condition", "minecraft:survives_explosion"
                )),
                "entries", List.of(Map.of(
                    "type", "minecraft:item",
                    "name", "geco:" + woodName + "_log"
                )),
                "rolls", 1.0
            ))
        );
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_log.json"), logLootTable);

        // Generate simple block loot tables (planks, wood, stripped_log, stripped_wood, sapling, stairs, pressure_plate)
        List<String> simpleBlocks = Arrays.asList(
            "planks", "wood", "stripped_log", "stripped_wood", "sapling", "stairs", "pressure_plate"
        );
        for (String block : simpleBlocks) {
            Map<String, Object> lootTable = Map.of(
                "type", "minecraft:block",
                "pools", List.of(Map.of(
                    "rolls", 1.0,
                    "entries", List.of(Map.of(
                        "type", "minecraft:item",
                        "name", "geco:" + woodName + "_" + block
                    )),
                    "conditions", List.of(Map.of(
                        "condition", "minecraft:survives_explosion"
                    ))
                ))
            );
            writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_" + block + ".json"), lootTable);
        }

        // Generate leaves loot table (complex)
        generateLeavesLootTable(woodName);

        // Generate slab loot table
        Map<String, Object> slabLootTable = new HashMap<>();
        slabLootTable.put("type", "minecraft:block");
        
        Map<String, Object> slabPool = new HashMap<>();
        slabPool.put("rolls", 1.0);
        
        List<Map<String, Object>> slabEntries = new java.util.ArrayList<>();
        Map<String, Object> slabItemEntry = new HashMap<>();
        slabItemEntry.put("type", "minecraft:item");
        slabItemEntry.put("name", "geco:" + woodName + "_slab");
        
        List<Map<String, Object>> slabFunctions = new java.util.ArrayList<>();
        Map<String, Object> setCountFunction = new HashMap<>();
        setCountFunction.put("function", "minecraft:set_count");
        
        List<Map<String, Object>> setCountConditions = new java.util.ArrayList<>();
        Map<String, Object> blockStatePropertyCondition = new HashMap<>();
        blockStatePropertyCondition.put("condition", "minecraft:block_state_property");
        blockStatePropertyCondition.put("block", "geco:" + woodName + "_slab");
        blockStatePropertyCondition.put("properties", Map.of("type", "double"));
        setCountConditions.add(blockStatePropertyCondition);
        setCountFunction.put("conditions", setCountConditions);
        setCountFunction.put("count", 2);
        slabFunctions.add(setCountFunction);
        
        Map<String, Object> explosionDecayFunction = new HashMap<>();
        explosionDecayFunction.put("function", "minecraft:explosion_decay");
        slabFunctions.add(explosionDecayFunction);
        
        slabItemEntry.put("functions", slabFunctions);
        slabEntries.add(slabItemEntry);
        slabPool.put("entries", slabEntries);
        
        List<Map<String, Object>> slabConditions = new java.util.ArrayList<>();
        slabConditions.add(Map.of("condition", "minecraft:survives_explosion"));
        slabPool.put("conditions", slabConditions);
        
        slabLootTable.put("pools", List.of(slabPool));
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_slab.json"), slabLootTable);

        // Generate fence loot table
        Map<String, Object> fenceLootTable = Map.of(
            "type", "minecraft:block",
            "pools", List.of(Map.of(
                "rolls", 1.0,
                "entries", List.of(Map.of(
                    "type", "minecraft:item",
                    "name", "geco:" + woodName + "_fence"
                )),
                "conditions", List.of(Map.of(
                    "condition", "minecraft:survives_explosion"
                ))
            ))
        );
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_fence.json"), fenceLootTable);

        // Generate fence gate loot table
        Map<String, Object> fenceGateLootTable = Map.of(
            "type", "minecraft:block",
            "pools", List.of(Map.of(
                "rolls", 1.0,
                "entries", List.of(Map.of(
                    "type", "minecraft:item",
                    "name", "geco:" + woodName + "_fence_gate"
                )),
                "conditions", List.of(Map.of(
                    "condition", "minecraft:survives_explosion"
                ))
            ))
        );
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_fence_gate.json"), fenceGateLootTable);

        // Generate button loot table
        Map<String, Object> buttonLootTable = Map.of(
            "type", "minecraft:block",
            "pools", List.of(Map.of(
                "rolls", 1.0,
                "entries", List.of(Map.of(
                    "type", "minecraft:item",
                    "name", "geco:" + woodName + "_button"
                )),
                "conditions", List.of(Map.of(
                    "condition", "minecraft:survives_explosion"
                ))
            ))
        );
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_button.json"), buttonLootTable);

        // Generate door loot table
        Map<String, Object> doorLootTable = Map.of(
            "type", "minecraft:block",
            "pools", List.of(Map.of(
                "rolls", 1.0,
                "entries", List.of(Map.of(
                    "type", "minecraft:item",
                    "name", "geco:" + woodName + "_door"
                )),
                "conditions", List.of(Map.of(
                    "condition", "minecraft:survives_explosion"
                ))
            ))
        );
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_door.json"), doorLootTable);

        // Generate trapdoor loot table
        Map<String, Object> trapdoorLootTable = Map.of(
            "type", "minecraft:block",
            "pools", List.of(Map.of(
                "rolls", 1.0,
                "entries", List.of(Map.of(
                    "type", "minecraft:item",
                    "name", "geco:" + woodName + "_trapdoor"
                )),
                "conditions", List.of(Map.of(
                    "condition", "minecraft:survives_explosion"
                ))
            ))
        );
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_trapdoor.json"), trapdoorLootTable);
    }

    private void generateLeavesLootTable(String woodName) throws IOException {
        // Create the complex leaves loot table matching the manual version
        Map<String, Object> leavesLootTable = new HashMap<>();
        leavesLootTable.put("type", "minecraft:block");
        leavesLootTable.put("random_sequence", "geco:blocks/" + woodName + "_leaves");
        
        List<Map<String, Object>> pools = new java.util.ArrayList<>();
        
        // First pool: Drop leaves with shears/silk touch, or saplings with fortune
        Map<String, Object> firstPool = new HashMap<>();
        firstPool.put("bonus_rolls", 0);
        firstPool.put("rolls", 1);
        
        List<Map<String, Object>> firstPoolEntries = new java.util.ArrayList<>();
        Map<String, Object> alternativesEntry = new HashMap<>();
        alternativesEntry.put("type", "minecraft:alternatives");
        
        List<Map<String, Object>> alternativeChildren = new java.util.ArrayList<>();
        
        // Leaves with shears or silk touch
        Map<String, Object> leavesWithTools = new HashMap<>();
        leavesWithTools.put("type", "minecraft:item");
        leavesWithTools.put("name", "geco:" + woodName + "_leaves");
        
        List<Map<String, Object>> leavesConditions = new java.util.ArrayList<>();
        Map<String, Object> anyOfCondition = new HashMap<>();
        anyOfCondition.put("condition", "minecraft:any_of");
        
        List<Map<String, Object>> anyOfTerms = new java.util.ArrayList<>();
        
        // Shears condition
        Map<String, Object> shearsCondition = new HashMap<>();
        shearsCondition.put("condition", "minecraft:match_tool");
        shearsCondition.put("predicate", Map.of("items", "minecraft:shears"));
        anyOfTerms.add(shearsCondition);
        
        // Silk touch condition
        Map<String, Object> silkTouchCondition = new HashMap<>();
        silkTouchCondition.put("condition", "minecraft:match_tool");
        silkTouchCondition.put("predicate", Map.of(
            "predicates", Map.of(
                "minecraft:enchantments", List.of(Map.of(
                    "enchantments", "minecraft:silk_touch",
                    "levels", Map.of("min", 1)
                ))
            )
        ));
        anyOfTerms.add(silkTouchCondition);
        
        anyOfCondition.put("terms", anyOfTerms);
        leavesConditions.add(anyOfCondition);
        leavesWithTools.put("conditions", leavesConditions);
        alternativeChildren.add(leavesWithTools);
        
        // Saplings with fortune
        Map<String, Object> saplingDrop = new HashMap<>();
        saplingDrop.put("type", "minecraft:item");
        saplingDrop.put("name", "geco:" + woodName + "_sapling");
        
        List<Map<String, Object>> saplingConditions = new java.util.ArrayList<>();
        saplingConditions.add(Map.of("condition", "minecraft:survives_explosion"));
        
        Map<String, Object> fortuneCondition = new HashMap<>();
        fortuneCondition.put("condition", "minecraft:table_bonus");
        fortuneCondition.put("enchantment", "minecraft:fortune");
        fortuneCondition.put("chances", Arrays.asList(0.05, 0.0625, 0.083333336, 0.1));
        saplingConditions.add(fortuneCondition);
        
        saplingDrop.put("conditions", saplingConditions);
        alternativeChildren.add(saplingDrop);
        
        alternativesEntry.put("children", alternativeChildren);
        firstPoolEntries.add(alternativesEntry);
        firstPool.put("entries", firstPoolEntries);
        pools.add(firstPool);
        
        // Second pool: Sticks when not using shears/silk touch
        Map<String, Object> secondPool = new HashMap<>();
        secondPool.put("bonus_rolls", 0);
        secondPool.put("rolls", 1);
        
        List<Map<String, Object>> secondPoolConditions = new java.util.ArrayList<>();
        Map<String, Object> invertedCondition = new HashMap<>();
        invertedCondition.put("condition", "minecraft:inverted");
        
        Map<String, Object> invertedTerm = new HashMap<>();
        invertedTerm.put("condition", "minecraft:any_of");
        invertedTerm.put("terms", anyOfTerms); // Reuse the same terms
        invertedCondition.put("term", invertedTerm);
        secondPoolConditions.add(invertedCondition);
        secondPool.put("conditions", secondPoolConditions);
        
        List<Map<String, Object>> secondPoolEntries = new java.util.ArrayList<>();
        Map<String, Object> stickEntry = new HashMap<>();
        stickEntry.put("type", "minecraft:item");
        stickEntry.put("name", "minecraft:stick");
        
        List<Map<String, Object>> stickConditions = new java.util.ArrayList<>();
        Map<String, Object> stickFortuneCondition = new HashMap<>();
        stickFortuneCondition.put("condition", "minecraft:table_bonus");
        stickFortuneCondition.put("enchantment", "minecraft:fortune");
        stickFortuneCondition.put("chances", Arrays.asList(0.02, 0.022222223, 0.025, 0.033333335, 0.1));
        stickConditions.add(stickFortuneCondition);
        stickEntry.put("conditions", stickConditions);
        
        List<Map<String, Object>> stickFunctions = new java.util.ArrayList<>();
        Map<String, Object> setCountFunction = new HashMap<>();
        setCountFunction.put("function", "minecraft:set_count");
        setCountFunction.put("add", false);
        setCountFunction.put("count", Map.of(
            "type", "minecraft:uniform",
            "min", 1,
            "max", 2
        ));
        stickFunctions.add(setCountFunction);
        
        Map<String, Object> explosionDecayFunction = new HashMap<>();
        explosionDecayFunction.put("function", "minecraft:explosion_decay");
        stickFunctions.add(explosionDecayFunction);
        
        stickEntry.put("functions", stickFunctions);
        secondPoolEntries.add(stickEntry);
        secondPool.put("entries", secondPoolEntries);
        pools.add(secondPool);
        
        leavesLootTable.put("pools", pools);
        writeJsonFile(outputDir.resolve("data/geco/loot_tables/blocks/" + woodName + "_leaves.json"), leavesLootTable);
    }

    private void writeJsonFile(Path path, Object data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(data));
    }
}