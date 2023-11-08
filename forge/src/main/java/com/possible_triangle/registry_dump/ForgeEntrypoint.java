package com.possible_triangle.registry_dump;

import com.possible_triangle.registry_dump.command.DumpCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        CommonClass.init();

        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            DumpCommand.register(event.getDispatcher());
        });
    }
}