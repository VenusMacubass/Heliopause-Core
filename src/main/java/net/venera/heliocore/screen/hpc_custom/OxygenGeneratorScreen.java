package net.venera.heliocore.screen.hpc_custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.util.MachineButtonHelper;

public class OxygenGeneratorScreen extends AbstractContainerScreen<OxygenGeneratorMenu> {
    private Button toggleButton;

    private static final ResourceLocation OXYGEN_GENERATOR_SCREEN_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/oxygen_generator/oxygen_generator.png");

    public OxygenGeneratorScreen(OxygenGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.toggleButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(6) > 0 ? "Disable" : "Enable"),
                        button -> {
                            PacketDistributor.sendToServer(
                                    new MachineButtonHelper(menu.blockEntity.getBlockPos(), 0)
                            );
                        })
                .bounds(x + 70, y + 12, 36, 18)
                .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, OXYGEN_GENERATOR_SCREEN_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(OXYGEN_GENERATOR_SCREEN_GUI, x, y, 0, 0, 176, 132); //Gui

        

        int chargeLength = menu.getEnergyScaled(54);
        if (chargeLength > 0) {
            int startX = x + 28;
            int startY = y + 33;
            int endX = startX + chargeLength;
            int endY = startY + 7;
            
            guiGraphics.fill(startX, startY, endX, endY, 0xFFFFE400);
        }

        int oxygenLength = menu.getOxygenScaled(54);
        if (oxygenLength > 0) {
            int startX = x + 94;
            int startY = y + 33;
            int endX = startX + oxygenLength;
            int endY = startY + 7;
            
            guiGraphics.fill(startX, startY, endX, endY, 0xFF3FBCEF);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int imageX = (width - imageWidth) / 2;
        int imageY = (height - imageHeight) / 2;

        int energyX = imageX + 28;
        int energyY = imageY + 33;
        int energyWidth = 54;
        int energyHeight = 7;
        int oxygenX = imageX + 94;
        int oxygenY = imageY + 33;
        int oxygenWidth = 54;
        int oxygenHeight = 7;

        if(isMouseOver(x, y, energyX, energyY, energyWidth, energyHeight)){
            int currentEnergy = menu.data.get(0);
            int maxEnergy = menu.data.get(1);

            guiGraphics.renderTooltip(font, Component.literal(
                    "Energy: " + currentEnergy + " / " + maxEnergy + " FE"), x, y);
        }

        if(isMouseOver(x, y, oxygenX, oxygenY, oxygenWidth, oxygenHeight)) {
            int currentOxygen = menu.data.get(2);
            int maxOxygen = menu.data.get(3);

            guiGraphics.renderTooltip(font, Component.literal(
                    "Oxygen: " + currentOxygen + " / " + maxOxygen + " FO"), x, y);
        }
    }
    
    @Override
    protected void containerTick() {
        super.containerTick();
        this.toggleButton.setMessage(Component.literal(menu.data.get(6) > 0 ? "Disable" : "Enable"));
    }
    
    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 4, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 41, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
