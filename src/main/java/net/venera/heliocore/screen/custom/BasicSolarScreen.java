package net.venera.heliocore.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.util.SolarPanelHelper;
public class BasicSolarScreen extends AbstractContainerScreen<BasicSolarMenu> {
    private Button toggleButton;
    
    private static final ResourceLocation BASIC_SOLAR_SCREEN_GUI = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/solar_panel/solar_gui.png");
    private static final ResourceLocation ENERGY_ACTIVITY_ICON = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/energy_activity_icon.png");
    private static final ResourceLocation SUN_ICON_INACTIVE = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/solar_inactivity.png");
    private static final ResourceLocation SUN_ICON_ACTIVE =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/solar_activity.png");
    
            

    public BasicSolarScreen(BasicSolarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        
        this.toggleButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(5) > 0 ? "Disable" : "Enable"),
                        button -> {
                            PacketDistributor.sendToServer(
                                    new SolarPanelHelper(menu.blockEntity.getBlockPos())
                            );
                        })
                .bounds(x + 107, y + 28, 40, 18)
                .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BASIC_SOLAR_SCREEN_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(BASIC_SOLAR_SCREEN_GUI, x, y, 0, 0, 176, 133); //Gui
        guiGraphics.blit(SUN_ICON_INACTIVE, x+15, y+22, 0, 0, 8, 8 , 8, 8); //Sun Icon (Inactive)

        if(menu.data.get(0) > 0){ 
            //guiGraphics.blit(SOURCE_GUI, whereToDrawX, whereToDrawY, textureX, textureY, textureW, textureH, imageW, imageH);
            guiGraphics.blit(ENERGY_ACTIVITY_ICON, x+35, y+24, 0, 0, 9, 13 , 9, 13); //Lightning Icon
        }
        if(menu.data.get(4) > 0) {
            guiGraphics.blit(SUN_ICON_ACTIVE, x+15, y+22, 0, 0, 8, 8, 8, 8); //Sun Icon  
            
        }
        
        int chargeLength = menu.getEnergyScaled(54);
        
        if (chargeLength > 0) {
            int startX = x + 36;
            int startY = y + 15;
            int endX = startX + chargeLength;
            int endY = startY + 7;
            
            //(FF: opacity, FF being opaque), rest is rgb
            guiGraphics.fill(startX, startY, endX, endY, 0xFFFFE400);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int imageX = (width - imageWidth) / 2;
        int imageY = (height - imageHeight) / 2;

        int energyX = imageX + 36;
        int energyY = imageY + 15;
        int energyWidth = 54;
        int energyHeight = 7;
        int sunX = imageX + 15;
        int sunY = imageY + 22;
        int sunArea = 16;
        
        if(isMouseOver(x, y, energyX, energyY, energyWidth, energyHeight)){ 
            int currentEnergy = menu.data.get(0);
            int maxEnergy = menu.data.get(1);
            
            guiGraphics.renderTooltip(font, Component.literal( 
                    "Energy: " + currentEnergy + " / " + maxEnergy + " FE"), x, y);
        }

        if(isMouseOver(x, y, sunX, sunY, sunArea, sunArea)) {
            if(menu.data.get(4) > 0) {
                guiGraphics.renderTooltip(font, Component.literal(
                        "Sun is visible."), x, y);
            } else {
                guiGraphics.renderTooltip(font, Component.literal(
                        "Sun is not visible."), x, y);
            }
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.toggleButton.setMessage(Component.literal(menu.data.get(5) > 0 ? "Disable" : "Enable"));
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
    

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 4, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 40, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
