package com.possible_triangle.registry_dump.platform;

import com.possible_triangle.registry_dump.Constants;
import com.possible_triangle.registry_dump.platform.services.IPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    public static final DeferredRegister<LootItemFunctionType> ITEM_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Constants.MOD_ID);

    @Override
    public Supplier<LootItemFunctionType> registerLootFunction(String id, Supplier<Serializer<? extends LootItemFunction>> serializer) {
        return ITEM_FUNCTIONS.register(id, () -> new LootItemFunctionType(serializer.get()));
    }

}
