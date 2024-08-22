package com.possible_triangle.registry_dump.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface IPlatformHelper {

    record ModInfo(String id, String name, String description, @Nullable String url) {
        public JsonElement toJson() {
            var json = new JsonObject();

            json.addProperty("id", id());
            json.addProperty("name", name());
            if (!description().isBlank()) json.addProperty("description", description());
            if (url != null) json.addProperty("url", url);

            return json;
        }
    }

    Stream<ModInfo> collectMods();

}
