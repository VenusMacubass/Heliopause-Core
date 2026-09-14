package net.venera.heliocore.screen.hud;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.venera.heliocore.HeliopauseClientConfig;

import java.util.function.Consumer;

public class HudSettingsScreen extends Screen {
    private final Screen parent; // Remembers the Mod Menu screen
    
    public HudSettingsScreen(Screen parent) {
        super(Component.literal("Space Suit HUD Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;

        // 1. Master Toggle Button
        this.addRenderableWidget(Button.builder(
                Component.literal("HUD: " + (HeliopauseClientConfig.HUD_ENABLED.get() ? "ON" : "OFF")),
                btn -> {
                    boolean current = HeliopauseClientConfig.HUD_ENABLED.get();
                    HeliopauseClientConfig.HUD_ENABLED.set(!current);
                    btn.setMessage(Component.literal("HUD: " + (!current ? "ON" : "OFF")));
                }
        ).bounds(centerX - 100, 40, 95, 20).build());
        
        this.addRenderableWidget(Button.builder(
                Component.literal("Layout: " + (HeliopauseClientConfig.HUD_HORIZONTAL.get() ? "Horizontal" : "Vertical")),
                btn -> {
                    boolean current = HeliopauseClientConfig.HUD_HORIZONTAL.get();
                    HeliopauseClientConfig.HUD_HORIZONTAL.set(!current);
                    btn.setMessage(Component.literal("Layout: " + (!current ? "Horizontal" : "Vertical")));
                }
        ).bounds(centerX + 5, 40, 95, 20).build());

        // 2. X Coordinate
        addSliderAndInput("X Position", centerX, 70,
                HeliopauseClientConfig.HUD_X.get(), 0, 1000,
                val -> HeliopauseClientConfig.HUD_X.set(val.intValue()));

        // 3. Y Coordinate
        addSliderAndInput("Y Position", centerX, 100,
                HeliopauseClientConfig.HUD_Y.get(), 0, 1000,
                val -> HeliopauseClientConfig.HUD_Y.set(val.intValue()));

        // 4. Scale 
        addSliderAndInput("Scale", centerX, 130,
                HeliopauseClientConfig.HUD_SCALE.get().floatValue(), 0.5f, 5.0f,
                val -> HeliopauseClientConfig.HUD_SCALE.set((double) val));

        // 5. Reset Defaults Button
        this.addRenderableWidget(Button.builder(Component.literal("Reset Defaults"), btn -> {
            HeliopauseClientConfig.HUD_ENABLED.set(true);
            HeliopauseClientConfig.HUD_X.set(HeliopauseClientConfig.HUD_X.getDefault());
            HeliopauseClientConfig.HUD_Y.set(HeliopauseClientConfig.HUD_Y.getDefault());
            HeliopauseClientConfig.HUD_SCALE.set(HeliopauseClientConfig.HUD_SCALE.getDefault());

            // Reload the screen, passing the parent along
            this.minecraft.setScreen(new HudSettingsScreen(this.parent));
        }).bounds(centerX - 100, 170, 200, 20).build());

        // 6. NEW: "Done" Button to exit back to Mod Menu
        this.addRenderableWidget(Button.builder(Component.literal("Done"), btn -> {
            this.onClose();
        }).bounds(centerX - 100, 200, 200, 20).build());
    }

    private void addSliderAndInput(String name, int centerX, int y, float currentValue, float min, float max, Consumer<Float> onSave) {
        boolean isScale = max < 100;
        EditBox editBox = new EditBox(this.font, centerX + 50, y, 70, 20, Component.literal(name));

        // Use Locale.US to force '.' instead of ',' for decimals to prevent NumberFormatException crashes!
        editBox.setValue(isScale ? String.format(java.util.Locale.US, "%.2f", currentValue) : String.valueOf((int) currentValue));

        // Create a custom subclass to bypass the private access restriction
        class CustomSlider extends AbstractSliderButton {
            public CustomSlider(int x, int y, int width, int height, Component message, double value) {
                super(x, y, width, height, message, value);
            }

            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(name));
            }

            @Override
            protected void applyValue() {
                float val = Mth.lerp((float) this.value, min, max);
                editBox.setValue(isScale ? String.format(java.util.Locale.US, "%.2f", val) : String.valueOf((int) val));
                onSave.accept(val);
            }

            // Expose the protected 'value' field safely
            public void updateVisuals(double newValue) {
                this.value = newValue; // Accessing the protected field directly
                this.updateMessage();
            }
        }

        CustomSlider slider = new CustomSlider(centerX - 120, y, 160, 20, Component.literal(name), Mth.inverseLerp(Math.min(currentValue, max), min, max));

        editBox.setResponder(text -> {
            try {
                float val = Float.parseFloat(text);
                onSave.accept(val);
                // Use our new custom method instead of the private setValue
                slider.updateVisuals(Mth.inverseLerp(val, min, max));
            } catch (NumberFormatException ignored) {
                // Ignore invalid characters while typing
            }
        });

        this.addRenderableWidget(slider);
        this.addRenderableWidget(editBox);
    }

    @Override
    public void onClose() {
        // Return to the Mod Menu instead of closing all GUIs
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, "Space Suit HUD Settings", this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
