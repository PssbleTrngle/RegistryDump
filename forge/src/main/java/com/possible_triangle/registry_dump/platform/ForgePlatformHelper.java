package com.possible_triangle.registry_dump.platform;

import com.possible_triangle.registry_dump.service.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.net.URL;
import java.util.stream.Stream;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public Stream<ModInfo> collectMods() {
        return ModList.get().getMods().stream().map(this::wrapMod);
    }

    private ModInfo wrapMod(IModInfo mod) {
        return new ModInfo(
                mod.getModId(),
                mod.getDisplayName(),
                mod.getDescription(),
                mod.getModURL().map(URL::toString).orElse(null)
        );
    }
}
