package net.venera.galacticraftcore.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.venera.galacticraftcore.GalacticraftCore;

public class EnergyStorageUnitScreen extends AbstractContainerScreen<EnergyStorageUnitMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "textures/gui/energy_storage_unit/energy_storage_unit.png");

    public EnergyStorageUnitScreen(EnergyStorageUnitMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, 175, 165);

        int currentEnergy = menu.data.get(0);
        int capacity = menu.data.get(1);
        int chargeLength = 0;
        if (capacity > 0 && currentEnergy > 0) {
            if (currentEnergy >= capacity) {
                chargeLength = 72;
            } else {
                chargeLength = Math.round(((float) currentEnergy / capacity) * 72);
            }
        }
        guiGraphics.blit(GUI_TEXTURE, x + 87, y + 52, 176, 0, chargeLength, 3);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
