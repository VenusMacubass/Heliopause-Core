package net.venera.heliocore.recipe;

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
import net.venera.heliocore.recipe.CoalCompressorRecipe;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CoalCompressorRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<String> rows = new ArrayList<>();
    private final Map<Character, Ingredient> key = new LinkedHashMap<>();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private CoalCompressorRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }
    
    public static CoalCompressorRecipeBuilder compress(RecipeCategory category, ItemLike result) {
        return new CoalCompressorRecipeBuilder(category, result, 1);
    }

    public static CoalCompressorRecipeBuilder compress(RecipeCategory category, ItemLike result, int count) {
        return new CoalCompressorRecipeBuilder(category, result, count);
    }

    public CoalCompressorRecipeBuilder pattern(String patternRow) {
        this.rows.add(patternRow);
        return this;
    }

    public CoalCompressorRecipeBuilder define(Character symbol, Ingredient ingredient) {
        this.key.put(symbol, ingredient);
        return this;
    }

    public CoalCompressorRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(item));
    }

    @Override
    public CoalCompressorRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public CoalCompressorRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for coal compressor recipe " + id + "!");
        }

        Advancement.Builder advancementBuilder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        
        ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);
        CraftingBookCategory bookCategory = CraftingBookCategory.MISC;
        
        CoalCompressorRecipe recipe = new CoalCompressorRecipe(
                this.group != null ? this.group : "",
                bookCategory,
                pattern,
                new ItemStack(this.result, this.count),
                true 
        );
        recipeOutput.accept(id, recipe, advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}
