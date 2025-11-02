package net.venera.galacticraftcore.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.item.custom.CanisterItem;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CanisterRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation CANISTER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "textures/item/gcc_canister.png");

    private static final int CANISTER_U = 3, CANISTER_V = 1;
    private static final int CANISTER_WIDTH = 10, CANISTER_HEIGHT = 14;

    private static final int HOLE_U = 7, HOLE_V = 5;
    private static final int HOLE_WIDTH = 2, HOLE_HEIGHT = 6;

    private static final int OIL_INDICATOR_U = 14, OIL_INDICATOR_V = 10;
    private static final int FUEL_INDICATOR_U = 14, FUEL_INDICATOR_V = 0;
    private static final int INDICATOR_WIDTH = 2, INDICATOR_HEIGHT = 6;

    public CanisterRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        CanisterData data = null;
        if (stack.getItem() instanceof CanisterItem canister) {
            data = canister.getCanisterData(stack);
        }

        poseStack.pushPose();

        // Apply standard Minecraft item transforms for this context
        applyItemTransforms(poseStack, displayContext);

        renderCanister(poseStack, bufferSource, packedLight, packedOverlay, data);
        poseStack.popPose();
    }

    private void applyItemTransforms(PoseStack poseStack, ItemDisplayContext displayContext) {
        // Start from center for consistent transforms
        poseStack.translate(0.5f, 0.5f, 0.5f);

        switch (displayContext) {
            case GUI:
                // Flat in inventory - just scale to fill slot
                poseStack.scale(1f, 1f, 1f);
                break;
            case GROUND:
                // On ground - lay flat
                poseStack.scale(0.5f, 0.5f, 0.5f);
                break;
            case FIXED:
                // Item frame - normal
                poseStack.scale(0.75f, 0.75f, 0.75f);
                break;
            case THIRD_PERSON_RIGHT_HAND:
            case THIRD_PERSON_LEFT_HAND:
                // 3rd person - upright
                poseStack.scale(0.6f, 0.6f, 0.6f);
                poseStack.translate(0, 0.2f, 0.1f);
                break;
            case FIRST_PERSON_RIGHT_HAND:
            case FIRST_PERSON_LEFT_HAND:
                // 1st person - upright
                poseStack.scale(0.7f, 0.7f, 0.7f);
                poseStack.translate(0.1f, 0.1f, 0.1f);
                break;
        }

        // Back to render space
        poseStack.translate(-0.5f, -0.5f, -0.5f);
    }


    private void renderCanister(PoseStack poseStack, MultiBufferSource bufferSource,
                                int packedLight, int packedOverlay, CanisterData data) {

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(CANISTER_TEXTURE));
        PoseStack.Pose pose = poseStack.last();

        // Render main canister - centered in 16x16 space
        renderItemQuad(pose, consumer,
                3, 1, CANISTER_WIDTH, CANISTER_HEIGHT, // Use original texture coordinates
                CANISTER_U, CANISTER_V, CANISTER_WIDTH, CANISTER_HEIGHT,
                packedLight, packedOverlay);

        // Render fluid indicator if present
        if (data != null && !data.isEmpty() && data.fluidId() != null) {
            renderFluidIndicator(pose, consumer, data, packedLight, packedOverlay);
        }
    }

    private void renderFluidIndicator(PoseStack.Pose pose, VertexConsumer consumer,
                                      CanisterData data, int packedLight, int packedOverlay) {

        float fillPercentage = data.amount() / (float) CanisterItem.MAX_CAPACITY;
        int filledHeight = Math.max(1, (int) (HOLE_HEIGHT * fillPercentage));

        int indicatorU, indicatorV;
        if (data.isCrudeOil()) {
            indicatorU = OIL_INDICATOR_U;
            indicatorV = OIL_INDICATOR_V;
        } else if (data.isRefinedFuel()) {
            indicatorU = FUEL_INDICATOR_U;
            indicatorV = FUEL_INDICATOR_V;
        } else {
            return;
        }

        // Bottom-up filling - take the bottom portion of the indicator
        int sourceV = indicatorV + (INDICATOR_HEIGHT - filledHeight);

        // Render directly in the hole position
        renderItemQuad(pose, consumer,
                HOLE_U, HOLE_V + (HOLE_HEIGHT - filledHeight), // Position in hole (from bottom up)
                HOLE_WIDTH, filledHeight,
                indicatorU, sourceV, // Source texture (bottom portion of indicator)
                INDICATOR_WIDTH, filledHeight,
                packedLight, packedOverlay);
    }

    private void renderItemQuad(PoseStack.Pose pose, VertexConsumer consumer,
                                float x, float y, float width, float height,
                                int texU, int texV, int texWidth, int texHeight,
                                int packedLight, int packedOverlay) {

        final float DEPTH = 1f / 16f;
        final float EPS = 0.0005f;

        // Texture coordinates
        float minU = (texU + EPS) / 16f;
        float maxU = (texU + texWidth - EPS) / 16f;
        float minV = (texV + EPS) / 16f;
        float maxV = (texV + texHeight - EPS) / 16f;

        // Vertex positions - convert from texture space to render space
        // Minecraft render space: 0-1 with 0.5 as center
        float minX = (x - 8f) / 16f + 0.5f; // Center in item space
        float maxX = (x + width - 8f) / 16f + 0.5f;
        float minY = (8f - y - height) / 16f + 0.5f; // Flip Y and center
        float maxY = (8f - y) / 16f + 0.5f;

        // Front face
        consumer.addVertex(pose.pose(), minX, maxY, DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(minU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, 1f);

        consumer.addVertex(pose.pose(), maxX, maxY, DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(maxU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, 1f);

        consumer.addVertex(pose.pose(), maxX, minY, DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(maxU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, 1f);

        consumer.addVertex(pose.pose(), minX, minY, DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(minU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, 1f);

        // Back face (same but reversed and negative Z)
        consumer.addVertex(pose.pose(), minX, minY, -DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(minU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, -1f);

        consumer.addVertex(pose.pose(), maxX, minY, -DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(maxU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, -1f);

        consumer.addVertex(pose.pose(), maxX, maxY, -DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(maxU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, -1f);

        consumer.addVertex(pose.pose(), minX, maxY, -DEPTH)
                .setColor(1f, 1f, 1f, 1f).setUv(minU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 0f, -1f);

        // Top face (facing up)
        consumer.addVertex(pose.pose(), minX, maxY, DEPTH)
                .setColor(0.8f, 0.8f, 0.8f, 1f).setUv(minU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 1f, 0f);

        consumer.addVertex(pose.pose(), maxX, maxY, DEPTH)
                .setColor(0.8f, 0.8f, 0.8f, 1f).setUv(maxU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 1f, 0f);

        consumer.addVertex(pose.pose(), maxX, maxY, -DEPTH)
                .setColor(0.8f, 0.8f, 0.8f, 1f).setUv(maxU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 1f, 0f);

        consumer.addVertex(pose.pose(), minX, maxY, -DEPTH)
                .setColor(0.8f, 0.8f, 0.8f, 1f).setUv(minU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, 1f, 0f);

// Bottom face (facing down)
        consumer.addVertex(pose.pose(), minX, minY, -DEPTH)
                .setColor(0.6f, 0.6f, 0.6f, 1f).setUv(minU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, -1f, 0f);

        consumer.addVertex(pose.pose(), maxX, minY, -DEPTH)
                .setColor(0.6f, 0.6f, 0.6f, 1f).setUv(maxU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, -1f, 0f);

        consumer.addVertex(pose.pose(), maxX, minY, DEPTH)
                .setColor(0.6f, 0.6f, 0.6f, 1f).setUv(maxU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, -1f, 0f);

        consumer.addVertex(pose.pose(), minX, minY, DEPTH)
                .setColor(0.6f, 0.6f, 0.6f, 1f).setUv(minU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 0f, -1f, 0f);

// Left face (facing left)
        consumer.addVertex(pose.pose(), minX, maxY, -DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(minU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, -1f, 0f, 0f);

        consumer.addVertex(pose.pose(), minX, minY, -DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(minU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, -1f, 0f, 0f);

        consumer.addVertex(pose.pose(), minX, minY, DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(minU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, -1f, 0f, 0f);

        consumer.addVertex(pose.pose(), minX, maxY, DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(minU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, -1f, 0f, 0f);

// Right face (facing right)
        consumer.addVertex(pose.pose(), maxX, maxY, DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(maxU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 1f, 0f, 0f);

        consumer.addVertex(pose.pose(), maxX, minY, DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(maxU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 1f, 0f, 0f);

        consumer.addVertex(pose.pose(), maxX, minY, -DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(maxU, maxV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 1f, 0f, 0f);

        consumer.addVertex(pose.pose(), maxX, maxY, -DEPTH)
                .setColor(0.7f, 0.7f, 0.7f, 1f).setUv(maxU, minV)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(pose, 1f, 0f, 0f);
    }
}
