package net.venera.heliocore.item;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;
import net.venera.heliocore.util.HpCTags;

public class HpCToolTiers {
    public static final Tier STEEL = new SimpleTier(HpCTags.Blocks.INCORRECT_FOR_STEEL_TOOL, 600, 4f, 3f, 18,
    () ->Ingredient.of(HpCItems.COMPRESSED_STEEL.get()));

    public static final Tier GLASS = new SimpleTier(Tags.Blocks.GLASS_BLOCKS, 3, 5f, 6f, 45,
            () ->Ingredient.of(Items.GLASS));
}
