package net.venera.heliocore.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.Entity;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;

public class LanderHudOverlay implements LayeredDraw.Layer {
    public static final LanderHudOverlay INSTANCE = new LanderHudOverlay();

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Entity vehicle = mc.player.getVehicle();
        if (vehicle instanceof Tier1RocketLanderEntity lander) {
            
            double yVelocity = lander.getDeltaMovement().y;
            double speedMps = Math.abs(yVelocity * 20.0);
            
            int color;
            if (yVelocity <= -6.0D) {
                color = 0xFF5555; // Red 
            } else if (yVelocity <= -2.0D) {
                color = 0xFFFF55; // Yellow 
            } else {
                color = 0x55FF55; // Green
            }

            Font font = mc.font;
            String text = String.format("Descent Speed: %.1f m/s", speedMps);

            int screenWidth = guiGraphics.guiWidth();
            int screenHeight = guiGraphics.guiHeight();
            
            int x = (screenWidth - font.width(text)) / 2;
            int y = screenHeight / 2 + 40;
            
            guiGraphics.drawString(font, text, x, y, color, true);
        }
    }
}
