package net.venera.galacticraftcore.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.venera.galacticraftcore.GalacticraftCore;

public class CoalCompressorScreen extends AbstractContainerScreen<CoalCompressorMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "textures/gui/coal_compressor/coal_compressor_gui.png");

    public CoalCompressorScreen(CoalCompressorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, 175, 191);
        int progressWidth = menu.blockEntity.getArrowScaled(52);
        if (progressWidth > 0){
            guiGraphics.blit(GUI_TEXTURE, x + 77, y + 37, 176, 13, progressWidth + 1, 16);
            guiGraphics.blit(GUI_TEXTURE, x + 101, y + 29, 176, 0, (progressWidth > 23 ? progressWidth - 23 : 0), 13);}

        int fireHeight = menu.blockEntity.getFireIconScaled(13);
        if (fireHeight > 0){
            guiGraphics.blit(GUI_TEXTURE, x + 81, (y + 27 + (13 - fireHeight)), 176, 30 + (13 - fireHeight), 13, (fireHeight+1));
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

}
