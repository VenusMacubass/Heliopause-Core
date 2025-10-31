package net.venera.galacticraftcore.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.venera.galacticraftcore.GalacticraftCore;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class FuelCanisterRenderer extends BlockEntityWithoutLevelRenderer {
    private static final FuelCanisterRenderer INSTANCE = new FuelCanisterRenderer();
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "textures/item/gcc_fuel_canister.png");

    public FuelCanisterRenderer() {
        super(null, null);
    }

    public static FuelCanisterRenderer getInstance() {
        return INSTANCE;
    }

//    @Override
//    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
//                             MultiBufferSource buffer, int packedLight, int packedOverlay) {
//        // Let Minecraft handle the base transformations for us
//        // We just render the custom model on top of the standard item rendering
//
//        float fillRatio = getFillRatio(stack);
//        FluidType fluidType = getFluidType(stack);
//        int fullnessLevel = getDiscreteFullnessLevel(fillRatio); // 0-5
//
//        // Only render fuel gauge if not empty
//        if (fullnessLevel > 0) {
//            renderFuelGauge(poseStack, buffer, packedLight, packedOverlay, fullnessLevel, fluidType);
//        }
//    }

    private int getDiscreteFullnessLevel(float fillRatio) {
        // Convert continuous 0.0-1.0 to discrete 0-5 levels
        if (fillRatio <= 0.0f) return 0; // Empty
        if (fillRatio <= 0.2f) return 1; // 20%
        if (fillRatio <= 0.4f) return 2; // 40%
        if (fillRatio <= 0.6f) return 3; // 60%
        if (fillRatio <= 0.8f) return 4; // 80%
        return 5; // 100%
    }

//    private void renderFuelGauge(PoseStack poseStack, MultiBufferSource buffer,
//                                 int packedLight, int packedOverlay, int fullnessLevel, FluidType fluidType) {
//
//        poseStack.pushPose();
//
//        // Apply standard item rendering transform
//        // This ensures it works in hand, GUI, item frames, etc.
//        poseStack.translate(0.5, 0.5, 0.5);
//
//        VertexConsumer consumer = buffer.getBuffer(Sheets.translucentItemSheet());
//        Matrix4f matrix = poseStack.last().pose();
//        Matrix3f normal = poseStack.last().normal();
//
//        // Fuel gauge positioning (centered on item)
//        float gaugeLeft = 0.25f;
//        float gaugeRight = 0.75f;
//        float gaugeBottom = 0.2f;
//        float gaugeHeight = 0.6f;
//
//        // Each fullness level takes 1/6th of the gauge height
//        float levelHeight = gaugeHeight / 6f;
//        float gaugeTop = gaugeBottom + (levelHeight * fullnessLevel);
//
//        // Determine which fuel texture strip to use
//        float u0, u1;
//        if (fluidType == FluidType.FUEL) {
//            u0 = 0.5f; u1 = 0.75f;  // Yellow fuel strip
//        } else {
//            u0 = 0.75f; u1 = 1.0f;  // Crude oil strip
//        }
//
//        // UV mapping for the specific fullness level
//        // Your texture should have 6 equal vertical segments for the fuel gauge
//        float segmentHeight = 1.0f / 6f; // Each level is 1/6th of texture height
//        float v0 = 1.0f - (segmentHeight * fullnessLevel); // Bottom of current level
//        float v1 = 1.0f - (segmentHeight * (fullnessLevel - 1)); // Top of current level
//
//        // Render fuel gauge slightly in front
//        float z = 0.01f;
//
//        consumer.addVertex(matrix, gaugeLeft - 0.5f, gaugeBottom - 0.5f, z)
//                .setColor(1.0f, 1.0f, 1.0f, 1.0f)
//                .setUv(u0, v1)
//                .setOverlay(packedOverlay)
//                .setLight(packedLight)
//                .setNormal(poseStack.last(), 0, 0, 1);
//
//        consumer.addVertex(matrix, gaugeRight - 0.5f, gaugeBottom - 0.5f, z)
//                .setColor(1.0f, 1.0f, 1.0f, 1.0f)
//                .setUv(u1, v1)
//                .setOverlay(packedOverlay)
//                .setLight(packedLight)
//                .setNormal(poseStack.last(), 0, 0, 1);
//
//        consumer.addVertex(matrix, gaugeRight - 0.5f, gaugeTop - 0.5f, z)
//                .setColor(1.0f, 1.0f, 1.0f, 1.0f)
//                .setUv(u1, v0)
//                .setOverlay(packedOverlay)
//                .setLight(packedLight)
//                .setNormal(poseStack.last(), 0, 0, 1);
//
//        consumer.addVertex(matrix, gaugeLeft - 0.5f, gaugeTop - 0.5f, z)
//                .setColor(1.0f, 1.0f, 1.0f, 1.0f)
//                .setUv(u0, v0)
//                .setOverlay(packedOverlay)
//                .setLight(packedLight)
//                .setNormal(poseStack.last(), 0, 0, 1);
//
//        poseStack.popPose();
//    }
//
//    private float getFillRatio(ItemStack stack) {
//        return 0.75f;
//    }
//
//    private FluidType getFluidType(ItemStack stack) {
//
//
//        return FluidType.FUEL; // Example
//    }

}
