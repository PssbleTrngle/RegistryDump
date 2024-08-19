package com.possible_triangle.registry_dump;

import com.possible_triangle.registry_dump.dump.FileDump;
import com.possible_triangle.registry_dump.dump.IDump;
import net.minecraft.server.MinecraftServer;

public class CommonClass {

    public static void init() {
    }

    public static IDump getDump(MinecraftServer server) {
        return new FileDump(server.getServerDirectory().resolve("dump"));
    }

}