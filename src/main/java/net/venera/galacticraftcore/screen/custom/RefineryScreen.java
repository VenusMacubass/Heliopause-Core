package net.venera.galacticraftcore.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.galacticraftcore.GalacticraftCore;

public class RefineryScreen extends AbstractContainerScreen<RefineryMenu> {
    private static final ResourceLocation REFINERY_GUI =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "textures/gui/refinery/refinery_gui.png");

    public RefineryScreen(RefineryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, REFINERY_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(REFINERY_GUI, x, y, 0, 0, 175, 167);

        int oilHeight = menu.blockEntity.getOilScaled(38);
        if(oilHeight > 0) {
            int renderY = y + 28 + (38 - oilHeight);
            guiGraphics.blit(REFINERY_GUI, x + 7, renderY, 176, 38 - oilHeight, 16, oilHeight);

        }

        int fuelHeight = menu.blockEntity.getFuelScaled(38);
        if(fuelHeight > 0) {
            int renderY = y + 28 + (38 - fuelHeight);
            guiGraphics.blit(REFINERY_GUI, x + 153, renderY, 192, 38 - fuelHeight, 16, fuelHeight);

        }

        if(menu.isActive()){
            guiGraphics.blit(REFINERY_GUI, x + 49, y + 16, 208, 0, 11, 11);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int oilX = x + 7;  // X of the tank texture
        int oilY = y + 28;   // Y of the top of the tank
        int oilWidth = 16;   // Width in pixels
        int oilHeight = 38;   //Height in pixel
        int fuelX = x + 153;
        int fuelY = y + 28;
        int fuelWidth = 16;
        int fuelHeight = 38;

        if (isMouseOver(mouseX, mouseY, oilX, oilY, oilWidth, oilHeight)) {
            int currentOil = menu.blockEntity.getOilAmount();
            int capacity = menu.blockEntity.getMaxCapacity();

            guiGraphics.renderTooltip(font,
                    Component.literal("Crude Oil: " + currentOil + "mB / " + capacity + "mB"),
                    mouseX, mouseY
            );
        }

        if (isMouseOver(mouseX, mouseY, fuelX, fuelY, fuelWidth, fuelHeight)) {
            int currentFuel = menu.blockEntity.getFuelAmount();
            int capacity = menu.blockEntity.getMaxCapacity();

            guiGraphics.renderTooltip(font,
                    Component.literal("Fuel: " + currentFuel + "mB / " + capacity + "mB"),
                    mouseX, mouseY
            );
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title,
                (imageWidth/2 - 14), 6,
                0x404040, false);

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
