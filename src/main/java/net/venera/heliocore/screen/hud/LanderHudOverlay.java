package net.venera.heliocore.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;

public class LanderHudOverlay implements LayeredDraw.Layer {
    public static final LanderHudOverlay INSTANCE = new LanderHudOverlay();

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof Tier1RocketLanderEntity lander) {
            
            double yVelocity = lander.getDeltaMovement().y;
            double speedMps = Math.abs(yVelocity * 20.0);
            
            int color;
            if (yVelocity <= -6.0D) {
                color = 0xFF5555; // Red 
            } else if (yVelocity <= -1.8D) {
                color = 0xFFFF55; // Yellow 
            } else {
                color = 0x55FF55; // Green
            }

            Font font = mc.font;

            String formattedSpeed = String.format("%.1f", speedMps);
            Component text = Component.translatable("hud.heliocore.lander_descent_speed", formattedSpeed);

            int screenWidth = guiGraphics.guiWidth();
            int screenHeight = guiGraphics.guiHeight();
            
            int x = (screenWidth - font.width(text)) / 2;
            int y = screenHeight / 2 + 40;
            
            guiGraphics.drawString(font, text, x, y, color, true);
        }
    }
}
