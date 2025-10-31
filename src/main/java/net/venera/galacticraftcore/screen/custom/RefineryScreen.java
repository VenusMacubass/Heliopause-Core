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
        int oilHeight = menu.blockEntity.getOilScaled(37);
        if(oilHeight > 0) {
            guiGraphics.blit(REFINERY_GUI, x + 7, y + 28, 176, (37 - oilHeight), 15, 37);

        }

        int fuelHeight = menu.blockEntity.getFuelScaled(37);
        if(fuelHeight > 0) {
            guiGraphics.blit(REFINERY_GUI, x + 153, y + 28, 192, (37 - fuelHeight), 15, 37);

        }

        if(menu.isActive()){
            guiGraphics.blit(REFINERY_GUI, x + 50, y + 17, 208, 0, 10, 10);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
