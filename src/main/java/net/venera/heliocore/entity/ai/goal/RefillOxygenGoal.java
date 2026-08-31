package net.venera.heliocore.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.venera.heliocore.block.entity.FluidTankEntity;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.hpc_custom.GasTankItem;

public class RefillOxygenGoal extends MoveToBlockGoal {
    private final Villager villager;
    private int interactCooldown = 0;
    private final String MEMORY_KEY = "KnownOxygenTankPos";

    public RefillOxygenGoal(Villager villager, double speedModifier, int searchRange) {
        super(villager, speedModifier, searchRange);
        this.villager = villager;
    }

    @Override
    public boolean canUse() {
        if (!needsOxygen()) return false;

        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && needsMoreOxygen();
    }
    
    @Override
    public double acceptedDistance() {
        return 2.0D;
    }

    @Override
    protected boolean isReachedTarget() {
        return this.villager.blockPosition().closerThan(this.blockPos, 2.0D);
    }

    @Override
    protected boolean findNearestBlock() {
        CompoundTag persistentData = this.villager.getPersistentData();

        if (persistentData.contains(MEMORY_KEY)) {
            BlockPos savedPos = BlockPos.of(persistentData.getLong(MEMORY_KEY));

            if (this.isValidTarget(this.villager.level(), savedPos)) {
                this.blockPos = savedPos;
                return true;
            } else {
                persistentData.remove(MEMORY_KEY);
            }
        }

        return super.findNearestBlock();
    }

    private boolean needsOxygen() {
        var inventory = this.villager.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        for (int i = 2; i <= 3; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof GasTankItem gasTankItem) {
                GasTankData data = gasTankItem.getGasTankData(stack);
                if (data != null && (data.isOxygen() || data.isEmpty())) {
                    float fillPercentage = (float) data.amount() / GasTankItem.MAX_CAPACITY;
                    if (fillPercentage <= 0.20f) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean needsMoreOxygen() {
        var inventory = this.villager.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        for (int i = 2; i <= 3; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof GasTankItem gasTankItem) {
                GasTankData data = gasTankItem.getGasTankData(stack);
                if (data != null && data.getSpace() > 0 && (data.isOxygen() || data.isEmpty())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof FluidTankEntity tankEntity) {
            if (tankEntity.fluidTank.getFluidAmount() > 0 && tankEntity.fluidTank.getFluid().getFluid().isSame(HpCFluids.OXYGEN.get())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isReachedTarget()) {
            if (this.interactCooldown > 0) {
                this.interactCooldown--;
            } else {
                refillTank();
                this.interactCooldown = 20;
            }
        }
    }

    private void refillTank() {
        BlockEntity be = this.villager.level().getBlockEntity(this.blockPos);
        if (!(be instanceof FluidTankEntity fluidTank)) return;

        var inventory = this.villager.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        for (int i = 2; i <= 3; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof GasTankItem gasTankItem) {
                GasTankData data = gasTankItem.getGasTankData(stack);

                if (data != null && data.getSpace() > 0 && (data.isOxygen() || data.isEmpty())) {
                    int amountToPull = Math.min(data.getSpace(), fluidTank.fluidTank.getFluidAmount());
                    FluidStack drained = fluidTank.fluidTank.drain(amountToPull, IFluidHandler.FluidAction.EXECUTE);

                    if (drained.getAmount() > 0) {
                        gasTankItem.fill(stack, GasTankData.OXYGEN_GAS, drained.getAmount());

                        this.villager.getPersistentData().putLong(MEMORY_KEY, this.blockPos.asLong());
                        return;
                    }
                }
            }
        }
    }
}