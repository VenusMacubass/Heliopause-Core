package net.venera.heliocore.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.hpc_custom.GasTankItem;
import top.theillusivec4.curios.api.CuriosApi;

public class OxygenSetupHelper {
    private static final int OXYGEN_USAGE = 2;

    public static boolean checkOxygenSetup(LivingEntity livingEntity) {
        var optionalCurios = CuriosApi.getCuriosInventory(livingEntity);
        if (optionalCurios.isEmpty()) {
            return false; 
        }

        var curios = optionalCurios.get().getCurios();
        var maskSlot = curios.get("oxygen_mask");
        if (maskSlot == null || maskSlot.getStacks().getStackInSlot(0).getItem() != HpCItems.OXYGEN_MASK.get()) {
            return false;
        }
        
        var connectorSlot = curios.get("oxygen_connectors");
        if (connectorSlot == null || connectorSlot.getStacks().getStackInSlot(0).getItem() != HpCItems.OXYGEN_CONNECTORS.get()) {
            return false;
        }
        
        var tank1Slot = curios.get("oxygen_tank_1");
        var tank2Slot = curios.get("oxygen_tank_2");

        ItemStack tank1Stack = tank1Slot != null ? tank1Slot.getStacks().getStackInSlot(0) : ItemStack.EMPTY;
        ItemStack tank2Stack = tank2Slot != null ? tank2Slot.getStacks().getStackInSlot(0) : ItemStack.EMPTY;
        
        if (tryConsumeOxygen(tank1Stack)) {
            return true;
        }
        
        if (tryConsumeOxygen(tank2Stack)) {
            return true;
        }
        
        return false;
    }

    private static boolean tryConsumeOxygen(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof GasTankItem gasTankItem)) {
            return false;
        }

        GasTankData data = gasTankItem.getGasTankData(stack);

        if (data != null && data.isOxygen() && data.amount() >= OXYGEN_USAGE) {
            gasTankItem.drain(stack, OXYGEN_USAGE);
            return true;
        }

        return false;
    }
}
