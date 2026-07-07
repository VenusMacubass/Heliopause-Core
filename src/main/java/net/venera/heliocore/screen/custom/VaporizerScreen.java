package net.venera.heliocore.screen.custom;

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

public class VaporizerScreen  extends AbstractContainerScreen<VaporizerMenu> {
    private Button enabilitationButton;

    private static final ResourceLocation VAPORIZER_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/refinery/refinery_gui.png");
    private static final ResourceLocation GAS_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/oxygen_gas_gui.png");
    private static final ResourceLocation LIQUID_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/oxygen_gui.png");

    public VaporizerScreen(VaporizerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.enabilitationButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(8) > 0 ? "Disable" : "Enable"),
                        button -> {
                            PacketDistributor.sendToServer(
                                    new MachineButtonHelper(menu.blockEntity.getBlockPos(), 0)
                            );
                        })
                .bounds(x + 81, y + 23, 35, 18)
                .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, VAPORIZER_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(VAPORIZER_GUI, x, y, 0, 0, 175, 146);

        int scaledLiquid = menu.getLiquidScaled(41);
        if (scaledLiquid > 0) {
            int emptySpace = 41 - scaledLiquid;
            guiGraphics.blit(LIQUID_GUI, x + 8, y + 13 + emptySpace, 0, emptySpace, 16, scaledLiquid, 16, 41);
        }

        int scaledGas = menu.getGasScaled(41);
        if (scaledGas > 0) {
            int emptySpace = 41 - scaledGas;
            guiGraphics.blit(GAS_GUI, x + 152, y + 13 + emptySpace, 0, emptySpace, 16, scaledGas, 16, 41);
        }

        int chargeLength = menu.getEnergyScaled(54);
        if (chargeLength > 0) {
            int startX = x + 61;
            int startY = y + 13;
            int endX = startX + chargeLength;
            int endY = startY + 7;
            guiGraphics.fill(startX, startY, endX, endY, 0xFFFFE400);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int gasX = x + 8; 
        int gasY = y + 13;   
        int gasWidth = 16;   
        int gasHeight = 41;   
        int liquidX = x + 152;
        int liquidY = y + 13;
        int liquidWidth = 16;
        int liquidHeight = 41;
        int energyX = x + 61;
        int energyY = y + 13;
        int energyWidth = 54;
        int energyHeight = 7;

        if (isMouseOver(mouseX, mouseY, gasX, gasY, gasWidth, gasHeight)) {
            int currentLiquid = menu.blockEntity.liquidTank.getFluidAmount();
            int capacity = menu.blockEntity.liquidTank.getCapacity();

            guiGraphics.renderTooltip(font,
                    Component.literal("Liquid: " + currentLiquid + " mB / " + capacity + " mB"),
                    mouseX, mouseY
            );
        }

        if (isMouseOver(mouseX, mouseY, liquidX, liquidY, liquidWidth, liquidHeight)) {
            int currentGas = menu.blockEntity.gasTank.getFluidAmount();
            int capacity = menu.blockEntity.gasTank.getCapacity();

            guiGraphics.renderTooltip(font,
                    Component.literal("Gas: " + currentGas + " mB / " + capacity + " mB"),
                    mouseX, mouseY
            );
        }

        if (isMouseOver(mouseX, mouseY, energyX, energyY, energyWidth, energyHeight)) {
            int currentEnergy = menu.data.get(6);
            int capacity = menu.data.get(7);

            guiGraphics.renderTooltip(font,
                    Component.literal("Energy: " + currentEnergy + " FE / " + capacity + " FE"),
                    mouseX, mouseY
            );
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.enabilitationButton.setMessage(Component.literal(menu.data.get(8) > 0 ? "Disable" : "Enable"));
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 3, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 55, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
