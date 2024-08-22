package com.possible_triangle.registry_dump.platform;

import com.possible_triangle.registry_dump.service.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.net.URL;
import java.util.stream.Stream;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Stream<ModInfo> collectMods() {
        return FabricLoader.getInstance().getAllMods().stream().map(this::wrapMod);
    }

    private ModInfo wrapMod(ModContainer mod) {
        var metadata = mod.getMetadata();
        return new ModInfo(
                metadata.getId(),
                metadata.getName(),
                metadata.getDescription(),
                null
        );
    }
}
