package net.venera.heliocore.screen.block_entity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.heliocore.HeliopauseCore;

public class MagneticAssemblyPlatformScreen extends AbstractContainerScreen<MagneticAssemblyPlatformMenu> {
    private static final ResourceLocation PLATFORM_GUI = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/magnetic_assembly_platform/magnetic_assembly_platform.png");
    
    public MagneticAssemblyPlatformScreen(MagneticAssemblyPlatformMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 175;
        this.imageHeight = 239;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PLATFORM_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(PLATFORM_GUI, x, y, 0, 0, 175, 239);
        
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 4, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 146, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
