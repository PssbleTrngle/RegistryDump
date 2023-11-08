package com.possible_triangle.registry_dump;

import com.possible_triangle.registry_dump.command.DumpCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                DumpCommand.register(dispatcher)
        );

    }
}
