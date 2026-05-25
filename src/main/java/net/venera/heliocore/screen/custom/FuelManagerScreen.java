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

public class FuelManagerScreen extends AbstractContainerScreen<FuelManagerMenu> {
    private Button inputEnableButton;
    private Button outputEnableButton;

    private static final ResourceLocation FUEL_MANAGER_GUI =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/fuel_manager/fuel_manager_gui.png");
    private static final ResourceLocation FUEL_GUI = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"textures/gui/fuel_gui.png");

    public FuelManagerScreen(FuelManagerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.inputEnableButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(6) > 0 ? "Stop Loading" : "Load Fuel"),
                        button -> {
                            PacketDistributor.sendToServer(
                                    new MachineButtonHelper(menu.blockEntity.getBlockPos(), 0)
                            );
                        })
                .bounds(x + 73, y + 5, 36, 15)
                .build()
        );

        this.outputEnableButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(7) > 0 ? "Stop Charging" : "Charge"),
                        button -> {
                            PacketDistributor.sendToServer(
                                    new MachineButtonHelper(menu.blockEntity.getBlockPos(), 1)
                            );
                        })
                .bounds(x + 73, y + 22, 36, 15)
                .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, FUEL_MANAGER_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(FUEL_MANAGER_GUI, x, y, 0, 0, 176, 174); //Gui

        int scaledHeight = menu.getFuelScaled(41);

        if (scaledHeight > 0) {
            int emptySpace = 41 - scaledHeight;

            guiGraphics.blit(
                    FUEL_GUI,
                    x + 152,                // Screen X
                    y + 6 + emptySpace,     // Screen Y (Pushed down)
                    0,                      // Texture U (Starts at 0 on the file)
                    emptySpace,             // Texture V (Pushed down)
                    16,                     // Render Width
                    scaledHeight,           // Render Height
                    16,                     // THE FIX: Actual width of the PNG file
                    41                      // THE FIX: Actual height of the PNG file
            );
        }

        int chargeLength = menu.getEnergyScaled(54);

        if (chargeLength > 0) {
            int startX = x + 74;
            int startY = y + 40;
            int endX = startX + chargeLength;
            int endY = startY + 7;

            guiGraphics.fill(startX, startY, endX, endY, 0xFFFFE400);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int imageX = (width - imageWidth) / 2;
        int imageY = (height - imageHeight) / 2;

        int energyX = imageX + 74;
        int energyY = imageY + 40;
        int energyWidth = 54;
        int energyHeight = 7;
        int fuelX = imageX + 152;
        int fuelY = imageY + 6;
        int fuelWidth = 16;
        int fuelHeight = 41;
        
        if (isMouseOver(x, y, energyX, energyY, energyWidth, energyHeight)) {
            int currentEnergy = menu.data.get(0);
            int maxEnergy = menu.data.get(1);

            guiGraphics.renderTooltip(font, Component.literal(
                    "Energy: " + currentEnergy + " / " + maxEnergy + " FE"), x, y);
        }
        if (isMouseOver(x, y, fuelX, fuelY, fuelWidth, fuelHeight)) {
            int currentFuel = menu.data.get(2);
            int maxFuel = menu.data.get(3);

            guiGraphics.renderTooltip(font, Component.literal(
                    "Fuel: " + currentFuel + " / " + maxFuel + " mL"), x, y);
        }
        
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.inputEnableButton.setMessage(Component.literal(menu.data.get(6) > 0 ? "Stop Loading" : "Load Fuel"));
        this.outputEnableButton.setMessage(Component.literal(menu.data.get(7) > 0 ? "Stop Charging" : "Charge"));
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
