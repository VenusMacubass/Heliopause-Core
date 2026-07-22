package net.venera.heliocore.block.hpc_custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.block.entity.MagneticCraftingTableEntity;
import net.venera.heliocore.screen.hpc_custom.MagneticCraftingTableMenu;

public class MagneticCraftingTableBlock extends Block implements EntityBlock {
    public MagneticCraftingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagneticCraftingTableEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MagneticCraftingTableEntity magneticBE) {
                MenuProvider menuProvider = new SimpleMenuProvider(
                        (containerId, playerInv, p) -> new MagneticCraftingTableMenu(containerId, playerInv, ContainerLevelAccess.create(level, pos), magneticBE),
                        Component.translatable("container.heliocore.magnetic_crafting")
                );
                player.openMenu(menuProvider, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MagneticCraftingTableEntity magneticBE) {
                ItemStackHandler inventory = magneticBE.getInventory();
                for (int i = 0; i < inventory.getSlots(); i++) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
