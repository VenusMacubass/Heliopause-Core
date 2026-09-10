package net.venera.heliocore.recipe.item;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import javax.annotation.Nullable;
import java.util.*;

public class SpaceSuitRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<String> rows = new ArrayList<>();
    private final Map<Character, Ingredient> key = new LinkedHashMap<>();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public SpaceSuitRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static SpaceSuitRecipeBuilder upgrade(RecipeCategory category, ItemLike result) {
        return new SpaceSuitRecipeBuilder(category, result, 1);
    }

    public SpaceSuitRecipeBuilder pattern(String pattern) {
        this.rows.add(pattern);
        return this;
    }

    public SpaceSuitRecipeBuilder define(Character symbol, Ingredient ingredient) {
        this.key.put(symbol, ingredient);
        return this;
    }

    public SpaceSuitRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(item));
    }

    @Override
    public SpaceSuitRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public SpaceSuitRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);

        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);

        SpaceSuitRecipe recipe = new SpaceSuitRecipe(
                Objects.requireNonNullElse(this.group, ""),
                CraftingBookCategory.EQUIPMENT,
                pattern,
                new ItemStack(this.result, this.count),
                true
        );

        output.accept(id, recipe, advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}