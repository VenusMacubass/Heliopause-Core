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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class PCBFabricatorRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    
    private Ingredient core;
    private Ingredient wire;
    private Ingredient logic1;
    private Ingredient logic2;
    private Ingredient sub1;
    private Ingredient sub2;

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private PCBFabricatorRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static PCBFabricatorRecipeBuilder fabricate(RecipeCategory category, ItemLike result) {
        return new PCBFabricatorRecipeBuilder(category, result, 1);
    }

    public static PCBFabricatorRecipeBuilder fabricate(RecipeCategory category, ItemLike result, int count) {
        return new PCBFabricatorRecipeBuilder(category, result, count);
    }

    //--- Ingredient Setters ---
    public PCBFabricatorRecipeBuilder core(Ingredient ingredient) { this.core = ingredient; return this; }
    public PCBFabricatorRecipeBuilder core(ItemLike item) { return this.core(Ingredient.of(item)); }
    public PCBFabricatorRecipeBuilder core(TagKey<Item> tag) { return this.core(Ingredient.of(tag)); }

    public PCBFabricatorRecipeBuilder wire(Ingredient ingredient) { this.wire = ingredient; return this; }
    public PCBFabricatorRecipeBuilder wire(ItemLike item) { return this.wire(Ingredient.of(item)); }
    public PCBFabricatorRecipeBuilder wire(TagKey<Item> tag) { return this.wire(Ingredient.of(tag)); }

    public PCBFabricatorRecipeBuilder logic1(Ingredient ingredient) { this.logic1 = ingredient; return this; }
    public PCBFabricatorRecipeBuilder logic1(ItemLike item) { return this.logic1(Ingredient.of(item)); }
    public PCBFabricatorRecipeBuilder logic1(TagKey<Item> tag) { return this.logic1(Ingredient.of(tag)); }

    public PCBFabricatorRecipeBuilder logic2(Ingredient ingredient) { this.logic2 = ingredient; return this; }
    public PCBFabricatorRecipeBuilder logic2(ItemLike item) { return this.logic2(Ingredient.of(item)); }
    public PCBFabricatorRecipeBuilder logic2(TagKey<Item> tag) { return this.logic2(Ingredient.of(tag)); }

    public PCBFabricatorRecipeBuilder sub1(Ingredient ingredient) { this.sub1 = ingredient; return this; }
    public PCBFabricatorRecipeBuilder sub1(ItemLike item) { return this.sub1(Ingredient.of(item)); }
    public PCBFabricatorRecipeBuilder sub1(TagKey<Item> tag) { return this.sub1(Ingredient.of(tag)); }

    public PCBFabricatorRecipeBuilder sub2(Ingredient ingredient) { this.sub2 = ingredient; return this; }
    public PCBFabricatorRecipeBuilder sub2(ItemLike item) { return this.sub2(Ingredient.of(item)); }
    public PCBFabricatorRecipeBuilder sub2(TagKey<Item> tag) { return this.sub2(Ingredient.of(tag)); }

    @Override
    public PCBFabricatorRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public PCBFabricatorRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        if (core == null || wire == null || logic1 == null || logic2 == null || sub1 == null || sub2 == null) {
            throw new IllegalStateException("All 6 ingredients must be defined for PCB Fabricator recipe " + id + "!");
        }

        Advancement.Builder advancementBuilder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        
        PCBFabricatorRecipe recipe = new PCBFabricatorRecipe(
                this.core,
                this.wire,
                this.logic1,
                this.logic2,
                this.sub1,
                this.sub2,
                new ItemStack(this.result, this.count)
        );

        recipeOutput.accept(id, recipe, advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}