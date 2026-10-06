package com.possible_triangle.registry_dump.dump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.possible_triangle.registry_dump.service.IPlatformHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public interface IDump {

    Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    DynamicCommandExceptionType FAILED_WRITE = new DynamicCommandExceptionType(it -> Component.literal("Failed to write to ").append(it.toString()));
    DynamicCommandExceptionType FAILED_CREATE = new DynamicCommandExceptionType(it -> Component.literal("Failed to create ").append(it.toString()));

    int LATEST_VERSION = 2;

    static IDump get(MinecraftServer server, int version) throws CommandSyntaxException {
        var outputDirectory = server.getServerDirectory().resolve("dump");
        prepare(outputDirectory, version);
        return switch (version) {
            case 1 -> new FileDumpV1(outputDirectory);
            case 2 -> new FileDumpV2(outputDirectory);
            default -> throw new IllegalArgumentException("unknown dump version '%s'".formatted(version));
        };
    }

    void dump(Collection<IPlatformHelper.ModInfo> mods, boolean simple) throws CommandSyntaxException;

    <T> void dump(ResourceKey<? extends Registry<T>> key, Stream<? extends Holder<T>> entries) throws CommandSyntaxException;

    default <T> void dump(RegistryAccess.RegistryEntry<T> registry) throws CommandSyntaxException {
        dump(registry.key(), registry.value().holders());
    }

    static Map<String, Collection<ResourceLocation>> gatherIds(Stream<? extends Holder<?>> entries) {
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

    static void write(Path path, JsonElement json) throws CommandSyntaxException {
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

    private static void prepare(Path path, int version) throws CommandSyntaxException {
        var metadata = new JsonObject();
        metadata.addProperty("version", version);
        final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        metadata.addProperty("timestamp", LocalDateTime.now().format(formatter));
        write(path.resolve(".metadata.json"), metadata);
    }

}
