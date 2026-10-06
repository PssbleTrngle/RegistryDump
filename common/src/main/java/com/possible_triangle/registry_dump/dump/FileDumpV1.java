package com.possible_triangle.registry_dump.dump;

import com.google.gson.JsonArray;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.possible_triangle.registry_dump.service.IPlatformHelper;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class FileDumpV1 implements IDump {

    private final Path outputDirectory;

    public FileDumpV1(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public <T> void dump(ResourceKey<? extends Registry<T>> key, Stream<? extends Holder<T>> entries) throws CommandSyntaxException {
        final var registryDirectory = outputDirectory.resolve(key.location().getPath());
        final var byNamespace = IDump.gatherIds(entries);

        for(var entry : byNamespace.entrySet()) {
            var ids = entry.getValue();

            final var file = registryDirectory.resolve(entry.getKey() + ".json");
            final var json = new JsonArray(ids.size());
            ids.stream()
                    .map(ResourceLocation::toString)
                    .sorted()
                    .forEach(json::add);
            IDump.write(file, json);
        }
    }

    @Override
    public void dump(Collection<IPlatformHelper.ModInfo> mods, boolean simple) throws CommandSyntaxException {
        final var file = outputDirectory.resolve("mods.json");

        final var json = new JsonArray(mods.size());

        mods.forEach(mod -> {
            if(simple) json.add(mod.id());
            else json.add(mod.toJson());
        });

        IDump.write(file, json);
    }

}
