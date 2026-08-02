package net.venera.heliocore.screen.hpc_custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.entity.machine.electric.PCBFabricatorEntity;

public class PCBFabricatorScreen extends AbstractContainerScreen<PCBFabricatorMenu> {
    private static final ResourceLocation PCB_FABRICATOR_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/pcb_fabricator/pcb_fabricator_gui.png");
    private static final ResourceLocation PROGRESS_ARROW =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/progress_arrow_short.png");
    
    public PCBFabricatorScreen(PCBFabricatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PCB_FABRICATOR_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(PCB_FABRICATOR_GUI, x, y, 0, 0, 175, 165); //175 165

        int chargeLength = menu.getEnergyScaled(54);
        if (chargeLength > 0) {
            int startX = x + 114;
            int startY = y + 72;
            int endX = startX + chargeLength;
            int endY = startY + 7;
            guiGraphics.fill(startX, startY, endX, endY, 0xFFFFE400);
        }

        int progressWidth = menu.getArrowScaled(22);
        if (progressWidth > 0){
            guiGraphics.blit(PROGRESS_ARROW, x + 124, y + 35, 0, 0, progressWidth, 15, 22, 15);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        
        int energyX = x + 94;
        int energyY = y + 69;
        int energyWidth = 54;
        int energyHeight = 7;

        if (isMouseOver(mouseX, mouseY, energyX, energyY, energyWidth, energyHeight)) {
            guiGraphics.renderTooltip(font,
                    Component.literal("Energy: " + menu.getEnergy() + " FE / " + menu.getMaxEnergy() + " FE"),
                    mouseX, mouseY
            );
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 3, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 73, 0x404040, false);
        
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
