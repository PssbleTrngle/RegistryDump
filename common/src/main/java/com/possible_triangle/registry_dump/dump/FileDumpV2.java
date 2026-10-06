package com.possible_triangle.registry_dump.dump;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.possible_triangle.registry_dump.service.IPlatformHelper;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class FileDumpV2 implements IDump {

    private final Path outputDirectory;

    public FileDumpV2(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public <T> void dump(ResourceKey<? extends Registry<T>> key, Stream<? extends Holder<T>> entries) throws CommandSyntaxException {
        final var registryDirectory = outputDirectory.resolve(key.location().getNamespace()).resolve(key.location().getPath());
        final var byNamespace = gatherIds(entries);

        final var metadata = new JsonObject();
        metadata.addProperty("namespace", key.location().getNamespace());
        metadata.addProperty("path", key.location().getPath());
        metadata.addProperty("tags", Registries.tagsDirPath(key));
        write(registryDirectory.resolve(".registry.json"), metadata);

        for (var entry : byNamespace.entrySet()) {
            var ids = entry.getValue();

            final var file = registryDirectory.resolve(entry.getKey() + ".json");
            final var json = new JsonArray(ids.size());
            ids.stream()
                    .map(ResourceLocation::toString)
                    .sorted()
                    .forEach(json::add);
            write(file, json);
        }
    }

    @Override
    public void dump(Collection<IPlatformHelper.ModInfo> mods, boolean simple) throws CommandSyntaxException {
        final var file = outputDirectory.resolve("mods.json");

        final var json = new JsonArray(mods.size());

        mods.forEach(mod -> {
            if (simple) json.add(mod.id());
            else json.add(mod.toJson());
        });

        write(file, json);
    }

}
