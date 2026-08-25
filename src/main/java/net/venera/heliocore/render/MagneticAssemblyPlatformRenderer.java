package net.venera.heliocore.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.entity.machine.MagneticAssemblyPlatformEntity;
import net.venera.heliocore.entity.client.Tier1RocketModel;

public class MagneticAssemblyPlatformRenderer implements BlockEntityRenderer<MagneticAssemblyPlatformEntity> {
    private static final ResourceLocation ROCKET_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/entity/tier_1_rocket.png"); 

    private final Tier1RocketModel<?> rocketModel;

    public MagneticAssemblyPlatformRenderer(BlockEntityRendererProvider.Context context) {
        this.rocketModel = new Tier1RocketModel<>(context.bakeLayer(Tier1RocketModel.ROCKET_LOCATION));
    }

    @Override
    public void render(MagneticAssemblyPlatformEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        
        poseStack.translate(0.5D, 1.3D, 0.5D); //Offset
        poseStack.scale(-0.2F, -0.2F, 0.2F); //Scale
        
        rocketModel.engine.visible = !blockEntity.inventory.getStackInSlot(0).isEmpty();
        rocketModel.bb_main.visible = !blockEntity.inventory.getStackInSlot(1).isEmpty();
        
        boolean hasFin1 = !blockEntity.inventory.getStackInSlot(2).isEmpty();
        rocketModel.wing_1.visible = hasFin1;
        rocketModel.upperwings_1.visible = hasFin1;

        boolean hasFin2 = !blockEntity.inventory.getStackInSlot(3).isEmpty();
        rocketModel.wing_2.visible = hasFin2;
        rocketModel.upper_wings_2.visible = hasFin2;

        boolean hasFin3 = !blockEntity.inventory.getStackInSlot(4).isEmpty();
        rocketModel.wing_3.visible = hasFin3;
        rocketModel.upper_wings_3.visible = hasFin3;

        boolean hasFin4 = !blockEntity.inventory.getStackInSlot(5).isEmpty();
        rocketModel.wing_4.visible = hasFin4;
        rocketModel.upper_wings_4.visible = hasFin4;
        
        rocketModel.wings.visible = hasFin1 || hasFin2 || hasFin3 || hasFin4;
        rocketModel.upper_wings.visible = hasFin1 || hasFin2 || hasFin3 || hasFin4;
        
        boolean hasAllHulls = true;
        for (int i = 6; i <= 13; i++) {
            if (blockEntity.inventory.getStackInSlot(i).isEmpty()) {
                hasAllHulls = false;
                break;
            }
        }
        rocketModel.walls.visible = hasAllHulls;
        rocketModel.top.visible = !blockEntity.inventory.getStackInSlot(16).isEmpty();
        
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(ROCKET_TEXTURE));
        int actualLight = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
        int overlay = OverlayTexture.NO_OVERLAY;

        rocketModel.renderToBuffer(poseStack, vertexConsumer, actualLight, overlay, 0xFFFFFFFF);

        poseStack.popPose();
    }
}
