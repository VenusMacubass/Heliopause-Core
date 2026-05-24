package net.venera.heliocore.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.heliocore.HeliopauseCore;

public class RocketScreen  extends AbstractContainerScreen<RocketMenu> {
    private static final ResourceLocation ROCKET_GUI = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/tier1_rocket/tier1_rocket.png");
    private static final ResourceLocation FUEL_GUI = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/fuel_gui.png");
    
    public RocketScreen(RocketMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ROCKET_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(ROCKET_GUI, x, y, 0, 0, 175, 123); //Gui
        
        int scaledHeight = menu.getFuelScaled(41);

        if (scaledHeight > 0) {
            int emptySpace = 41 - scaledHeight;

            guiGraphics.blit(
                    FUEL_GUI,
                    x + 152,                  // X stays the same
                    y + 17 + emptySpace,     // Push the render start down by the empty space
                    0,                      // Texture U stays the same
                    emptySpace,         // Push the texture read start down to match
                    16,                  // Width stays the same
                    scaledHeight               // Only draw the remaining scaled height!
            );
        }
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 4, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 53, 0x404040, false);
    }
}
