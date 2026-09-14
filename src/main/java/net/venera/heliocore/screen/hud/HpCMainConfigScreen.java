package net.venera.heliocore.screen.hud;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HpCMainConfigScreen extends Screen {
    private final Screen parent; // The NeoForge Mod Menu

    public HpCMainConfigScreen(Screen parent) {
        super(Component.literal("Heliopause Core Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int startY = 60;

        // 1. Opens the Client Category List
        this.addRenderableWidget(Button.builder(Component.literal("Client Settings"), btn -> {
            this.minecraft.setScreen(new ClientConfigHubScreen(this));
        }).bounds(centerX - 100, startY, 200, 20).build());

        // 2. Opens the Server Category List
        this.addRenderableWidget(Button.builder(Component.literal("Server Settings"), btn -> {
            this.minecraft.setScreen(new ServerConfigHubScreen(this));
        }).bounds(centerX - 100, startY + 25, 200, 20).build());

        // 3. Done/Back Button (Returns to Mod Menu)
        this.addRenderableWidget(Button.builder(Component.literal("Done"), btn -> {
            this.onClose();
        }).bounds(centerX - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}