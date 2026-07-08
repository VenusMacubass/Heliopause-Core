package net.venera.heliocore.screen.hpc_custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.heliocore.HeliopauseCore;

public class LanderScreen  extends AbstractContainerScreen<LanderMenu> {
    private static final ResourceLocation LANDER_GUI = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/tier1_rocket/tier1_rocket_lander.png");
    private static final ResourceLocation FUEL_GUI = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/fuel_gui.png");

    public LanderScreen(LanderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    int offsetY = -25;
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, LANDER_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2 + offsetY;
        guiGraphics.blit(LANDER_GUI, x, y, 0, 0, 175, 209); //Gui

//        int chargeLength = menu.getEnergyScaled(54);
//        if (chargeLength > 0) {
//            int startX = x + 59;
//            int startY = y + 51;
//            int endX = startX + chargeLength;
//            int endY = startY + 7;
//
//            //(FF: opacity, FF being opaque), rest is rgb
//            guiGraphics.fill(startX, startY, endX, endY, 0xFFFFE400);
//        }

        int scaledHeight = menu.getFuelScaled(41);
        if (scaledHeight > 0) {
            int emptySpace = 41 - scaledHeight;

            guiGraphics.blit(
                    FUEL_GUI,
                    x + 152,                // Screen X
                    y + 17 + emptySpace,     // Screen Y (Pushed down)
                    0,                      // Texture U (Starts at 0 on the file)
                    emptySpace,             // Texture V (Pushed down)
                    16,                     // Render Width
                    scaledHeight,           // Render Height
                    16,                     // THE FIX: Actual width of the PNG file
                    41                      // THE FIX: Actual height of the PNG file
            );
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int imageX = (width - imageWidth) / 2;
        int imageY = (height - imageHeight) / 2 + offsetY;

        int energyX = imageX + 59;
        int energyY = imageY + 51;
        int energyWidth = 54;
        int energyHeight = 7;
        int fuelX = imageX + 152; //124/17
        int fuelY = imageY + 15;
        int fuelWidth = 16;
        int fuelHeight = 41;

//        if (isMouseOver(x, y, energyX, energyY, energyWidth, energyHeight)) {
//            int currentEnergy = menu.lander.getEnergyAmount();
//            int maxEnergy = menu.lander.MAX_ENERGY;
//
//            guiGraphics.renderTooltip(font, Component.literal(
//                    "Energy: " + currentEnergy + " / " + maxEnergy + " FE"), x, y);
//        }
        if (isMouseOver(x, y, fuelX, fuelY, fuelWidth, fuelHeight)) {
            int currentFuel = menu.lander.getFuelAmount();
            int maxFuel = menu.lander.MAX_FUEL;

            guiGraphics.renderTooltip(font, Component.literal(
                    "Fuel: " + currentFuel + " / " + maxFuel + " mL"), x, y);
        }

    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 52 + offsetY, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 117 + offsetY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}

