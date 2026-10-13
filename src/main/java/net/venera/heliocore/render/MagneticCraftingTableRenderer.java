package net.venera.heliocore.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.venera.heliocore.block.entity.MagneticCraftingTableEntity;

public class MagneticCraftingTableRenderer implements BlockEntityRenderer<MagneticCraftingTableEntity> {
    private final ItemRenderer itemRenderer;

    public MagneticCraftingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(MagneticCraftingTableEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int actualLight = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
        
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.05D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        for (int i = 0; i < 9; i++) {
            ItemStack stack = blockEntity.inventory.getStackInSlot(i);

            if (!stack.isEmpty()) {
                poseStack.pushPose();

                int col = i % 3;
                int row = i / 3;
                
                double xOffset = -0.20D + (col * 0.20D);
                double zOffset = -0.20D + (row * 0.20D);
                
                poseStack.translate(xOffset, 0.0D, zOffset);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.scale(0.3F, 0.3F, 0.3F);

                this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, actualLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, blockEntity.getLevel(), 0);

                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }
}
