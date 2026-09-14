package net.venera.heliocore.controls;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.venera.heliocore.HeliopauseCore;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = HeliopauseCore.MOD_ID, value = Dist.CLIENT)
public class HpCKeybinds {
    public static final KeyMapping ZOOM_KEY = new KeyMapping(
            "key.heliocore.zoom",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.heliocore.keys"
    );

    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(ZOOM_KEY);
        
    }
}