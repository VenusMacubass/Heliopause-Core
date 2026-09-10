package net.venera.heliocore.recipe.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class SpaceSuitRecipeSerializer implements RecipeSerializer<SpaceSuitRecipe> {
    public static final MapCodec<SpaceSuitRecipe> CODEC = ShapedRecipe.Serializer.CODEC.xmap(
            shaped -> new SpaceSuitRecipe(shaped.getGroup(), shaped.category(), shaped.pattern, shaped.getResultItem(null), shaped.showNotification()),
            custom -> new ShapedRecipe(custom.getGroup(), custom.category(), custom.pattern, custom.getResultItem(null), custom.showNotification())
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceSuitRecipe> STREAM_CODEC = ShapedRecipe.Serializer.STREAM_CODEC.map(
            shaped -> new SpaceSuitRecipe(shaped.getGroup(), shaped.category(), shaped.pattern, shaped.getResultItem(null), shaped.showNotification()),
            custom -> new ShapedRecipe(custom.getGroup(), custom.category(), custom.pattern, custom.getResultItem(null), custom.showNotification())
    );

    @Override
    public MapCodec<SpaceSuitRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SpaceSuitRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}