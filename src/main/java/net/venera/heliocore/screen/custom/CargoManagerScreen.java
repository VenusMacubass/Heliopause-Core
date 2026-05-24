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


public class CargoManagerScreen extends AbstractContainerScreen<CargoManagerMenu> {
    private Button inputEnableButton;
    private Button outputEnableButton;
    
    private static final ResourceLocation CARGO_MANAGER_GUI = 
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/cargo_manager/cargo_manager_gui.png");
    
    public CargoManagerScreen(CargoManagerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.inputEnableButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(4) > 0 ? "Loadn't" : "Load"),
                        button -> {
                            PacketDistributor.sendToServer(
                                    new MachineButtonHelper(menu.blockEntity.getBlockPos(), 0)
                            );
                        })
                .bounds(x + 62, y + 53, 25, 15)
                .build()
        );

        this.outputEnableButton = this.addRenderableWidget(Button.builder(
                        Component.literal(menu.data.get(5) > 0 ? "Unloadn't" : "Unload"),
                        button -> {
                            PacketDistributor.sendToServer(
                                    new MachineButtonHelper(menu.blockEntity.getBlockPos(), 1)
                            );
                        })
                .bounds(x + 144, y + 53, 25, 15)
                .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CARGO_MANAGER_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(CARGO_MANAGER_GUI, x, y, 0, 0, 176, 174); //Gui

        int chargeLength = menu.getEnergyScaled(54);

        if (chargeLength > 0) {
            int startX = x + 92;
            int startY = y + 82;
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

        int energyX = imageX + 92;
        int energyY = imageY + 82;
        int energyWidth = 54;
        int energyHeight = 7;


        if (isMouseOver(x, y, energyX, energyY, energyWidth, energyHeight)) {
            int currentEnergy = menu.data.get(0);
            int maxEnergy = menu.data.get(1);

            guiGraphics.renderTooltip(font, Component.literal(
                    "Energy: " + currentEnergy + " / " + maxEnergy + " FE"), x, y);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.inputEnableButton.setMessage(Component.literal(menu.data.get(4) > 0 ? "Loadn't" : "Load"));
        this.outputEnableButton.setMessage(Component.literal(menu.data.get(5) > 0 ? "Unloadn't" : "Unload"));
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 7, 4, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 80, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
