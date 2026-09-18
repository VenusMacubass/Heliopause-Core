package net.venera.heliocore.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.venera.heliocore.block.entity.PizzaEntity;
import net.venera.heliocore.block.hpc_custom.PizzaBlock;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PizzaBakedModel implements BakedModel {
    private final BakedModel basePizzaModel;
    private final Map<String, BakedModel> toppingModels;

    public PizzaBakedModel(BakedModel basePizzaModel, Map<String, BakedModel> toppingModels) {
        this.basePizzaModel = basePizzaModel;
        this.toppingModels = toppingModels;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();

        // 1. Give the Chunk Builder the base crust ONLY during the Solid pass
        if (renderType == null || renderType == RenderType.solid()) {
            quads.addAll(basePizzaModel.getQuads(state, side, rand, data, renderType));
        }

        // 2. Give the Chunk Builder the toppings ONLY during the Cutout (transparent) pass
        if (renderType == null || renderType == RenderType.cutout()) {
            if (state != null && data.has(PizzaEntity.TOPPINGS_PROPERTY)) {
                int mask = data.get(PizzaEntity.TOPPINGS_PROPERTY);
                int slices = state.getValue(PizzaBlock.SLICES); // 0 to 3

                if ((mask & 1) != 0) addToppingQuads(quads, "chicken", slices, state, side, rand, data, renderType);
                if ((mask & 2) != 0) addToppingQuads(quads, "meat", slices, state, side, rand, data, renderType);
                if ((mask & 4) != 0) addToppingQuads(quads, "mushroom", slices, state, side, rand, data, renderType);
                if ((mask & 8) != 0) addToppingQuads(quads, "fish", slices, state, side, rand, data, renderType);
                if ((mask & 16) != 0) addToppingQuads(quads, "veggies", slices, state, side, rand, data, renderType);
            }
        }

        return quads;
    }

    private void addToppingQuads(List<BakedQuad> quads, String toppingType, int slices, BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        BakedModel topping = toppingModels.get(toppingType + "_" + slices);
        if (topping != null) {
            quads.addAll(topping.getQuads(state, side, rand, data, renderType));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.solid(), RenderType.cutout());
    }

    // --- DELEGATE ALL OTHER METHODS TO THE BASE MODEL ---
    @Override public boolean useAmbientOcclusion() { return basePizzaModel.useAmbientOcclusion(); }
    @Override public boolean isGui3d() { return basePizzaModel.isGui3d(); }
    @Override public boolean usesBlockLight() { return basePizzaModel.usesBlockLight(); }
    @Override public boolean isCustomRenderer() { return basePizzaModel.isCustomRenderer(); }
    @Override public net.minecraft.client.renderer.texture.TextureAtlasSprite getParticleIcon() { return basePizzaModel.getParticleIcon(); }
    @Override public ItemTransforms getTransforms() { return basePizzaModel.getTransforms(); }
    @Override public ItemOverrides getOverrides() { return basePizzaModel.getOverrides(); }
}
