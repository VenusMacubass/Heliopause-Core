package net.venera.heliocore.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.heliocore.HeliopauseCore;

public class CoalCompressorScreen extends AbstractContainerScreen<CoalCompressorMenu> {
    private static final ResourceLocation COAL_COMPRESSOR_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/coal_compressor/coal_compressor_gui.png");
    private static final ResourceLocation FIRE_ICON =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/lit_fire.png");
    private static final ResourceLocation COMPRESSING = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/compressing.png");
    private static final ResourceLocation PROGRESS_ARROW = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/progress_arrow_long.png");

    public CoalCompressorScreen(CoalCompressorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, COAL_COMPRESSOR_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(COAL_COMPRESSOR_GUI, x, y, 0, 0, 175, 165);
        int progressWidth = menu.getArrowScaled(34);
        if (progressWidth > 0){
            //guiGraphics.blit(SOURCE_GUI, whereToDrawX, whereToDrawY, textureX, textureY, textureW, textureH);
            guiGraphics.blit(PROGRESS_ARROW, x + 85, y + 35, 0, 0, progressWidth, 15, 33, 15);
            //guiGraphics.blit(COAL_COMPRESSOR_GUI, x + 101, y + 29, 176, 0, (progressWidth > 23 ? progressWidth - 23 : 0), 13);
        }
        
        if(menu.getActivation() && menu.getCompression()){
            guiGraphics.blit(COMPRESSING, x + 91, y + 26, 0, 0, 15, 13, 15, 13);
        }

        int fireHeight = menu.getFireIconScaled(13);
        if (fireHeight > 0) {
            guiGraphics.blit(FIRE_ICON,
                    x + 67,                        // 1. Destination X on Screen
                    y + 36 + (13 - fireHeight),    // 2. Destination Y on Screen (Pushes the starting point down)
                    0.0F,                          // 3. Texture U (Left edge of your PNG)
                    (float) (13 - fireHeight),     // 4. Texture V (Starts reading lower in your PNG)
                    13,                            // 5. Draw Width
                    fireHeight,                    // 6. Draw Height (Only draw the remaining active pixels)
                    13,                            // 7. Total PNG Width
                    13                             // 8. Total PNG Height
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 4, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 73, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
