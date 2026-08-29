package net.venera.heliocore.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.hpc_custom.AirlockFrameSwitch;

public class OpenAirlockGoal extends Goal {
    private final Mob mob;
    private BlockPos doorPos = BlockPos.ZERO;
    private BlockPos switchPos = null;
    private int timeoutTicks = 0;

    public OpenAirlockGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        Level level = this.mob.level();
        BlockPos mobPos = this.mob.blockPosition();
        this.switchPos = null;
        
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos checkPos = mobPos.relative(dir);
            if (level.getBlockState(checkPos).is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get()) ||
                    level.getBlockState(checkPos.above()).is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())) {

                this.doorPos = checkPos; 
                this.switchPos = findSwitch(level, this.doorPos);
                if (this.switchPos != null) return true;
            }
        }
        
        for (int y = 2; y <= 4; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos overheadPos = mobPos.offset(x, y, z);
                    if (level.getBlockState(overheadPos).is(HpCBlocks.AIRLOCK_FRAME_SWITCH.get())) {
                        BlockState switchState = level.getBlockState(overheadPos);

                        // Only take control if the switch is currently ACTIVE (open)
                        if (switchState.getBlock() instanceof AirlockFrameSwitch && switchState.getValue(AirlockFrameSwitch.ACTIVE)) {
                            this.doorPos = mobPos;
                            this.switchPos = overheadPos;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private BlockPos findSwitch(Level level, BlockPos basePos) {
        for (int y = 1; y <= 4; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos check = basePos.offset(x, y, z);
                    if (level.getBlockState(check).is(HpCBlocks.AIRLOCK_FRAME_SWITCH.get())) {
                        return check;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void start() {
        this.timeoutTicks = 0;
        Level level = this.mob.level();
        
        if (this.switchPos != null) {
            BlockState switchState = level.getBlockState(this.switchPos);
            if (switchState.getBlock() instanceof AirlockFrameSwitch switchBlock) {
                if (!switchState.getValue(AirlockFrameSwitch.ACTIVE)) {
                    switchBlock.toggleDoor(level, this.switchPos, switchState, true);
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.switchPos == null) return false;
        
        this.timeoutTicks++;
        if (this.timeoutTicks >= 100) return false;
        
        double distSq = this.mob.distanceToSqr(this.doorPos.getX() + 0.5, this.doorPos.getY(), this.doorPos.getZ() + 0.5);
        return distSq <= 6.25;
    }

    @Override
    public void stop() {
        if (this.switchPos != null) {
            Level level = this.mob.level();
            BlockState switchState = level.getBlockState(this.switchPos);

            if (switchState.getBlock() instanceof AirlockFrameSwitch switchBlock) {
                if (switchState.getValue(AirlockFrameSwitch.ACTIVE)) {
                    switchBlock.toggleDoor(level, this.switchPos, switchState, false);
                }
            }
        }
        this.switchPos = null;
    }
}