package net.venera.heliocore.data.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import java.util.function.UnaryOperator;

public class HpCDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, HeliopauseCore.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> COORDINATES = register("coordinates",
            builder -> builder.persistent(BlockPos.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CanisterData>> CANISTER_COMPONENT = register("canister_content",
            builder -> builder.persistent(CanisterData.CODEC).networkSynchronized(CanisterData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BatteryData>> BATTERY_COMPONENT = register("battery_content",
            builder -> builder.persistent(BatteryData.CODEC).networkSynchronized(BatteryData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GasTankData>> GAS_TANK_COMPONENT = register("gas_tank_content",
            builder -> builder.persistent(GasTankData.CODEC).networkSynchronized(GasTankData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PIZZA_TOPPINGS = register("pizza_toppings",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BURGER_CHEESE = register("burger_cheese",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> BURGER_VEGGIE = register("burger_veggie",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    
    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }
    public static void register(IEventBus bus) {
    DATA_COMPONENT_TYPES.register(bus);
}
}