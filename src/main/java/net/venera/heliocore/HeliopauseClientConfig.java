package net.venera.heliocore;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.lwjgl.system.Pointer;

public class HeliopauseClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.push("Space Suit HUD Settings");
    }
    
    public static final ModConfigSpec.BooleanValue HUD_ENABLED = BUILDER
            .comment("Whether the Space Suit HUD is visible")
            .define("enabled", true);

    public static final ModConfigSpec.IntValue HUD_X = BUILDER
            .comment("The X coordinate of the HUD")
            .defineInRange("x", 380, 0, 4000);

    public static final ModConfigSpec.IntValue HUD_Y = BUILDER
            .comment("The Y coordinate of the HUD")
            .defineInRange("y", 90, 0, 4000);

    public static final ModConfigSpec.DoubleValue HUD_SCALE = BUILDER
            .comment("The visual scale of the HUD. Minimum is 0.5")
            .defineInRange("scale", 2.0, 0.5, 10.0);

    public static final ModConfigSpec.BooleanValue HUD_HORIZONTAL = BUILDER
            .comment("Use horizontal HUD layout instead of vertical")
            .define("horizontal_layout", false);
    static {
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
