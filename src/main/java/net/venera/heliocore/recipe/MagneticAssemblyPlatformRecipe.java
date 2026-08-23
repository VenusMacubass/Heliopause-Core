package net.venera.heliocore.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;

public record MagneticAssemblyPlatformRecipe(
        Ingredient engine,
        Ingredient base, // Renamed from storage
        List<Ingredient> fins,
        List<Ingredient> hulls,
        List<Ingredient> boosters,
        Ingredient nose,
        ItemStack result) implements Recipe<MagneticAssemblyPlatformInput> {

    @Override
    public boolean matches(MagneticAssemblyPlatformInput input, Level level) {
        // Must always have engine, base, and nose
        if (!this.engine.test(input.getItem(0))) return false;
        if (!this.base.test(input.getItem(1))) return false;
        if (!this.nose.test(input.getItem(16))) return false;

        // Check Fins (Indices 2 to 5)
        for (int i = 0; i < 4; i++) {
            ItemStack stack = input.getItem(i + 2);
            if (i < this.fins.size()) {
                if (!this.fins.get(i).test(stack)) return false;
            } else {
                if (!stack.isEmpty()) return false; // Slot MUST be empty
            }
        }

        // Check Hulls (Indices 6 to 13)
        for (int i = 0; i < 8; i++) {
            ItemStack stack = input.getItem(i + 6);
            if (i < this.hulls.size()) {
                if (!this.hulls.get(i).test(stack)) return false;
            } else {
                if (!stack.isEmpty()) return false;
            }
        }

        // Check Boosters (Indices 14 to 15)
        for (int i = 0; i < 2; i++) {
            ItemStack stack = input.getItem(i + 14);
            if (i < this.boosters.size()) {
                if (!this.boosters.get(i).test(stack)) return false;
            } else {
                if (!stack.isEmpty()) return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(MagneticAssemblyPlatformInput input, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HpCRecipes.MAGNETIC_ASSEMBLY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return HpCRecipes.MAGNETIC_ASSEMBLY_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<MagneticAssemblyPlatformRecipe> {
        public static final MapCodec<MagneticAssemblyPlatformRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("engine").forGetter(r -> r.engine()),
                Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(r -> r.base()),

                // Using optionalFieldOf allows you to completely omit these from the JSON if not needed
                Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("fins", List.of()).forGetter(r -> r.fins()),
                Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("hulls", List.of()).forGetter(r -> r.hulls()),
                Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("boosters", List.of()).forGetter(r -> r.boosters()),

                Ingredient.CODEC_NONEMPTY.fieldOf("nose").forGetter(r -> r.nose()),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result())
        ).apply(inst, MagneticAssemblyPlatformRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MagneticAssemblyPlatformRecipe> STREAM_CODEC = StreamCodec.of(
                MagneticAssemblyPlatformRecipe.Serializer::toNetwork, MagneticAssemblyPlatformRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<MagneticAssemblyPlatformRecipe> codec() { return CODEC; }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MagneticAssemblyPlatformRecipe> streamCodec() { return STREAM_CODEC; }

        private static MagneticAssemblyPlatformRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            return new MagneticAssemblyPlatformRecipe(
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buffer),
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buffer),
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer),
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer),
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer),
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buffer),
                    ItemStack.STREAM_CODEC.decode(buffer)
            );
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, MagneticAssemblyPlatformRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.engine());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.fins());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.hulls());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.boosters());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.nose());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result());
        }
    }
}
