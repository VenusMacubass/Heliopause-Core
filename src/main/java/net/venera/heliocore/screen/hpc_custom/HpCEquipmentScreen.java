package net.venera.heliocore.screen.hpc_custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.heliocore.HeliopauseCore;

public class HpCEquipmentScreen extends AbstractContainerScreen<HpCEquipmentMenu> {
    private static final ResourceLocation EQUIPMENT_GUI = ResourceLocation.fromNamespaceAndPath(
            HeliopauseCore.MOD_ID, "textures/gui/player_gui/heliopause_player_gui.png"
    );

    public HpCEquipmentScreen(HpCEquipmentMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 165;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        
        guiGraphics.blit(EQUIPMENT_GUI, x, y, 0, 0, this.imageWidth, this.imageHeight);
        
        if (this.menu.targetEntity != null) {
            int boxStartX = x + 46;
            int boxStartY = y + 8;
            int boxEndX = x + 94;
            int boxEndY = y + 77;
            int scale = 30; 

            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    guiGraphics,
                    boxStartX,
                    boxStartY,
                    boxEndX,
                    boxEndY,
                    scale,
                    0.0625F, 
                    pMouseX,
                    pMouseY,
                    this.menu.targetEntity
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }
    
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick); 
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
