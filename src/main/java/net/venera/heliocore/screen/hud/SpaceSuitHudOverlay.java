package net.venera.heliocore.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;

public class SpaceSuitHudOverlay implements LayeredDraw.Layer{
    public static final SpaceSuitHudOverlay INSTANCE = new SpaceSuitHudOverlay();
    
    private static final ResourceLocation OXYGEN_TANK = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/oxygen_tanks.png");
    private static final ResourceLocation HAZARD_LEVEL_V = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/hazard_level_vertical.png");
    private static final ResourceLocation HAZARD_LEVEL_H = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/hazard_level_horizontal.png");
    
    public static int oxygenAmount1 = 0;
    public static int oxygenAmount2 = 0;
    public static int oxygenCapacity1 = 0;
    public static int oxygenCapacity2 = 0;
    public static int pressureAmount = 0;
    public static final int pressureCapacity = 7600;
    public static double radiationAmount = 0;
    public static final double radiationCapacity = 1000;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        float scale = 2.5f;

        // 1. DIMENSIONS
        // Background height increased to 50 to fit the new text and bars
        int bgWidth = (int) (20 * scale);
        int bgHeight = (int) (50 * scale);

        int tankWidth = (int) (16 * scale);
        int tankHeight = (int) (16 * scale);

        // *CHANGE THESE IF YOUR HAZARD PNG IS TALLER THAN 4 PIXELS*
        int hazardWidth = (int) (16 * scale);
        int hazardHeight = (int) (4 * scale);

        // 2. THE MASTER ANCHOR
        int hudX = 10;
        int hudY = 10;

        // tankX is the universal X coordinate for EVERYTHING (Tanks, Bars, and Text)
        int tankX = hudX + (int)(2 * scale);

        // Draw Background
        guiGraphics.fill(hudX, hudY, hudX + bgWidth, hudY + bgHeight, 0x80000000);

        // 3. TOP-DOWN Y-COORDINATES
        // We space everything out mathematically from the top!
        int radTextY   = hudY + (int) (3 * scale);
        int radBarY    = hudY + (int) (8 * scale);

        int pressTextY = hudY + (int) (15 * scale);
        int pressBarY  = hudY + (int) (20 * scale);

        int oxyTextY   = hudY + (int) (27 * scale);
        int tankY      = hudY + (int) (32 * scale);

        // 4. DRAW HORIZONTAL HAZARD LIQUIDS (Radiation & Pressure)
        // Assuming the hazard bars have a 1-pixel empty border on the left/right
        int maxHazardFill = (int) (14 * scale); // 1 pixel border on a 16 pixel texture = 14 fill

        int radFill = getHazardScaled(radiationAmount, radiationCapacity, maxHazardFill);
        int pressFill = getPressureScaled(pressureAmount, maxHazardFill);

        int radColor = getRadiationColor(radiationAmount);
        int pressColor = getPressureColor(pressureAmount);

        // Radiation Fill 
        guiGraphics.fill(tankX + (int)(1 * scale), radBarY + (int)(1 * scale),
                tankX + (int)(1 * scale) + radFill, radBarY + (int)(3 * scale), radColor);

        // Pressure Fill 
        guiGraphics.fill(tankX + (int)(1 * scale), pressBarY + (int)(1 * scale),
                tankX + (int)(1 * scale) + pressFill, pressBarY + (int)(3 * scale), pressColor);
        // 5. DRAW VERTICAL OXYGEN LIQUIDS
        int maxOxyHeight = (int) (14 * scale);
        int oxyBottomY = tankY + (int) (15 * scale);

        int oxyFill1 = getHazardScaled(oxygenAmount1, oxygenCapacity1, maxOxyHeight);
        int oxyFill2 = getHazardScaled(oxygenAmount2, oxygenCapacity2, maxOxyHeight);

        int liqWidth = (int) (4 * scale);
        guiGraphics.fill(tankX + (int)(2 * scale), oxyBottomY - oxyFill1, tankX + (int)(2 * scale) + liqWidth, oxyBottomY, 0xFF00FFFF);
        guiGraphics.fill(tankX + (int)(10 * scale), oxyBottomY - oxyFill2, tankX + (int)(10 * scale) + liqWidth, oxyBottomY, 0xFF00FFFF);

        // 6. DRAW ALL TEXTURES OVER THE LIQUIDS
        // (Texture, X, Y, ScaledW, ScaledH, U, V, SourceW, SourceH, TexW, TexH)
        guiGraphics.blit(HAZARD_LEVEL_H, tankX, radBarY, hazardWidth, hazardHeight, 0, 0, 16, 4, 16, 4);
        guiGraphics.blit(HAZARD_LEVEL_H, tankX, pressBarY, hazardWidth, hazardHeight, 0, 0, 16, 4, 16, 4);
        guiGraphics.blit(OXYGEN_TANK, tankX, tankY, tankWidth, tankHeight, 0, 0, 16, 16, 16, 16);

        //region Texts
        Font font = mc.font;
        guiGraphics.pose().pushPose();
        float textScale = scale * 0.35f;
        guiGraphics.pose().scale(textScale, textScale, 1.0f);
        
        int textX = (int) (tankX / textScale);

        guiGraphics.drawString(font, "Radiation:", textX, (int) (radTextY / textScale), 0xFFFFFF, false);
        guiGraphics.drawString(font, "Pressure:", textX, (int) (pressTextY / textScale), 0xFFFFFF, false);
        guiGraphics.drawString(font, "Oxygen:", textX, (int) (oxyTextY / textScale), 0xFFFFFF, false);

        guiGraphics.pose().popPose();
        //endregion
    }

    //region Helpers
    public int getHazardScaled(double amount, double capacity, int maxPixels) {
        if (capacity == 0 || amount <= 0) return 0;
        return (int) ((amount * maxPixels) / capacity);
    }
    public int getPressureScaled(double amount, int maxPixels) {
        double ratio;
        if (amount <= 760) {
            // First half of the bar is 0 to 760
            ratio = (amount / 760.0) * 0.5;
        } else {
            // Second half of the bar is 760 to 7600
            ratio = 0.5 + ((amount - 760.0) / (7600.0 - 760.0)) * 0.5;
        }
        return (int) Math.min(ratio * maxPixels, maxPixels);
    }

    // Dynamic Color for Radiation
    public int getRadiationColor(double amount) {
        if (amount < 100) return 0xFF55FF55; // Green (Safe)
        if (amount < 400) return 0xFFFFFF55; // Yellow (Warning)
        return 0xFFFF5555; // Red (Lethal)
    }

    // Dynamic Color for Pressure based on your SpaceGearSetupController bounds
    public int getPressureColor(double amount) {
        if (amount < 228 || amount > 2280) return 0xFFFF5555; // Red (Requires Baric Setup 1 or 2)
        if (amount < 500 || amount > 1500) return 0xFFFFFF55; // Yellow (Getting uncomfortable)
        return 0xFF55FF55; // Green (Optimal ~760)
    }
    //endregion
}
