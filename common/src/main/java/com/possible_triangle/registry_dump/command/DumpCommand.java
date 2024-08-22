package com.possible_triangle.registry_dump.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.possible_triangle.registry_dump.CommonClass;
import com.possible_triangle.registry_dump.Services;
import com.sun.jdi.connect.Connector;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Predicate;

public class DumpCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dump").requires(it -> it.hasPermission(3))
                .then(Commands.literal("registry")
                        .executes(ctx -> dumpRegistries(ctx, $ -> true))
                        .then(Commands.argument("type", ResourceKeyArgument.key(BuiltInRegistries.REGISTRY.key()))
                                .executes(ctx -> dumpRegistries(ctx, createArgumentPredicate(ctx)))
                        )
                )
                .then(Commands.literal("mods")
                        .executes(ctx -> dumpMods(ctx, true))
                        .then(Commands.argument("simple", BoolArgumentType.bool())
                                .executes(ctx -> dumpMods(ctx, BoolArgumentType.getBool(ctx, "simple")))
                        )
                )
        );
    }

    private static Predicate<ResourceKey<? extends Registry<?>>> createArgumentPredicate(CommandContext<CommandSourceStack> context) {
        var key = context.getArgument("type", ResourceKey.class);
        return key::equals;
    }

    private static int dumpRegistries(CommandContext<CommandSourceStack> context, Predicate<ResourceKey<? extends Registry<?>>> predicate) throws CommandSyntaxException {
        final var server = context.getSource().getServer();
        final var registries = server.registryAccess().registries()
                .filter(it -> predicate.test(it.key()))
                .toList();
        final var emitter = CommonClass.getDump(server);

        for (var registry : registries) {
            emitter.dump(registry);
        }

        return registries.size();
    }

    private static int dumpMods(CommandContext<CommandSourceStack> context, boolean simple) throws CommandSyntaxException {
        final var server = context.getSource().getServer();
        final var mods = Services.PLATFORM.collectMods().toList();
        final var emitter = CommonClass.getDump(server);

        emitter.dump(mods, simple);

        return mods.size();
    }

}
