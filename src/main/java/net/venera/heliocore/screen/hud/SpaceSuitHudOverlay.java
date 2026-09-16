package net.venera.heliocore.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.HeliopauseClientConfig;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.item.HpCTags;

public class SpaceSuitHudOverlay implements LayeredDraw.Layer{
    public static final SpaceSuitHudOverlay INSTANCE = new SpaceSuitHudOverlay();
    
    private static final ResourceLocation OXYGEN_TANK = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/oxygen_tanks.png");
    private static final ResourceLocation HAZARD_LEVEL_H = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/hud/hazard_level_horizontal.png");
    private static final ResourceLocation OXYGEN_MASK_ICON = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/player_gui/oxygen_mask_slot.png");
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

        if (!HeliopauseClientConfig.HUD_ENABLED.get()) return;
        var head = mc.player.getItemBySlot(EquipmentSlot.HEAD);
        var chest = mc.player.getItemBySlot(EquipmentSlot.CHEST);

        boolean hasHelmet = head.is(HpCTags.Items.T1_PRESSURE_PROTECTORS) || head.is(HpCTags.Items.T2_PRESSURE_PROTECTORS);
        boolean hasChestplate = chest.is(HpCTags.Items.T1_PRESSURE_PROTECTORS) || chest.is(HpCTags.Items.T2_PRESSURE_PROTECTORS);

        if (!hasHelmet || !hasChestplate) {
            return;
        }

        float scale = HeliopauseClientConfig.HUD_SCALE.get().floatValue();
        int hudX = HeliopauseClientConfig.HUD_X.get();
        int hudY = HeliopauseClientConfig.HUD_Y.get();

        boolean isHorizontal = HeliopauseClientConfig.HUD_HORIZONTAL.get();

        // 1. DYNAMIC BACKGROUND SIZING (Much shorter and tighter!)
        int bgWidth = (int) ((isHorizontal ? 52 : 20) * scale);
        int bgHeight = (int) ((isHorizontal ? 23 : 46) * scale);

        int tankWidth = (int) (16 * scale);
        int tankHeight = (int) (16 * scale);

        int hazardWidth = (int) (16 * scale);
        int hazardHeight = (int) (4 * scale);

        // --- NEW: DYNAMIC COORDINATE ROUTER ---
        int tankX, tankY, oxyTextY;
        int radBarX, radBarY, radTextY;
        int pressBarX, pressBarY, pressTextY;

        if (isHorizontal) {
            // PART 1: Oxygen (Left)
            // Moved up to Y=2 so it doesn't push the bottom edge down
            tankX = hudX + (int) (2 * scale);
            oxyTextY = hudY + (int) (2 * scale);
            tankY = hudY + (int) (5 * scale);

            // PART 2: Hazards (Middle)
            // Moved closer to the tanks (X=20) and squished the vertical gaps
            radBarX = hudX + (int) (20 * scale);
            radTextY = hudY + (int) (2 * scale);
            radBarY = hudY + (int) (5 * scale);

            pressBarX = hudX + (int) (20 * scale);
            pressTextY = hudY + (int) (11 * scale);
            pressBarY = hudY + (int) (14 * scale);
        } else {
            // VERTICAL LAYOUT (Your exact numbers remain untouched)
            tankX = hudX + (int)(2 * scale);
            radBarX = tankX;
            pressBarX = tankX;

            radTextY   = hudY + (int) (11 * scale);
            radBarY    = hudY + (int) (14 * scale);

            pressTextY = hudY + (int) (18 * scale);
            pressBarY  = hudY + (int) (21 * scale);

            oxyTextY   = hudY + (int) (25 * scale);
            tankY      = hudY + bgHeight - tankHeight - (int)(2 * scale);
        }

        // Draw Background
        guiGraphics.fill(hudX, hudY, hudX + bgWidth, hudY + bgHeight, 0x80000000);

        // --- RENDER EQUIPMENT ICONS ---
        var inventory = mc.player.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        if (isHorizontal) {
            // PART 3 (Right): Thermal Icons
            // Moved closer (X=38) and tightened vertical gaps (2, 7, 12, 17)
            guiGraphics.pose().pushPose();
            float thermIconScale = scale * (4.0f / 16.0f);
            guiGraphics.pose().scale(thermIconScale, thermIconScale, 1.0f);

            int thermCol = (int) ((hudX + (38 * scale)) / thermIconScale);
            int tRow1 = (int) ((hudY + (2 * scale)) / thermIconScale);
            int tRow2 = (int) ((hudY + (7 * scale)) / thermIconScale);
            int tRow3 = (int) ((hudY + (12 * scale)) / thermIconScale);
            int tRow4 = (int) ((hudY + (17 * scale)) / thermIconScale);

            renderSlot(guiGraphics, inventory.getStackInSlot(4), HEAD_ICON, thermCol, tRow1);
            renderSlot(guiGraphics, inventory.getStackInSlot(5), TORSO_ICON, thermCol, tRow2);
            renderSlot(guiGraphics, inventory.getStackInSlot(6), LEGS_ICON, thermCol, tRow3);
            renderSlot(guiGraphics, inventory.getStackInSlot(7), HANDS_ICON, thermCol, tRow4);
            guiGraphics.pose().popPose();

            // PART 4 (Far Right): Oxygen Setup Icons
            // Moved closer (X=44)
            guiGraphics.pose().pushPose();
            float oxyIconScale = scale * (6.0f / 16.0f);
            guiGraphics.pose().scale(oxyIconScale, oxyIconScale, 1.0f);

            int oxyCol = (int) ((hudX + (44 * scale)) / oxyIconScale);
            int oRow1 = (int) ((hudY + (3 * scale)) / oxyIconScale);
            int oRow2 = (int) ((hudY + (12 * scale)) / oxyIconScale);

            renderSlot(guiGraphics, inventory.getStackInSlot(0), OXYGEN_MASK_ICON, oxyCol, oRow1);
            renderSlot(guiGraphics, inventory.getStackInSlot(1), CONNECTOR_ICON, oxyCol, oRow2);
            guiGraphics.pose().popPose();

        } else {
            // VERTICAL LAYOUT ICONS (Your exact logic)
            guiGraphics.pose().pushPose();
            float oxyIconScale = scale * (6.0f / 16.0f);
            guiGraphics.pose().scale(oxyIconScale, oxyIconScale, 1.0f);

            int oxyCol1 = (int) ((hudX + (3 * scale)) / oxyIconScale);
            int oxyCol2 = (int) ((hudX + (11 * scale)) / oxyIconScale);
            int row1    = (int) ((hudY + (1 * scale)) / oxyIconScale);

            renderSlot(guiGraphics, inventory.getStackInSlot(0), OXYGEN_MASK_ICON, oxyCol1, row1);
            renderSlot(guiGraphics, inventory.getStackInSlot(1), CONNECTOR_ICON, oxyCol2, row1);
            guiGraphics.pose().popPose();

            guiGraphics.pose().pushPose();
            float thermIconScale = scale * (4.0f / 16.0f);
            guiGraphics.pose().scale(thermIconScale, thermIconScale, 1.0f);

            int thermCol1 = (int) ((hudX + (1 * scale)) / thermIconScale);
            int thermCol2 = (int) ((hudX + (5 * scale)) / thermIconScale);
            int thermCol3 = (int) ((hudX + (10 * scale)) / thermIconScale);
            int thermCol4 = (int) ((hudX + (14 * scale)) / thermIconScale);
            int row2      = (int) ((hudY + (7 * scale)) / thermIconScale);

            renderSlot(guiGraphics, inventory.getStackInSlot(4), HEAD_ICON, thermCol1, row2);
            renderSlot(guiGraphics, inventory.getStackInSlot(5), TORSO_ICON, thermCol2, row2);
            renderSlot(guiGraphics, inventory.getStackInSlot(6), LEGS_ICON, thermCol3, row2);
            renderSlot(guiGraphics, inventory.getStackInSlot(7), HANDS_ICON, thermCol4, row2);

            guiGraphics.pose().popPose();
        }

        // --- LIQUID FILLS ---
        int maxHazardFill = (int) (14 * scale);

        int radFill = getHazardScaled(radiationAmount, radiationCapacity, maxHazardFill);
        int pressFill = getPressureScaled(pressureAmount, maxHazardFill);

        int radColor = getRadiationColor(radiationAmount);
        int pressColor = getPressureColor(pressureAmount);

        guiGraphics.fill(radBarX + (int)(1 * scale), radBarY + (int)(1 * scale),
                radBarX + (int)(1 * scale) + radFill, radBarY + (int)(3 * scale), radColor);

        guiGraphics.fill(pressBarX + (int)(1 * scale), pressBarY + (int)(1 * scale),
                pressBarX + (int)(1 * scale) + pressFill, pressBarY + (int)(3 * scale), pressColor);

        int maxOxyHeight = (int) (14 * scale);
        int oxyBottomY = tankY + (int) (15 * scale);

        int oxyFill1 = getHazardScaled(oxygenAmount1, oxygenCapacity1, maxOxyHeight);
        int oxyFill2 = getHazardScaled(oxygenAmount2, oxygenCapacity2, maxOxyHeight);

        int liqWidth = (int) (4 * scale);
        guiGraphics.fill(tankX + (int)(2 * scale), oxyBottomY - oxyFill1, tankX + (int)(2 * scale) + liqWidth, oxyBottomY, 0xFF00FFFF);
        guiGraphics.fill(tankX + (int)(10 * scale), oxyBottomY - oxyFill2, tankX + (int)(10 * scale) + liqWidth, oxyBottomY, 0xFF00FFFF);

        // --- DRAW ALL TEXTURES OVER THE LIQUIDS ---
        guiGraphics.blit(HAZARD_LEVEL_H, radBarX, radBarY, hazardWidth, hazardHeight, 0, 0, 16, 4, 16, 4);
        guiGraphics.blit(HAZARD_LEVEL_H, pressBarX, pressBarY, hazardWidth, hazardHeight, 0, 0, 16, 4, 16, 4);
        guiGraphics.blit(OXYGEN_TANK, tankX, tankY, tankWidth, tankHeight, 0, 0, 16, 16, 16, 16);

        // --- DRAW ALL TEXT ---
        Font font = mc.font;
        guiGraphics.pose().pushPose();
        float textScale = scale * 0.35f;
        guiGraphics.pose().scale(textScale, textScale, 1.0f);

        guiGraphics.drawString(font, "Radiation:", (int) (radBarX / textScale), (int) (radTextY / textScale), 0xFFFFFF, false);
        guiGraphics.drawString(font, "Pressure:", (int) (pressBarX / textScale), (int) (pressTextY / textScale), 0xFFFFFF, false);
        guiGraphics.drawString(font, "Oxygen:", (int) (tankX / textScale), (int) (oxyTextY / textScale), 0xFFFFFF, false);

        guiGraphics.pose().popPose();
    }

    //region Helpers
    public int getHazardScaled(double amount, double capacity, int maxPixels) {
        if (capacity == 0 || amount <= 0) return 0;
        int rawPixels = (int) ((amount * maxPixels) / capacity);
        return Math.min(rawPixels, maxPixels);
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
