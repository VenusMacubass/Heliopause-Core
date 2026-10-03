package net.venera.heliocore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.client.gui.font.providers.UnihexProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.radiation.RadiationData;
import net.venera.heliocore.dimension.HpCDimensions;
import net.venera.heliocore.util.SyncRadiationPayload;

import java.awt.*;
import java.util.Collection;

@EventBusSubscriber(modid = HeliopauseCore.MOD_ID)
public class HpCCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("radiation")
                .requires(source -> source.hasPermission(2)) // Require OP level 2 (standard cheats)

                // --- SUBCOMMAND: ADD ---
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0)) // Prevents negative numbers
                                        .executes(context -> executeRadiationCommand(
                                                context.getSource(),
                                                EntityArgument.getEntities(context, "targets"),
                                                DoubleArgumentType.getDouble(context, "amount"),
                                                "add"
                                        ))
                                )
                        )
                )

                // --- SUBCOMMAND: SET ---
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                                        .executes(context -> executeRadiationCommand(
                                                context.getSource(),
                                                EntityArgument.getEntities(context, "targets"),
                                                DoubleArgumentType.getDouble(context, "amount"),
                                                "set"
                                        ))
                                )
                        )
                )

                // --- SUBCOMMAND: REMOVE ---
                .then(Commands.literal("remove")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                                        .executes(context -> executeRadiationCommand(
                                                context.getSource(),
                                                EntityArgument.getEntities(context, "targets"),
                                                DoubleArgumentType.getDouble(context, "amount"),
                                                "remove"
                                        ))
                                )
                        )
                )

                // --- SUBCOMMAND: CLEAR ---
                // Clear doesn't need an amount argument!
                .then(Commands.literal("clear")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .executes(context -> executeRadiationCommand(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),
                                        0, // Amount doesn't matter here
                                        "clear"
                                ))
                        )
                )
        );

        dispatcher.register(Commands.literal("dimensiontp")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.literal("earth")
                                .executes(context -> teleportEntity(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),
                                        Level.OVERWORLD)
                                )
                        )
                        .then(Commands.literal("nether")
                                .executes(context -> teleportEntity(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),
                                        Level.NETHER)
                                )
                        )
                        .then(Commands.literal("end")
                                .executes(context -> teleportEntity(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),
                                        Level.END)
                                )
                        )
                        .then(Commands.literal("moon")
                                .executes(context -> teleportEntity(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),
                                        HpCDimensions.MOON_LEVEL_KEY)
                                )
                        )
                )
        );
    }

    private static int executeRadiationCommand(CommandSourceStack source, Collection<? extends Entity> targets, double amount, String action) {
        int successCount = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity living) {
                RadiationData radData = living.getData(HpCAttachments.RADIATION_DATA);

                switch (action) {
                    // We pass MAX_RADIATION as the limit so admin commands bypass Space Suit armor!
                    case "add" -> radData.changeRadiation(amount, true, RadiationData.MAX_RADIATION);
                    case "remove" -> radData.changeRadiation(amount, false);
                    case "set" -> radData.setRadiation(amount);
                    case "clear" -> radData.setRadiation(0);
                }

                // Force an immediate HUD sync so the admin sees the change instantly
                if (living instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new SyncRadiationPayload(radData.getRadiation()));
                }
                successCount++;
            }
        }

        // Send feedback to the admin who typed the command
        if (successCount > 0) {
            source.sendSuccess(() -> Component.literal("Successfully applied radiation entities."), true);
        } else {
            source.sendFailure(Component.literal("No valid living entities found to apply radiation."));
        }

        // Brigadier requires returning an integer (usually the number of successful executions)
        return successCount;
    }

    private static int teleportEntity(CommandSourceStack source, Collection<? extends Entity> targets, ResourceKey<Level> targetDimensionKey) {
        int successCount = 0;
        ServerLevel targetLevel = source.getServer().getLevel(targetDimensionKey);

        if (targetLevel == null) return 0; // Safety check

        double targetScale = getCoordinateScale(targetDimensionKey);

        for (Entity entity : targets) {
            double sourceScale = getCoordinateScale(entity.level().dimension());

            // Dynamic Math: Scales coordinates seamlessly between ANY two dimensions
            double targetX = (entity.getX() * sourceScale) / targetScale;
            double targetZ = (entity.getZ() * sourceScale) / targetScale;

            // Calculate a safe drop point
            double targetY = getSafeY(targetLevel, (int) targetX, (int) targetZ);

            DimensionTransition transition = new DimensionTransition(
                    targetLevel,
                    new Vec3(targetX, targetY, targetZ),
                    Vec3.ZERO, 
                    entity.getYRot(),
                    entity.getXRot(),
                    DimensionTransition.DO_NOTHING
            );

            Entity teleported = entity.changeDimension(transition);
            if (teleported != null) {
                successCount++;
            }
        }
        return successCount;
    }

    private static double getCoordinateScale(ResourceKey<Level> dimension) {
        if (dimension.equals(Level.NETHER)) return 8.0;
        if (dimension.equals(HpCDimensions.MOON_LEVEL_KEY)) return 4.0; 
        return 1.0;
    }

    private static double getSafeY(ServerLevel targetLevel, int x, int z) {
        targetLevel.getChunk(x >> 4, z >> 4, ChunkStatus.FULL, true);

        if (targetLevel.dimension().equals(Level.NETHER)) {
            // NETHER: Scan downward from below the bedrock roof
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 120, z);
            for (int y = 120; y > 32; y--) {
                pos.setY(y);

                // Look for 2 blocks of air above a block that is NOT air and NOT lava
                if (targetLevel.isEmptyBlock(pos) && targetLevel.isEmptyBlock(pos.above())) {
                    BlockPos floor = pos.below();
                    if (!targetLevel.getBlockState(floor).isAir() && targetLevel.getFluidState(floor).isEmpty()) {
                        return y + 0.1D; // Safe cave found!
                    }
                }
            }
            return 70.0D; // Fallback if no safe cave is found
        } else {
            // OVERWORLD / MOON / END: Surface scan
            int surfaceY = targetLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            
            return surfaceY > targetLevel.getMinBuildHeight() ? (surfaceY + 0.1D) : 600.0D;
        }
    }
}
