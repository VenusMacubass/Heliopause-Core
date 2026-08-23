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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MagneticAssemblyPlatformRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;

    private Ingredient engine;
    private Ingredient base; 
    private Ingredient nose;
    
    private final List<Ingredient> fins = new ArrayList<>();
    private final List<Ingredient> hulls = new ArrayList<>();
    private final List<Ingredient> boosters = new ArrayList<>();

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private MagneticAssemblyPlatformRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static MagneticAssemblyPlatformRecipeBuilder fabricate(RecipeCategory category, ItemLike result) {
        return new MagneticAssemblyPlatformRecipeBuilder(category, result, 1);
    }

    // Engine, Storage, Nose
    public MagneticAssemblyPlatformRecipeBuilder engine(Ingredient ingredient) { this.engine = ingredient; return this; }
    public MagneticAssemblyPlatformRecipeBuilder engine(TagKey<Item> tag) { return this.engine(Ingredient.of(tag)); }

    public MagneticAssemblyPlatformRecipeBuilder base(Ingredient ingredient) { this.base = ingredient; return this; }
    public MagneticAssemblyPlatformRecipeBuilder base(TagKey<Item> tag) { return this.base(Ingredient.of(tag)); }

    public MagneticAssemblyPlatformRecipeBuilder nose(Ingredient ingredient) { this.nose = ingredient; return this; }
    public MagneticAssemblyPlatformRecipeBuilder nose(TagKey<Item> tag) { return this.nose(Ingredient.of(tag)); }

    // List Appenders
    public MagneticAssemblyPlatformRecipeBuilder addFin(Ingredient ingredient) { this.fins.add(ingredient); return this; }
    public MagneticAssemblyPlatformRecipeBuilder addFin(TagKey<Item> tag) { return this.addFin(Ingredient.of(tag)); }

    public MagneticAssemblyPlatformRecipeBuilder addHull(Ingredient ingredient) { this.hulls.add(ingredient); return this; }
    public MagneticAssemblyPlatformRecipeBuilder addHull(TagKey<Item> tag) { return this.addHull(Ingredient.of(tag)); }

    public MagneticAssemblyPlatformRecipeBuilder addBooster(Ingredient ingredient) { this.boosters.add(ingredient); return this; }
    public MagneticAssemblyPlatformRecipeBuilder addBooster(TagKey<Item> tag) { return this.addBooster(Ingredient.of(tag)); }

    @Override
    public MagneticAssemblyPlatformRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public MagneticAssemblyPlatformRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        if (this.engine == null || this.base == null || this.nose == null) {
            throw new IllegalStateException("Engine, Base, and Nose must be defined for assembly recipe " + id);
        }
        if (this.fins.size() > 4 || this.hulls.size() > 8 || this.boosters.size() > 2) {
            throw new IllegalStateException("Recipe " + id + " exceeds maximum platform slots!");
        }

        Advancement.Builder advancementBuilder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);

        MagneticAssemblyPlatformRecipe recipe = new MagneticAssemblyPlatformRecipe(
                this.engine,
                this.base,
                this.fins,
                this.hulls,
                this.boosters,
                this.nose,
                new ItemStack(this.result, this.count)
        );

        recipeOutput.accept(id, recipe, advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}
