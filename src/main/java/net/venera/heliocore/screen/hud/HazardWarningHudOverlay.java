package net.venera.heliocore.screen.hud;

import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class HazardWarningHudOverlay implements LayeredDraw.Layer {
    public static final HazardWarningHudOverlay INSTANCE = new HazardWarningHudOverlay();
    
    public static boolean showRadiationWarning = false;
    public static boolean showPressureWarning = false;
    public static boolean showOxygenWarning = false;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!showRadiationWarning && !showPressureWarning && !showOxygenWarning) return;

        Minecraft mc = Minecraft.getInstance();
        List<Component> activeWarnings = new ArrayList<>();

        if (showRadiationWarning) activeWarnings.add(Component.translatable("hud.heliocore.warning.radiation"));
        if (showPressureWarning) activeWarnings.add(Component.translatable("hud.heliocore.warning.pressure"));
        if (showOxygenWarning) activeWarnings.add(Component.translatable("hud.heliocore.warning.oxygen"));

        if (!activeWarnings.isEmpty()) {
            Font font = mc.font;
            int screenWidth = guiGraphics.guiWidth();

            double wave = Math.abs(Math.sin(Util.getMillis() / 250.0));
            int alpha = (int) (wave * 255.0);
            int color = (alpha << 24) | 0x00FF5555;

            int warningY = guiGraphics.guiHeight() / 2 - 60;

            for (Component warning : activeWarnings) {
                int x = (screenWidth - font.width(warning)) / 2;
                guiGraphics.drawString(font, warning, x, warningY, color, true);
                warningY += 12;
            }
        }
    }
}