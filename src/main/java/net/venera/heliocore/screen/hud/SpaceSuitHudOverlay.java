package net.venera.heliocore.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;

public class SpaceSuitHudOverlay implements LayeredDraw.Layer{
    public static final SpaceSuitHudOverlay INSTANCE = new SpaceSuitHudOverlay();
    
    private static final ResourceLocation OXYGEN_TANK = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/oxygen_tanks.png");
    private static final ResourceLocation HAZARD_LEVEL_V = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/hazard_level_vertical.png");
    private static final ResourceLocation HAZARD_LEVEL_H = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/hazard_level_horizontal.png");
    private static final ResourceLocation CONNECTOR_ICON = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/player_gui/oxygen_connectors_slot.png");
    private static final ResourceLocation HEAD_ICON = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/player_gui/t1_thermal_insulation_head_slot.png");
    private static final ResourceLocation TORSO_ICON = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/player_gui/t1_thermal_insulation_torso_slot.png");
    private static final ResourceLocation LEGS_ICON = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/player_gui/t1_thermal_insulation_leggings_slot.png");
    private static final ResourceLocation HANDS_ICON = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/player_gui/t1_thermal_insulation_hands_and_feet_slot.png");
    
    public static int oxygenAmount1 = 0;
    public static int oxygenAmount2 = 0;
    public static int oxygenCapacity1 = 0;
    public static int oxygenCapacity2 = 0;
    public static int pressureAmount = 0;
    public static final int pressureCapacity = 7600;
    public static double radiationAmount = 0;
    public static final double radiationCapacity = 800;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        float scale = 2.5f;

        // 1. DIMENSIONS
        // Background height increased to 50 to fit the new text and bars
        int bgWidth = (int) (20 * scale);
        int bgHeight = (int) (46 * scale);

        int tankWidth = (int) (16 * scale);
        int tankHeight = (int) (16 * scale);
        
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
        int radTextY   = hudY + (int) (11 * scale);
        int radBarY    = hudY + (int) (14 * scale);

        int pressTextY = hudY + (int) (18 * scale);
        int pressBarY  = hudY + (int) (21 * scale);

        int oxyTextY   = hudY + (int) (25 * scale);
        int tankY      = hudY + bgHeight - tankHeight - (int)(2 * scale);

        var inventory = mc.player.getData(HpCAttachments.EQUIPMENT_INVENTORY);
        guiGraphics.pose().pushPose();
        float oxyIconScale = scale * (6.0f / 16.0f);
        guiGraphics.pose().scale(oxyIconScale, oxyIconScale, 1.0f);

        // Centered horizontally: 3px padding on the left and right
        int oxyCol1 = (int) ((hudX + (3 * scale)) / oxyIconScale);
        int oxyCol2 = (int) ((hudX + (11 * scale)) / oxyIconScale);
        int row1    = (int) ((hudY + (1 * scale)) / oxyIconScale);

//        renderSlot(guiGraphics, inventory.getStackInSlot(0), EMPTY_MASK, oxyCol1, row1);
        renderSlot(guiGraphics, inventory.getStackInSlot(1), CONNECTOR_ICON, oxyCol2, row1);
        guiGraphics.pose().popPose();

        // 2. Row 2: Thermal Gear (4 Items, 4x4 pixels each)
        guiGraphics.pose().pushPose();
        // Shrink the standard 16x16 down to fit into a tiny 4x4 space!
        float thermIconScale = scale * (4.0f / 16.0f);
        guiGraphics.pose().scale(thermIconScale, thermIconScale, 1.0f);

        // Fit all 4 items within the 20-pixel width (1, 6, 11, 16)
        int thermCol1 = (int) ((hudX + (1 * scale)) / thermIconScale);
        int thermCol2 = (int) ((hudX + (5 * scale)) / thermIconScale);
        int thermCol3 = (int) ((hudX + (10 * scale)) / thermIconScale);
        int thermCol4 = (int) ((hudX + (14 * scale)) / thermIconScale);
        int row2      = (int) ((hudY + (7 * scale)) / thermIconScale); // Y level kept identical

        renderSlot(guiGraphics, inventory.getStackInSlot(4), HEAD_ICON, thermCol1, row2);
        renderSlot(guiGraphics, inventory.getStackInSlot(5), TORSO_ICON, thermCol2, row2);
        renderSlot(guiGraphics, inventory.getStackInSlot(6), LEGS_ICON, thermCol3, row2);
        renderSlot(guiGraphics, inventory.getStackInSlot(7), HANDS_ICON, thermCol4, row2);

        guiGraphics.pose().popPose();
        
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
            ratio = 0.5 + ((amount - 760.0) / (pressureCapacity - 760.0)) * 0.5;
        }
        return (int) Math.min(ratio * maxPixels, maxPixels);
    }

    // Dynamic Color for Radiation
    public int getRadiationColor(double amount) {
        if (amount < 100) return 0xFF55FF55; // Green (Safe)
        if (amount < 200) return 0xFFFFFF55; // Yellow (Warning)
        return 0xFFFF5555; // Red (Lethal)
    }

    // Dynamic Color for Pressure based on your SpaceGearSetupController bounds
    public int getPressureColor(double amount) {
        if (amount < 228 || amount > 2280) return 0xFFFF5555; // Red (Requires Baric Setup 1 or 2)
        if (amount < 500 || amount > 1500) return 0xFFFFFF55; // Yellow (Getting uncomfortable)
        return 0xFF55FF55; // Green (Optimal ~760)
    }

    private void renderSlot(GuiGraphics guiGraphics, ItemStack stack, ResourceLocation emptyTex, int x, int y) {
        if (!stack.isEmpty()) {
            guiGraphics.renderItem(stack, x, y);
        } else {
            // Draws the empty texture at standard 16x16 size (which is scaled down by iconScale!)
            guiGraphics.blit(emptyTex, x, y, 0, 0, 16, 16, 16, 16);
        }
    }
    //endregion
}
