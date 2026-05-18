package net.venera.heliocore.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.block.ModBlocks;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;

public class Tier1RocketRenderer extends EntityRenderer<Tier1RocketEntity> {
    public Tier1RocketRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Tier1RocketEntity tier1RocketEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public boolean shouldRender(Tier1RocketEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(Tier1RocketEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

        poseStack.pushPose();

        // Center the blocks inside the entity's hitbox
        poseStack.translate(-0.5, 0, -0.5);

        // Grab Minecraft's built-in block renderer
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        // 1. Draw the Bottom Block
        dispatcher.renderSingleBlock(ModBlocks.T1_ROCKET_BOT.get().defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        // 2. Move up 1 block and draw the Middle Block
        poseStack.translate(0, 1, 0);
        dispatcher.renderSingleBlock(ModBlocks.T1_ROCKET_MID.get().defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        // 3. Move up 1 block and draw the Top Block
        poseStack.translate(0, 1, 0);
        dispatcher.renderSingleBlock(ModBlocks.T1_ROCKET_TOP.get().defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}
