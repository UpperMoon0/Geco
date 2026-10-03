package com.nstut.geco.datagen;

import com.google.gson.*;
import com.nstut.geco.common.stone.StoneType;
import com.nstut.geco.common.wood.WoodType;
import com.nstut.geco.common.worldgen.TemplateTreePlacement;
import java.nio.file.*;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class GecoDataGeneratorTest {
    @TempDir Path output;
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    final WoodType ebony = new WoodType(ResourceLocation.parse("geco:ebony"), Set.of(5));
    JsonObject read(String path) throws Exception { return JsonParser.parseString(Files.readString(output.resolve(path))).getAsJsonObject(); }
    @Test void allWoodAndStoneStairsMatchEveryVanillaVariant() throws Exception {
        var generator = new GecoBlockstateGenerator(output, gson);
        generator.generateWoodBlockstateFiles(ebony);
        for (String name : new String[]{"cream_marble", "multicolor_marble"}) {
            generator.generateStoneBlockstateFiles(new StoneType(ResourceLocation.parse("geco:" + name)));
            for (String base : new String[]{name, "polished_" + name, "polished_" + name + "_bricks",
                    "polished_" + name + "_tiles", "smooth_" + name})
                assertVanilla("stairs", base);
        }
        assertVanilla("stairs", "ebony");
    }
    @Test void gatesButtonsAndTrapdoorsMatchAllVanillaVariants() throws Exception {
        new GecoBlockstateGenerator(output, gson).generateWoodBlockstateFiles(ebony);
        for (String kind : new String[]{"fence_gate", "button", "trapdoor"}) assertVanilla(kind, "ebony");
    }
    void assertVanilla(String kind, String base) throws Exception {
        try (var stream = getClass().getResourceAsStream("/assets/minecraft/blockstates/oak_" + kind + ".json")) {
            assertNotNull(stream, "Vanilla assets must be on the test classpath");
            String vanilla = new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8)
                    .replace("minecraft:block/oak", "geco:block/" + base);
            assertEquals(JsonParser.parseString(vanilla), read("assets/geco/blockstates/" + base + "_" + kind + ".json"),
                    base + "_" + kind);
        }
    }
    @Test void strippedLootUsesTheRegisteredIdsAndHasNoObsoleteTables() throws Exception {
        new GecoLootTableGenerator(output, gson).generateWoodLootTableFiles(ebony);
        for (String part : new String[]{"log", "wood"}) {
            var table = read("data/geco/loot_table/blocks/stripped_ebony_" + part + ".json");
            assertEquals("geco:stripped_ebony_" + part, table.getAsJsonArray("pools").get(0).getAsJsonObject()
                    .getAsJsonArray("entries").get(0).getAsJsonObject().get("name").getAsString());
            assertFalse(Files.exists(output.resolve("data/geco/loot_table/blocks/ebony_stripped_" + part + ".json")));
        }
    }
    @Test void doorsOnlyDropFromTheLowerHalf() throws Exception {
        new GecoLootTableGenerator(output, gson).generateWoodLootTableFiles(ebony);
        var entry = read("data/geco/loot_table/blocks/ebony_door.json").getAsJsonArray("pools").get(0).getAsJsonObject()
                .getAsJsonArray("entries").get(0).getAsJsonObject();
        var condition = entry.getAsJsonArray("conditions").get(0).getAsJsonObject();
        assertEquals("minecraft:block_state_property", condition.get("condition").getAsString());
        assertEquals("lower", condition.getAsJsonObject("properties").get("half").getAsString());
    }
    @Test void woodAndStoneSlabsOnlyApplyExplosionDecayOnce() throws Exception {
        var generator = new GecoLootTableGenerator(output, gson);
        generator.generateWoodLootTableFiles(ebony);
        generator.generateStoneLootTableFiles(new StoneType(ResourceLocation.parse("geco:cream_marble")));
        for (String name : new String[]{"ebony", "cream_marble"}) {
            var pool = read("data/geco/loot_table/blocks/" + name + "_slab.json").getAsJsonArray("pools").get(0).getAsJsonObject();
            assertFalse(pool.has("conditions"));
            var functions = pool.getAsJsonArray("entries").get(0).getAsJsonObject().getAsJsonArray("functions");
            assertEquals(2, functions.size());
            assertEquals(2, functions.get(0).getAsJsonObject().get("count").getAsInt());
            assertEquals("minecraft:explosion_decay", functions.get(1).getAsJsonObject().get("function").getAsString());
        }
    }
    @Test void trapdoorsCraftTwoAndSaplingsAndLeavesHaveVanillaTags() throws Exception {
        new GecoRecipeGenerator(output, gson).generateWoodRecipeFiles(ebony);
        assertEquals(2, read("data/geco/recipe/ebony_trapdoor.json").getAsJsonObject("result").get("count").getAsInt());
        var tags = new GecoTagGenerator(output, gson);
        tags.generateMinecraftTagFiles(ebony);
        for (String kind : new String[]{"block", "item"})
            assertEquals("geco:ebony_sapling", read("data/minecraft/tags/" + kind + "/saplings.json").getAsJsonArray("values").get(0).getAsString());
        assertEquals("geco:ebony_leaves", read("data/minecraft/tags/block/mineable/hoe.json").getAsJsonArray("values").get(0).getAsString());
    }
    @Test void regeneratingProducesExactlyTheSameBytes() throws Exception {
        var generator = new GecoBlockstateGenerator(output, gson);
        generator.generateWoodBlockstateFiles(ebony);
        var first = Files.readString(output.resolve("assets/geco/blockstates/ebony_stairs.json"));
        generator.generateWoodBlockstateFiles(ebony);
        assertEquals(first, Files.readString(output.resolve("assets/geco/blockstates/ebony_stairs.json")));
    }
    @Test void outputErrorsPropagate() throws Exception {
        Files.writeString(output.resolve("assets"), "blocked");
        assertThrows(java.io.IOException.class, () -> new GecoBlockstateGenerator(output, gson).generateWoodBlockstateFiles(ebony));
    }
    @Test void saplingsAndNaturalTreesShareTheSameAllowedTemplates() {
        assertEquals(java.util.List.of("geco:ebony_tree_m1", "geco:ebony_tree_m2", "geco:ebony_tree_m3", "geco:ebony_tree_m4"),
                TemplateTreePlacement.templates(ebony).stream().map(Object::toString).toList());
    }

    java.util.Map<String, String> snapshot(Path root) throws Exception {
        try (var paths = Files.walk(root)) {
            var result = new java.util.TreeMap<String, String>();
            for (var path : paths.filter(Files::isRegularFile).toList())
                result.put(root.relativize(path).toString(), Files.readString(path));
            return result;
        }
    }
    @Test void entireGeneratorMatchesEveryCheckedInFileAndIsIdempotent() throws Exception {
        var stones = java.util.List.of(
                new StoneType(ResourceLocation.parse("geco:cream_marble")),
                new StoneType(ResourceLocation.parse("geco:multicolor_marble")));
        GecoDataGenerator.generate(output, java.util.List.of(ebony), stones);
        var first = snapshot(output);
        assertEquals(421, first.size());
        assertEquals(snapshot(Path.of("src/generated/resources")), first);
        GecoDataGenerator.generate(output, java.util.List.of(ebony), stones);
        assertEquals(first, snapshot(output));
    }
    @Test void malformedExistingTagsFailWithoutSilentlyLosingMembers() throws Exception {
        Path path = output.resolve("data/minecraft/tags/block/doors.json");
        Files.createDirectories(path.getParent());
        for (String data : java.util.List.of("{", "null", "{}", "{\"values\":null}", "{\"values\":[7]}")) {
            Files.writeString(path, data);
            assertThrows(java.io.IOException.class,
                    () -> new GecoTagGenerator(output, gson).generateMinecraftTagFiles(ebony), data);
            assertEquals(data, Files.readString(path));
        }
    }
    @Test void existingTagMembersArePreservedAndNewMembersAreNotDuplicated() throws Exception {
        Path path = output.resolve("data/minecraft/tags/block/doors.json");
        Files.createDirectories(path.getParent());
        Files.writeString(path, "{\"replace\":false,\"values\":[\"minecraft:oak_door\",\"geco:ebony_door\"]}");
        var generator = new GecoTagGenerator(output, gson);
        generator.generateMinecraftTagFiles(ebony);
        generator.generateMinecraftTagFiles(ebony);
        assertEquals(JsonParser.parseString("[\"minecraft:oak_door\",\"geco:ebony_door\"]"),
                read("data/minecraft/tags/block/doors.json").get("values"));
    }
}
