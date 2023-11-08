package com.possible_triangle.registry_dump.dump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class FileDump implements IDump {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final DynamicCommandExceptionType FAILED_WRITE = new DynamicCommandExceptionType(it -> Component.literal("Failed to write to {}").append(it.toString()));
    private static final DynamicCommandExceptionType FAILED_CREATE = new DynamicCommandExceptionType(it -> Component.literal("Failed to create {}").append(it.toString()));

    private final Path outputDirectory;

    public FileDump(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public <T> void dump(ResourceKey<? extends Registry<T>> key, Stream<? extends Holder<T>> entries) throws CommandSyntaxException {
        final var registryDirectory = outputDirectory.resolve(key.location().getPath());
        final var byNamespace = gatherIds(entries);

        for(var entry : byNamespace.entrySet()) {
            var ids = entry.getValue();

            final var file = registryDirectory.resolve(entry.getKey() + ".json");
            final var json = new JsonArray(ids.size());
            ids.forEach(it -> json.add(it.toString()));
            write(file, json);
        }
    }

    private Map<String, Collection<ResourceLocation>> gatherIds(Stream<? extends Holder<?>> entries) {
        final var byNamespace = new HashMap<String, Collection<ResourceLocation>>();

        final var ids = entries.map(Holder::unwrapKey)
                .map(Optional::orElseThrow)
                .map(ResourceKey::location);

        ids.forEach(it -> {
            final var list = byNamespace.computeIfAbsent(it.getNamespace(), $ -> new HashSet<>());
            list.add(it);
        });

        return byNamespace;
    }

    private void write(Path path, JsonElement json) throws CommandSyntaxException {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                throw FAILED_CREATE.create(path);
            }
        }

        try (var writer = Files.newBufferedWriter(path)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            throw FAILED_WRITE.create(path);
        }
    }

}
