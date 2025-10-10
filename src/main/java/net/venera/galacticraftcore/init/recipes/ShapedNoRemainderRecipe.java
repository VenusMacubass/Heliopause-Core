package net.venera.galacticraftcore.init.recipes;

import com.mojang.serialization.Codec;
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
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class ShapedNoRemainderRecipe extends ShapedRecipe {
	static int MAX_WIDTH = 3;
	static int MAX_HEIGHT = 3;

	final ShapedRecipePattern pattern;
	final ItemStack result;
	final String group;
	final CraftingBookCategory category;
	final boolean showNotification;

	public ShapedNoRemainderRecipe(String groupIn, CraftingBookCategory bookCategory, ShapedRecipePattern recipePattern, ItemStack recipeOutputIn, boolean showNotif) {
		super(groupIn, bookCategory, recipePattern, recipeOutputIn, showNotif);
		this.group = groupIn;
		this.category = bookCategory;
		this.pattern = recipePattern;
		this.result = recipeOutputIn;
		this.showNotification = showNotif;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return LiquidRecipes.SHAPED_NO_REMAINDER_SERIALIZER.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
		return NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
	}

	@Override
	public boolean isIncomplete() {
		NonNullList<Ingredient> nonnulllist = this.getIngredients();
		return nonnulllist.isEmpty() || nonnulllist.stream().anyMatch(Ingredient::isEmpty);
	}

	public static class Serializer implements RecipeSerializer<ShapedNoRemainderRecipe> {
		public static final MapCodec<ShapedNoRemainderRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
								CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(recipe -> recipe.category),
								ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
								Codec.BOOL.optionalFieldOf("show_notification", Boolean.TRUE).forGetter(recipe -> recipe.showNotification)
						)
						.apply(instance, ShapedNoRemainderRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapedNoRemainderRecipe> STREAM_CODEC = StreamCodec.of(
				Serializer::toNetwork, Serializer::fromNetwork
		);

		@Override
		public MapCodec<ShapedNoRemainderRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapedNoRemainderRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static ShapedNoRemainderRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
			String s = byteBuf.readUtf();
			CraftingBookCategory craftingbookcategory = byteBuf.readEnum(CraftingBookCategory.class);
			ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(byteBuf);
			ItemStack itemstack = ItemStack.STREAM_CODEC.decode(byteBuf);
			boolean flag = byteBuf.readBoolean();
			return new ShapedNoRemainderRecipe(s, craftingbookcategory, shapedrecipepattern, itemstack, flag);
		}

		private static void toNetwork(RegistryFriendlyByteBuf byteBuf, ShapedNoRemainderRecipe recipe) {
			byteBuf.writeUtf(recipe.group);
			byteBuf.writeEnum(recipe.category);
			ShapedRecipePattern.STREAM_CODEC.encode(byteBuf, recipe.pattern);
			ItemStack.STREAM_CODEC.encode(byteBuf, recipe.result);
			byteBuf.writeBoolean(recipe.showNotification);
		}
	}
}