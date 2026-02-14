package net.venera.galacticraftcore.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MachineConfigHelper {
    public static final Map<Supplier<? extends BlockEntityType<?>>, Map<Direction, Boolean>> IO_CONFIG = new HashMap<>();

    static { //Defines "Model Side" -> Input (True) / Output (False)
        IO_CONFIG.put(ModBlockEntities.REFINERY_ENTITY, Map.of(
                Direction.UP, true
        ));
        
        IO_CONFIG.put(ModBlockEntities.ENERGY_STORAGE_ENTITY, Map.of(
                Direction.WEST, true,
                Direction.EAST, false
        ));
        
        IO_CONFIG.put(ModBlockEntities.BASIC_SOLAR_PANEL_ENTITY, Map.of(
                Direction.NORTH, false
        ));
    }

    /**
     * NEW HELPER: Safely finds the config for a specific runtime BlockEntityType
     */
    public static Map<Direction, Boolean> getConfigFor(BlockEntityType<?> type) {
        for (var entry : IO_CONFIG.entrySet()) {
            // We verify if the Supplier matches the runtime type
            if (entry.getKey().get() == type) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * HELPER: Translates a "World Side" (e.g. East) into a "Model Side" (e.g. North)
     * based on how the machine is facing.
     * * @param worldSide     The absolute direction in the world (e.g. The side the wire is touching).
     * @param machineFacing The direction the machine is facing (BlockState).
     * @return The relative side on the default (North-facing) model.
     */
    public static Direction getRelativeSide(Direction worldSide, Direction machineFacing) {
        // Up and Down never rotate (unless your machine flips upside down, which is rare)
        if (worldSide.getAxis() == Direction.Axis.Y) {
            return worldSide;
        }

        
        if (machineFacing == Direction.NORTH) return worldSide; //Already at default facing value.

        // If facing South, everything is opposite.
        if (machineFacing == Direction.SOUTH) return worldSide.getOpposite();

        // If facing East:
        // World North -> Model West (Left)
        // World East  -> Model North (Front)
        // World South -> Model East (Right)
        // World West  -> Model South (Back)
        if (machineFacing == Direction.EAST) {
            return worldSide.getCounterClockWise();
        }

        // If facing West:
        // World North -> Model East (Right)
        if (machineFacing == Direction.WEST) {
            return worldSide.getClockWise();
        }

        return worldSide; // Fallback
    }
}
