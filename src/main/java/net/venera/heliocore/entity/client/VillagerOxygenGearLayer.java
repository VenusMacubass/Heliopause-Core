package net.venera.heliocore.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.entity.villager.HpCVillagers;
import net.venera.heliocore.item.HpCTags;

public class VillagerOxygenGearLayer extends RenderLayer<Villager, VillagerModel<Villager>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/entity/oxygen_gear/villager_oxygen_gear.png");
    private final VillagerOxygenGear<Villager> oxygenGearModel;

    public VillagerOxygenGearLayer(RenderLayerParent<Villager, VillagerModel<Villager>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.oxygenGearModel = new VillagerOxygenGear<>(modelSet.bakeLayer(VillagerOxygenGear.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Villager villager,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        var inventory = villager.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        boolean hasMask = !inventory.getStackInSlot(0).isEmpty();
        boolean hasConnectors = !inventory.getStackInSlot(1).isEmpty();
        boolean hasLeftTank = !inventory.getStackInSlot(2).isEmpty();
        boolean hasRightTank = !inventory.getStackInSlot(3).isEmpty();

        // --- SPACE SUIT HELMET CHECK ---
        var headArmor = villager.getItemBySlot(EquipmentSlot.HEAD);
        boolean hasSpaceHelmet = headArmor.is(HpCTags.Items.T1_PRESSURE_PROTECTORS) ||
                headArmor.is(HpCTags.Items.T2_PRESSURE_PROTECTORS);

        // If they have the full helmet on, hide the internal oxygen mask
        if (hasSpaceHelmet) {
            hasMask = false;
        }

        // Exit early if no gear is equipped at all
        if (!hasMask && !hasConnectors && !hasLeftTank && !hasRightTank) {
            return;
        }

        // 1. Sync the base villager's body rotations and animations to our custom model
        this.getParentModel().copyPropertiesTo(this.oxygenGearModel);
        this.oxygenGearModel.setupAnim(villager, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // Apply targeted visibility to individual parts
        this.oxygenGearModel.mask.visible = hasMask;
        this.oxygenGearModel.shortNose.visible = hasMask;
        this.oxygenGearModel.connectors.visible = hasConnectors;
        this.oxygenGearModel.leftTank.visible = hasLeftTank;
        this.oxygenGearModel.rightTank.visible = hasRightTank;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.oxygenGearModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }
}
