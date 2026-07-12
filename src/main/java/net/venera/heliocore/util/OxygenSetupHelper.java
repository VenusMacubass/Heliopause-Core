package net.venera.heliocore.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.hpc_custom.GasTankItem;
import top.theillusivec4.curios.api.CuriosApi;

public class OxygenSetupHelper {
    private static final int OXYGEN_USAGE = 5;
    private static boolean oxygenMaskValid = false;
    private static boolean oxygenConnectorsValid = false;
    private static boolean oxygenTank1Valid = false;
    private static boolean oxygenTank2Valid = false;
    
    public static boolean checkOxygenSetup(LivingEntity livingEntity) {
        validOxygenSetup(livingEntity);
        return oxygenMaskValid && oxygenConnectorsValid && (oxygenTank1Valid || oxygenTank2Valid);
    }
    
    public static void validOxygenSetup(LivingEntity entity) {
        var curiosInventory = CuriosApi.getCuriosInventory(entity);
        if (curiosInventory.isEmpty()) {
            return;
        }
        var oxygenMask = curiosInventory.get().getCurios().get("oxygen_mask");
        var oxygenConnectors = curiosInventory.get().getCurios().get("oxygen_connectors");
        var oxygenTank1 =  curiosInventory.get().getCurios().get("oxygen_tank_1");
        var oxygenTank2 =  curiosInventory.get().getCurios().get("oxygen_tank_2");

        oxygenMaskValid = oxygenMask.getStacks().getStackInSlot(0).getItem() == HpCItems.OXYGEN_MASK.get();
        oxygenConnectorsValid = oxygenConnectors.getStacks().getStackInSlot(0).getItem() == HpCItems.OXYGEN_CONNECTORS.get();
        
        if(oxygenTank1.getStacks().getStackInSlot(0).getItem() instanceof GasTankItem gasTankItem1){
            oxygenTank1Valid = tankOxygenLevel(gasTankItem1, oxygenTank1.getStacks().getStackInSlot(0)) > OXYGEN_USAGE;
        }
        if(oxygenTank2.getStacks().getStackInSlot(0).getItem() instanceof GasTankItem gasTankItem2){
            oxygenTank2Valid = tankOxygenLevel(gasTankItem2, oxygenTank1.getStacks().getStackInSlot(0)) > OXYGEN_USAGE;
        }
    }    
    
    private static int tankOxygenLevel(GasTankItem gasTankItem, ItemStack stack) {
        GasTankData data = gasTankItem.getGasTankData(stack);
        if(data == null) return 0;
        if(data.isOxygen() && data.amount() > OXYGEN_USAGE){ 
            return data.amount();
        }
        return 0;
    }
}
