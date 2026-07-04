package net.venera.heliocore.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MachineConfigHelper {
    public static final Map<Supplier<? extends BlockEntityType<?>>, Map<Direction, Boolean>> IO_CONFIG = new HashMap<>();
    static { 
        IO_CONFIG.put(HpCBlockEntities.REFINERY_ENTITY, Map.of(
                Direction.UP, true
        ));
        
        IO_CONFIG.put(HpCBlockEntities.ENERGY_STORAGE_ENTITY, Map.of(
                Direction.WEST, false,
                Direction.EAST, true
        ));
        
        IO_CONFIG.put(HpCBlockEntities.BASIC_SOLAR_PANEL_ENTITY, Map.of(
                Direction.NORTH, false
        ));
        
        IO_CONFIG.put(HpCBlockEntities.CARGO_MANAGER_ENTITY, Map.of(
                Direction.NORTH, true
        ));
        
        IO_CONFIG.put(HpCBlockEntities.FUEL_MANAGER_ENTITY, Map.of(
                Direction.NORTH, true
        ));
        IO_CONFIG.put(HpCBlockEntities.OXYGEN_GENERATOR_ENTITY, Map.of(
                Direction.NORTH, true
        ));
        IO_CONFIG.put(HpCBlockEntities.GAS_COMPRESSOR_ENTITY, Map.of(
                Direction.NORTH, true
        ));
        IO_CONFIG.put(HpCBlockEntities.ENERGY_GENERATOR_ENTITY, Map.of(
                Direction.NORTH, false
        ));
    }
    
    public static Map<Direction, Boolean> getConfigFor(BlockEntityType<?> type) {
        for (var entry : IO_CONFIG.entrySet()) {
            if (entry.getKey().get() == type) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Direction getRelativeSide(Direction worldSide, Direction machineFacing) {
        if (worldSide.getAxis() == Direction.Axis.Y) {
            return worldSide;
        }
        
        if (machineFacing == Direction.NORTH) return worldSide; 

        if (machineFacing == Direction.SOUTH) return worldSide.getOpposite();
        
        if (machineFacing == Direction.EAST) {
            return worldSide.getCounterClockWise();
        }

        if (machineFacing == Direction.WEST) {
            return worldSide.getClockWise();
        }

        return worldSide; 
    }

    public interface IToggleableMachine {
        void toggleEnabled(int buttonId);
    } 
}
