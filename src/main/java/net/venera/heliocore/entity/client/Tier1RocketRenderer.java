package net.venera.heliocore.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;

public class Tier1RocketRenderer extends EntityRenderer<Tier1RocketEntity> {
    private final Tier1RocketModel model;

    public Tier1RocketRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new Tier1RocketModel(context.bakeLayer(Tier1RocketModel.ROCKET_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(Tier1RocketEntity entity) {
        // 3. Point to your newly saved 2D Texture Atlas
        return ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/entity/tier_1_rocket.png");
    }

    @Override
    public boolean shouldRender(Tier1RocketEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(Tier1RocketEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.pushPose();
        
         poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180f));
         poseStack.translate(0, -1.5f, 0);

        // 4. Grab the rendering "brush" and apply your texture
        VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));

        // 5. Draw the entire model at once! (0xFFFFFFFF is the standard 1.21 default white tint)
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}
