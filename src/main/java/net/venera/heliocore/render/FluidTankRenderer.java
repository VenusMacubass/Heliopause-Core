package net.venera.heliocore.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.venera.heliocore.block.entity.FluidTankEntity;
import org.joml.Matrix4f;

public class FluidTankRenderer implements BlockEntityRenderer<FluidTankEntity> {
    public FluidTankRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(FluidTankEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (entity.getFluidAmount() <= 0 || entity.getCurrentFluid() == Fluids.EMPTY) {
            return;
        }
        
        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(entity.getCurrentFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExt.getStillTexture());
        int tintColor = fluidExt.getTintColor(entity.getCurrentFluid().defaultFluidState(), entity.getLevel(), entity.getBlockPos());
        
        float a = ((tintColor >> 24) & 0xFF) / 255f;
        float r = ((tintColor >> 16) & 0xFF) / 255f;
        float g = ((tintColor >> 8) & 0xFF) / 255f;
        float b = (tintColor & 0xFF) / 255f;
        if (a == 0.0f) a = 1.0f;
        
        float xzMin = 2.0f / 16.0f;
        float xzMax = 14.0f / 16.0f;
        
        float yMin = 0.001f;
        float yMax = 0.999f;

        float fillPercentage = entity.getFluidAmount() / (float) FluidTankEntity.FLUID_TANK_CAPACITY;
        float fluidTop = yMin + (fillPercentage * (yMax - yMin));
        
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        float vTop = v1 - ((v1 - v0) * fillPercentage);

        VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
        Matrix4f pose = poseStack.last().pose();

        //NORTH FACE (-Z)
        builder.addVertex(pose, xzMax, yMin, xzMin).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0, 0, -1);
        builder.addVertex(pose, xzMin, yMin, xzMin).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0, 0, -1);
        builder.addVertex(pose, xzMin, fluidTop, xzMin).setColor(r, g, b, a).setUv(u1, vTop).setLight(packedLight).setNormal(0, 0, -1);
        builder.addVertex(pose, xzMax, fluidTop, xzMin).setColor(r, g, b, a).setUv(u0, vTop).setLight(packedLight).setNormal(0, 0, -1);

        //SOUTH FACE (+Z)
        builder.addVertex(pose, xzMin, yMin, xzMax).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0, 0, 1);
        builder.addVertex(pose, xzMax, yMin, xzMax).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0, 0, 1);
        builder.addVertex(pose, xzMax, fluidTop, xzMax).setColor(r, g, b, a).setUv(u1, vTop).setLight(packedLight).setNormal(0, 0, 1);
        builder.addVertex(pose, xzMin, fluidTop, xzMax).setColor(r, g, b, a).setUv(u0, vTop).setLight(packedLight).setNormal(0, 0, 1);

        //WEST FACE (-X)
        builder.addVertex(pose, xzMin, yMin, xzMin).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(-1, 0, 0);
        builder.addVertex(pose, xzMin, yMin, xzMax).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(-1, 0, 0);
        builder.addVertex(pose, xzMin, fluidTop, xzMax).setColor(r, g, b, a).setUv(u1, vTop).setLight(packedLight).setNormal(-1, 0, 0);
        builder.addVertex(pose, xzMin, fluidTop, xzMin).setColor(r, g, b, a).setUv(u0, vTop).setLight(packedLight).setNormal(-1, 0, 0);

        //EAST FACE (+X)
        builder.addVertex(pose, xzMax, yMin, xzMax).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(1, 0, 0);
        builder.addVertex(pose, xzMax, yMin, xzMin).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(1, 0, 0);
        builder.addVertex(pose, xzMax, fluidTop, xzMin).setColor(r, g, b, a).setUv(u1, vTop).setLight(packedLight).setNormal(1, 0, 0);
        builder.addVertex(pose, xzMax, fluidTop, xzMax).setColor(r, g, b, a).setUv(u0, vTop).setLight(packedLight).setNormal(1, 0, 0);

        //TOP FACE (+Y)
        builder.addVertex(pose, xzMin, fluidTop, xzMax).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0, 1, 0);
        builder.addVertex(pose, xzMax, fluidTop, xzMax).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0, 1, 0);
        builder.addVertex(pose, xzMax, fluidTop, xzMin).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0, 1, 0);
        builder.addVertex(pose, xzMin, fluidTop, xzMin).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0, 1, 0);

        //BOTTOM FACE (-Y)
        builder.addVertex(pose, xzMin, yMin, xzMin).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0, -1, 0);
        builder.addVertex(pose, xzMax, yMin, xzMin).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0, -1, 0);
        builder.addVertex(pose, xzMax, yMin, xzMax).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0, -1, 0);
        builder.addVertex(pose, xzMin, yMin, xzMax).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0, -1, 0);
    }
}
