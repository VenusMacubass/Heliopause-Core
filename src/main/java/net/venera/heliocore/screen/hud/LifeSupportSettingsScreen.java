package net.venera.heliocore.screen.hud;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.venera.heliocore.HeliopauseConfig;

import java.util.function.Consumer;

public class LifeSupportSettingsScreen extends Screen {
    private final Screen parent;

    public LifeSupportSettingsScreen(Screen parent) {
        super(Component.literal("Life Support Systems"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;

        // Slider for Max Chamber Volume (100 to 50000)
        addSliderAndInput("Max Chamber Volume", centerX, 70,
                HeliopauseConfig.MAX_CHAMBER_VOLUME.get().floatValue(), 100f, 50000f,
                val -> HeliopauseConfig.MAX_CHAMBER_VOLUME.set(val.intValue()));

        // Reset Defaults Button
        this.addRenderableWidget(Button.builder(Component.literal("Reset Defaults"), btn -> {
            HeliopauseConfig.MAX_CHAMBER_VOLUME.set(2000);
            this.minecraft.setScreen(new LifeSupportSettingsScreen(this.parent));
        }).bounds(centerX - 100, 110, 200, 20).build());

        // Done Button to exit back to Main Hub
        this.addRenderableWidget(Button.builder(Component.literal("Done"), btn -> {
            this.onClose();
        }).bounds(centerX - 100, this.height - 40, 200, 20).build());
    }

    // Identical helper method from your HUD screen
    private void addSliderAndInput(String name, int centerX, int y, float currentValue, float min, float max, Consumer<Float> onSave) {
        boolean isScale = max < 100;
        EditBox editBox = new EditBox(this.font, centerX + 50, y, 70, 20, Component.literal(name));
        editBox.setValue(isScale ? String.format(java.util.Locale.US, "%.2f", currentValue) : String.valueOf((int) currentValue));

        class CustomSlider extends AbstractSliderButton {
            public CustomSlider(int x, int y, int width, int height, Component message, double value) {
                super(x, y, width, height, message, value);
            }
            @Override
            protected void updateMessage() { this.setMessage(Component.literal(name)); }
            @Override
            protected void applyValue() {
                float val = Mth.lerp((float) this.value, min, max);
                editBox.setValue(isScale ? String.format(java.util.Locale.US, "%.2f", val) : String.valueOf((int) val));
                onSave.accept(val);
            }
            public void updateVisuals(double newValue) {
                this.value = newValue;
                this.updateMessage();
            }
        }

        CustomSlider slider = new CustomSlider(centerX - 120, y, 160, 20, Component.literal(name), Mth.inverseLerp(Math.min(currentValue, max), min, max));

        editBox.setResponder(text -> {
            try {
                float val = Float.parseFloat(text);
                onSave.accept(val);
                slider.updateVisuals(Mth.inverseLerp(val, min, max));
            } catch (NumberFormatException ignored) {}
        });

        this.addRenderableWidget(slider);
        this.addRenderableWidget(editBox);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
