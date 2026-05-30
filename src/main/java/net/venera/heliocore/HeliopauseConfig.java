package net.venera.heliocore;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class HeliopauseConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.push("Life Support Systems");
    }

    public static final ModConfigSpec.IntValue MAX_CHAMBER_VOLUME = BUILDER
            .comment("Maximum amount of air blocks a single Oxygen Sealer can flood-fill.")
            .defineInRange("maxChamberVolume", 2000, 100, 50000);

    

    static {
        BUILDER.pop(); //Closes the category
    }
    
    public static final ModConfigSpec SPEC = BUILDER.build();
}
