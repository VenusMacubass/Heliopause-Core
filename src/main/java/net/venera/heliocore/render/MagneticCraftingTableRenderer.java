package net.venera.heliocore.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.block.entity.MagneticCraftingTableEntity;

public class MagneticCraftingTableRenderer implements BlockEntityRenderer<MagneticCraftingTableEntity> {

    private final ItemRenderer itemRenderer;

    public MagneticCraftingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(MagneticCraftingTableEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        // Grab the actual light level from above the table so items don't render black
        int actualLight = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());

        // Loop through the 3x3 grid (Slots 0 to 8)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = blockEntity.inventory.getStackInSlot(i);

            if (!stack.isEmpty()) {
                poseStack.pushPose();

                // 1. Math to calculate the 3x3 Grid Position
                // Columns (X): 0, 1, 2
                int col = i % 3;
                // Rows (Z): 0, 1, 2
                int row = i / 3;

                // 2. Map columns/rows to coordinates (Spaced at 0.2, 0.5, and 0.8)
                double x = 0.3D + (col * 0.20D);
                double z = 0.3D + (row * 0.20D);
                double y = 1.05D; // Hover just slightly above the top of the block to prevent Z-fighting

                poseStack.translate(x, y, z);

                // 3. Lay the item flat on the table
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                // 4. Scale it down so all 9 items fit comfortably on one block surface
                poseStack.scale(0.3F, 0.3F, 0.3F);

                // 5. Draw the item
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, actualLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, blockEntity.getLevel(), 0);

                poseStack.popPose();
            }
        }
    }
}
