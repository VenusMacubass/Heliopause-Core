package net.venera.heliocore.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.event.HpCEvents;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.hpc_custom.GasTankItem;

public class SpaceGearSetupHelper {
    private static final int OXYGEN_USAGE = 2;
    public static boolean checkOxygenSetup(LivingEntity livingEntity) {
        ItemStackHandler inventory = livingEntity.getData(HpCAttachments.EQUIPMENT_INVENTORY);
        
        ItemStack maskStack = inventory.getStackInSlot(0);
        if (maskStack.isEmpty() || maskStack.getItem() != HpCItems.OXYGEN_MASK.get()) {
            return false;
        }
        
        ItemStack connectorStack = inventory.getStackInSlot(1);
        if (connectorStack.isEmpty() || connectorStack.getItem() != HpCItems.OXYGEN_CONNECTORS.get()) {
            return false;
        }
        
        ItemStack tank1Stack = inventory.getStackInSlot(2);
        ItemStack tank2Stack = inventory.getStackInSlot(3);
        
        if (tryConsumeOxygen(tank1Stack)) {
            HpCEvents.syncToAllTracking(livingEntity);
            return true;
        }

        if (tryConsumeOxygen(tank2Stack)) {
            HpCEvents.syncToAllTracking(livingEntity);
            return true;
        }

        return false;
    }

    public static int checkThermalSetup(LivingEntity livingEntity) {
        ItemStackHandler inventory = livingEntity.getData(HpCAttachments.EQUIPMENT_INVENTORY);
        int thermalProtectionScore = 0;

        ItemStack headStack = inventory.getStackInSlot(4);
        if (headStack.is(HpCItems.T1_THERMAL_INSULATION_HEAD.get())) {
            thermalProtectionScore++;
        }

        
        ItemStack torsoStack = inventory.getStackInSlot(5);
        if (torsoStack.is(HpCItems.T1_THERMAL_INSULATION_TORSO.get())) {
            thermalProtectionScore++;
        }

        
        ItemStack legStack = inventory.getStackInSlot(6);
        if (legStack.is(HpCItems.T1_THERMAL_INSULATION_LEGGINGS.get())) {
            thermalProtectionScore++;
        }

        
        ItemStack handsAndFeetStack = inventory.getStackInSlot(7);
        if (handsAndFeetStack.is(HpCItems.T1_THERMAL_INSULATION_HANDS_AND_FEET.get())) {
            thermalProtectionScore++;
        }
        
        return thermalProtectionScore;
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