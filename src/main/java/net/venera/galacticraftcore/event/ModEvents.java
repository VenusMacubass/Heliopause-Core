package net.venera.galacticraftcore.event;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.block.custom.machine.electric.WireBlock;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import net.venera.galacticraftcore.block.entity.machine.electric.BaseElectricMachineEntity;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.item.custom.CanisterItem;
import net.venera.galacticraftcore.item.custom.TempSword;

import java.util.HashSet;
import java.util.Set;
@EventBusSubscriber
public class ModEvents {
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    @SubscribeEvent
    public static void onShwordUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if(mainHandItem.getItem() instanceof TempSword shword && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if(HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }

            for(BlockPos pos : TempSword.getBlocksToBeDestroyed(1, initialBlockPos, serverPlayer)) {
                if(pos == initialBlockPos || !shword.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }
                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void onModelEvent(ModelEvent.RegisterAdditional event) {
        // Register custom item properties
        ItemProperties.register(ModItems.CANISTER.get(),
                ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "fluid_type"),
                (stack, level, entity, seed) -> {
                    CanisterData data = ((CanisterItem) stack.getItem()).getCanisterData(stack);
                    if (data == null || data.isEmpty()) return 0f;
                    return data.isCrudeOil() ? 1f : 2f;
                });

        ItemProperties.register(ModItems.CANISTER.get(),
                ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "fill_level"),
                (stack, level, entity, seed) -> {
                    CanisterData data = ((CanisterItem) stack.getItem()).getCanisterData(stack);
                    if (data == null || data.isEmpty()) return 0f;
                    return data.amount() / (float) CanisterItem.MAX_CAPACITY;
                });
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // 1. Register Energy Storage
        registerElectric(event, ModBlockEntities.ENERGY_STORAGE_ENTITY.get());

        // 2. Register Refinery
        registerElectric(event, ModBlockEntities.REFINERY_ENTITY.get());

        // 3. Register Solar Panel (and any future machines)
        registerElectric(event, ModBlockEntities.BASIC_SOLAR_PANEL_ENTITY.get());
    }
    
    private static void registerElectric(RegisterCapabilitiesEvent event, BlockEntityType<? extends BaseElectricMachineEntity> type) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                (machine, side) -> {
                    // A. Internal Access (e.g. GUI or the machine itself) -> ALWAYS ALLOW
                    if (side == null) {
                        return machine.getEnergyStorage();
                    }

                    // B. Input Side -> ALLOW connection
                    if (machine.isInputSide(side)) {
                        return machine.getEnergyStorage();
                    }

                    // C. Output Side -> ALLOW connection
                    if (machine.isOutputSide(side)) {
                        return machine.getEnergyStorage();
                    }

                    // D. Otherwise -> DENY connection (Return null)
                    // This tells the wire/pipe: "I have no energy capability on this side."
                    return null;
                }
        );
    }
}
