package net.venera.heliocore.screen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.screen.custom.BasicSolarMenu;
import net.venera.heliocore.screen.custom.CoalCompressorMenu;
import net.venera.heliocore.screen.custom.EnergyStorageUnitMenu;
import net.venera.heliocore.screen.custom.RefineryMenu;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, HeliopauseCore.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CoalCompressorMenu>> COAL_COMPRESSOR_MENU =
            registerMenuType("coal_compressor_menu", CoalCompressorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<RefineryMenu>> REFINERY_MENU =
            registerMenuType("refinery_menu", RefineryMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<EnergyStorageUnitMenu>> ENERGY_STORAGE_UNIT_MENU =
            registerMenuType("energy_storage_unit_menu", EnergyStorageUnitMenu::new);
    
    public static final DeferredHolder<MenuType<?>, MenuType<BasicSolarMenu>> BASIC_SOLAR_MENU =
            registerMenuType("basic_solar_menu", BasicSolarMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
