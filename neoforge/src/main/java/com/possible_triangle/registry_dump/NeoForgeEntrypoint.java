package com.possible_triangle.registry_dump;

import com.possible_triangle.registry_dump.command.DumpCommand;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class NeoForgeEntrypoint {

    public NeoForgeEntrypoint() {
        CommonClass.init();

        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            DumpCommand.register(event.getDispatcher());
        });
    }
}