package net.venera.heliocore.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record PCBFabricatorRecipe(Ingredient core, Ingredient wire, Ingredient logic1, Ingredient logic2,
                                  Ingredient sub1, Ingredient sub2,
                                  ItemStack result) implements Recipe<PCBFabricatorInput> {

    @Override
    public boolean matches(PCBFabricatorInput input, Level level) {
        return this.core.test(input.getItem(0)) &&
                this.wire.test(input.getItem(1)) &&
                this.logic1.test(input.getItem(2)) &&
                this.logic2.test(input.getItem(3)) &&
                this.sub1.test(input.getItem(4)) &&
                this.sub2.test(input.getItem(5));
    }

    @Override
    public ItemStack assemble(PCBFabricatorInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HpCRecipes.PCB_FABRICATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return HpCRecipes.PCB_FABRICATOR_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PCBFabricatorRecipe> {
        public static final MapCodec<PCBFabricatorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("core").forGetter(r -> r.core),
                Ingredient.CODEC_NONEMPTY.fieldOf("wire").forGetter(r -> r.wire),
                Ingredient.CODEC_NONEMPTY.fieldOf("logic_1").forGetter(r -> r.logic1),
                Ingredient.CODEC_NONEMPTY.fieldOf("logic_2").forGetter(r -> r.logic2),
                Ingredient.CODEC_NONEMPTY.fieldOf("substrate_1").forGetter(r -> r.sub1),
                Ingredient.CODEC_NONEMPTY.fieldOf("substrate_2").forGetter(r -> r.sub2),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
        ).apply(inst, PCBFabricatorRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PCBFabricatorRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<PCBFabricatorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PCBFabricatorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static PCBFabricatorRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient core = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient wire = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient logic1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient logic2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient sub1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient sub2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new PCBFabricatorRecipe(core, wire, logic1, logic2, sub1, sub2, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, PCBFabricatorRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.core);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.wire);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.logic1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.logic2);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.sub1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.sub2);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}