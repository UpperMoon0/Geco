package com.nstut.geco.datagen;

import com.google.gson.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

/** Stable object-key order; array order is semantic and must be preserved. */
final class CanonicalJson {
    private CanonicalJson() {}
    static void write(Path path, Object value, Gson gson) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, gson.toJson(sort(gson.toJsonTree(value))) + "\n");
    }
    private static JsonElement sort(JsonElement value) {
        if (value.isJsonObject()) {
            JsonObject result = new JsonObject();
            new TreeMap<>(value.getAsJsonObject().asMap()).forEach((key, child) -> result.add(key, sort(child)));
            return result;
        }
        if (value.isJsonArray()) {
            JsonArray result = new JsonArray();
            value.getAsJsonArray().forEach(child -> result.add(sort(child)));
            return result;
        }
        return value;
    }
}
