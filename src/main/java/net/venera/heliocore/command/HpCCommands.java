package net.venera.heliocore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.radiation.RadiationData;
import net.venera.heliocore.util.SyncRadiationPayload;

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
}
