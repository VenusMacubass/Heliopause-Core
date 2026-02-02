package net.venera.galacticraftcore.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.galacticraftcore.GalacticraftCore;

public class BasicSolarScreen extends AbstractContainerScreen<BasicSolarMenu> {
    private static final ResourceLocation BASIC_SOLAR_SCREEN_GUI = 
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID,"textures/gui/solar_panel/solar_gui.png");

    public BasicSolarScreen(BasicSolarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BASIC_SOLAR_SCREEN_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(BASIC_SOLAR_SCREEN_GUI, x, y, 0, 0, 175, 200); //Gui

        if(menu.data.get(0) > 0){ 
            guiGraphics.blit(BASIC_SOLAR_SCREEN_GUI, x + 82, y + 24, 176, 0, 11, 10); //Lightning Icon
        }
        if(menu.data.get(4) > 0) {
            
            guiGraphics.blit(BASIC_SOLAR_SCREEN_GUI, x + 48, y + 21, 176, 10, 18, 18); //Sun Icon  
        }

        int currentEnergy = menu.data.get(0);
        int capacity = menu.data.get(1);
        int chargeLength = 0;
        if (capacity > 0 && currentEnergy > 0) {
            if (currentEnergy >= capacity) {
                chargeLength = 54;
            } else {
                chargeLength = Math.round(((float) currentEnergy / capacity) * 54);
            }
        }
        if(menu.data.get(0) > 0){ //Energy Bar
            guiGraphics.blit(BASIC_SOLAR_SCREEN_GUI, x + 97, y + 25, 187, 0, chargeLength, 7);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int imageX = (width - imageWidth) / 2;
        int imageY = (height - imageHeight) / 2;

        int energyX = imageX + 97;
        int energyY = imageY + 25;
        int energyWidth = 54;
        int energyHeight = 7;
        int sunX = imageX + 47;
        int sunY = imageY + 20;
        int sunArea = 18;
        
        if(isMouseOver(x, y, energyX, energyY, energyWidth, energyHeight)){ 
            int currentEnergy = menu.data.get(0);
            int maxEnergy = menu.data.get(1);
            
            guiGraphics.renderTooltip(font, Component.literal( 
                    "Energy: " + currentEnergy + " / " + maxEnergy + " FE"), x, y);
        }

        if(isMouseOver(x, y, sunX, sunY, sunArea, sunArea)){
            guiGraphics.renderTooltip(font, Component.literal(
                            "Generating Energy"),x,y);
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
