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

public class OxygenSealerScreen extends AbstractContainerScreen<OxygenSealerMenu> {
    private Button enabilitationButton;

    private static final ResourceLocation ENERGY_GENERATOR_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/oxygen_sealer/oxygen_sealer_gui.png");
    private static final ResourceLocation OXYGEN_GAS_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/oxygen_gas_gui.png");

    public OxygenSealerScreen(OxygenSealerMenu menu, Inventory playerInventory, Component title) {
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
                .bounds(x + 93, y + 47, 36, 18)
                .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ENERGY_GENERATOR_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(ENERGY_GENERATOR_GUI, x, y, 0, 0, 175, 170);

        int scaledFuel = menu.getOxygenScaled(41);
        if (scaledFuel > 0) {
            int emptySpace = 41 - scaledFuel;
            guiGraphics.blit(OXYGEN_GAS_GUI, x + 8, y + 35 + emptySpace, 0, emptySpace, 16, scaledFuel, 16, 41);
        }

        int chargeLength = menu.getEnergyScaled(54);
        if (chargeLength > 0) {
            int startX = x + 94;
            int startY = y + 69;
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

        int fuelX = x + 8;
        int fuelY = y + 35;
        int fuelWidth = 16;
        int fuelHeight = 41;
        int energyX = x + 94;
        int energyY = y + 69;
        int energyWidth = 54;
        int energyHeight = 7;

        if (isMouseOver(mouseX, mouseY, fuelX, fuelY, fuelWidth, fuelHeight)) {
            int currentOxygen = menu.blockEntity.oxygenTank.getFluidAmount();
            int capacity = menu.blockEntity.oxygenTank.getCapacity();

            guiGraphics.renderTooltip(font,
                    Component.literal("Oxygen: " + currentOxygen + " mB / " + capacity + " mB"),
                    mouseX, mouseY
            );
        }

        if (isMouseOver(mouseX, mouseY, energyX, energyY, energyWidth, energyHeight)) {
            int currentEnergy = menu.data.get(2);
            int capacity = menu.data.get(3);

            guiGraphics.renderTooltip(font,
                    Component.literal("Energy: " + currentEnergy + " FE / " + capacity + " FE"),
                    mouseX, mouseY
            );
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.enabilitationButton.setMessage(Component.literal(menu.data.get(5) > 0 ? "Disable" : "Enable"));
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 3, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 78, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
