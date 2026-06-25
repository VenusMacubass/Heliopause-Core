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
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;

public class Tier1RocketLanderRenderer extends EntityRenderer<Tier1RocketLanderEntity> {
    private final Tier1RocketLanderModel model;

    public Tier1RocketLanderRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new Tier1RocketLanderModel(context.bakeLayer(Tier1RocketLanderModel.LANDER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(Tier1RocketLanderEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/entity/tier_1_rocket_lander.png");
    }

    @Override
    public boolean shouldRender(Tier1RocketLanderEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(Tier1RocketLanderEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.pushPose();

        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180f));
        poseStack.translate(0, -1.5f, 0);
        VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}
