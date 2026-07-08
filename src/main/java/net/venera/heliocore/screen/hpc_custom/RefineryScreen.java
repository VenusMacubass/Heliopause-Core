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

public class RefineryScreen extends AbstractContainerScreen<RefineryMenu> {
    private Button enabilitationButton;
    
    private static final ResourceLocation REFINERY_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/refinery/refinery_gui.png");
    private static final ResourceLocation OIL_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/oil_gui.png");
    private static final ResourceLocation FUEL_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/fuel_gui.png");

    public RefineryScreen(RefineryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.enabilitationButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(6) > 0 ? "Disable" : "Enable"),
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
        RenderSystem.setShaderTexture(0, REFINERY_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(REFINERY_GUI, x, y, 0, 0, 175, 146);

        int scaledOil = menu.getOilScaled(41);
        if (scaledOil > 0) {
            int emptySpace = 41 - scaledOil;
            guiGraphics.blit(OIL_GUI, x + 8, y + 13 + emptySpace, 0, emptySpace, 16, scaledOil, 16, 41);
        }

        int scaledFuel = menu.getFuelScaled(41);
        if (scaledFuel > 0) {
            int emptySpace = 41 - scaledFuel;
            guiGraphics.blit(FUEL_GUI, x + 152, y + 13 + emptySpace, 0, emptySpace, 16, scaledFuel, 16, 41);
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

        int oilX = x + 8;    //X of the tank texture
        int oilY = y + 13;   //Y of the top of the tank
        int oilWidth = 16;   //Width in pixels
        int oilHeight = 41;  //Height in pixel
        int fuelX = x + 152;
        int fuelY = y + 13;
        int fuelWidth = 16;
        int fuelHeight = 41;
        int energyX = x + 61;
        int energyY = y + 13;
        int energyWidth = 54;
        int energyHeight = 7;

        if (isMouseOver(mouseX, mouseY, oilX, oilY, oilWidth, oilHeight)) {
            int currentOil = menu.blockEntity.getOilAmount();
            int capacity = menu.blockEntity.getMaxCapacity();

            guiGraphics.renderTooltip(font,
                    Component.literal("Crude Oil: " + currentOil + " mB / " + capacity + " mB"),
                    mouseX, mouseY
            );
        }

        if (isMouseOver(mouseX, mouseY, fuelX, fuelY, fuelWidth, fuelHeight)) {
            int currentFuel = menu.blockEntity.getFuelAmount();
            int capacity = menu.blockEntity.getMaxCapacity();

            guiGraphics.renderTooltip(font,
                    Component.literal("Fuel: " + currentFuel + " mB / " + capacity + " mB"),
                    mouseX, mouseY
            );
        }

        if (isMouseOver(mouseX, mouseY, energyX, energyY, energyWidth, energyHeight)) {
            int currentEnergy = menu.data.get(4);
            int capacity = menu.data.get(5);

            guiGraphics.renderTooltip(font,
                    Component.literal("Energy: " + currentEnergy + " FE / " + capacity + " FE"),
                    mouseX, mouseY
            );
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.enabilitationButton.setMessage(Component.literal(menu.data.get(6) > 0 ? "Disable" : "Enable"));
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
