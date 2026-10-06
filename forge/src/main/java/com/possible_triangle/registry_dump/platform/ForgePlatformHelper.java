package com.possible_triangle.registry_dump.platform;

import com.possible_triangle.registry_dump.service.IPlatformHelper;
import net.minecraftforge.fml.ModList;

import java.util.stream.Stream;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public Stream<ModInfo> collectMods() {
        return ModList.get().applyForEachModContainer(it -> new ModInfo(
                it.getModId(),
                it.getModInfo().getDisplayName(),
                it.getModInfo().getDescription(),
                it.getModInfo().getModURL().toString()
        ));
    }

}
