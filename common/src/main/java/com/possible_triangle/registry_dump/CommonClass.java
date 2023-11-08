package com.possible_triangle.registry_dump;

import com.google.gson.Gson;
import com.possible_triangle.registry_dump.dump.FileDump;
import com.possible_triangle.registry_dump.dump.IDump;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.Deserializers;

public class CommonClass {

    public static final Gson LOOT_GSON = Deserializers.createFunctionSerializer().create();

    public static void init() {
    }

    public static IDump getDump(MinecraftServer server) {
        return new FileDump(server.getServerDirectory().toPath().resolve("dump"));
    }

}