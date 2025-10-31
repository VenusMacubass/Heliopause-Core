package net.venera.galacticraftcore.item.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.render.FuelCanisterRenderer;

import java.util.function.Consumer;

public class FuelCanister extends Item {
    public static final int CANISTER_MAX_FUEL = 1000;
    IntegerProperty CANISTER_FUEL = IntegerProperty.create("canister_fuel", 0, 5);
    ResourceLocation CANISTER_TEXTURE = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "textures/item/gcc_fuel_canister.png");

    public FuelCanister(Properties properties) {
        super(properties);
    }

    
}
