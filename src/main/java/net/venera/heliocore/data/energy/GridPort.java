package net.venera.heliocore.data.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record GridPort(BlockPos machinePos, Direction machineFace, GridPort.PortType type) {
    public enum PortType {
        INPUT,  //Consumer
        OUTPUT  //Generator
    }
    
    public BlockPos getConnectedWirePos() {
        return machinePos.relative(machineFace);
    }
}
