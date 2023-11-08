package com.possible_triangle.registry_dump.dump;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

import java.util.stream.Stream;

public interface IDump {

    <T> void dump(ResourceKey<? extends Registry<T>> key, Stream<? extends Holder<T>> entries) throws CommandSyntaxException;

    default <T> void dump(RegistryAccess.RegistryEntry<T> registry) throws CommandSyntaxException {
        dump(registry.key(), registry.value().holders());
    }

}
