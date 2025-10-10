package net.venera.galacticraftcore.init.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ShapelessNoRemainderRecipe extends ShapelessRecipe {
	final String group;
	final CraftingBookCategory category;
	final ItemStack result;
	final NonNullList<Ingredient> ingredients;

	public ShapelessNoRemainderRecipe(String groupIn, CraftingBookCategory bookCategory, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
		super(groupIn, bookCategory, recipeOutputIn, recipeItemsIn);
		this.group = groupIn;
		this.category = bookCategory;
		this.result = recipeOutputIn;
		this.ingredients = recipeItemsIn;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return LiquidRecipes.SHAPELESS_NO_REMAINDER_SERIALIZER.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
		return NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
	}

	public static class Serializer implements RecipeSerializer<ShapelessNoRemainderRecipe> {
		private static final MapCodec<ShapelessNoRemainderRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
								CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
								Ingredient.CODEC_NONEMPTY
										.listOf()
										.fieldOf("ingredients")
										.flatXmap(
												array -> {
													Ingredient[] aingredient = array.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
													if (aingredient.length == 0) {
														return DataResult.error(() -> "No ingredients for shapeless recipe");
													} else {
														return aingredient.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()
																? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()))
																: DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
													}
												},
												DataResult::success
										)
										.forGetter(recipe -> recipe.ingredients)
						)
						.apply(instance, ShapelessNoRemainderRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessNoRemainderRecipe> STREAM_CODEC = StreamCodec.of(
				Serializer::toNetwork, Serializer::fromNetwork
		);

		@Override
		public MapCodec<ShapelessNoRemainderRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapelessNoRemainderRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static ShapelessNoRemainderRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
			String s = byteBuf.readUtf();
			CraftingBookCategory craftingbookcategory = byteBuf.readEnum(CraftingBookCategory.class);
			int i = byteBuf.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
			nonnulllist.replaceAll(p_319735_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(byteBuf));
			ItemStack itemstack = ItemStack.STREAM_CODEC.decode(byteBuf);
			return new ShapelessNoRemainderRecipe(s, craftingbookcategory, itemstack, nonnulllist);
		}

		private static void toNetwork(RegistryFriendlyByteBuf byteBuf, ShapelessNoRemainderRecipe recipe) {
			byteBuf.writeUtf(recipe.group);
			byteBuf.writeEnum(recipe.category);
			byteBuf.writeVarInt(recipe.ingredients.size());

			for (Ingredient ingredient : recipe.ingredients) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(byteBuf, ingredient);
			}

			ItemStack.STREAM_CODEC.encode(byteBuf, recipe.result);
		}
	}
}
