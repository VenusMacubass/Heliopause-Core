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
import net.venera.heliocore.screen.hpc_custom.*;

public class HpCMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, HeliopauseCore.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CoalCompressorMenu>> COAL_COMPRESSOR_MENU =
            registerMenuType("coal_compressor_menu", CoalCompressorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<RefineryMenu>> REFINERY_MENU =
            registerMenuType("refinery_menu", RefineryMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<EnergyStorageUnitMenu>> ENERGY_STORAGE_UNIT_MENU =
            registerMenuType("energy_storage_unit_menu", EnergyStorageUnitMenu::new);
    
    public static final DeferredHolder<MenuType<?>, MenuType<BasicSolarMenu>> BASIC_SOLAR_MENU =
            registerMenuType("basic_solar_menu", BasicSolarMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<RocketMenu>> ROCKET_MENU =
            registerMenuType("rocket_menu", RocketMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<LanderMenu>> LANDER_MENU =
            registerMenuType("lander_menu", LanderMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<CargoManagerMenu>> CARGO_MANAGER_MENU =
            registerMenuType("cargo_manager_menu", CargoManagerMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<FuelManagerMenu>> FUEL_MANAGER_MENU =
            registerMenuType("fuel_manager_menu", FuelManagerMenu::new);
    
    public static final DeferredHolder<MenuType<?>, MenuType<OxygenGeneratorMenu>> OXYGEN_GENERATOR_MENU =
            registerMenuType("oxygen_generator_menu", OxygenGeneratorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<GasCompressorMenu>> GAS_COMPRESSOR_MENU =
            registerMenuType("gas_compressor_menu", GasCompressorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<VaporizerMenu>> VAPORIZER_MENU =
            registerMenuType("vaporizer_menu", VaporizerMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<EnergyGeneratorMenu>> ENERGY_GENERATOR_MENU =
            registerMenuType("energy_generator_menu", EnergyGeneratorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<OxygenSealerMenu>> OXYGEN_SEALER_MENU =
            registerMenuType("oxygen_sealer_menu", OxygenSealerMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
