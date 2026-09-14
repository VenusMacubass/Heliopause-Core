package net.venera.heliocore.screen.hud;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClientConfigHubScreen extends Screen {
    private final Screen parent; // This will be HpCMainConfigScreen

    public ClientConfigHubScreen(Screen parent) {
        super(Component.literal("Client Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int startY = 60;

        // Add to this list as you make more client features!
        this.addRenderableWidget(Button.builder(Component.literal("Space Suit HUD"), btn -> {
            this.minecraft.setScreen(new net.venera.heliocore.screen.hud.HudSettingsScreen(this));
        }).bounds(centerX - 100, startY, 200, 20).build());

        // Back Button
        this.addRenderableWidget(Button.builder(Component.literal("Back"), btn -> {
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
