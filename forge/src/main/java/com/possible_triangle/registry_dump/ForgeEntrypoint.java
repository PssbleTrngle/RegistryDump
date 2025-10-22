package com.possible_triangle.registry_dump;

import com.possible_triangle.registry_dump.command.DumpCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        CommonClass.init();

        RegisterCommandsEvent.BUS.addListener((RegisterCommandsEvent event) -> {
            DumpCommand.register(event.getDispatcher());
        });
    }
}