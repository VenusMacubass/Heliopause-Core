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
import net.minecraft.world.entity.npc.AbstractVillager;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.item.HpCTags;

public class VillagerOxygenGearLayer<T extends AbstractVillager> extends RenderLayer<T, VillagerModel<T>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/entity/oxygen_gear/villager_oxygen_gear.png");
    private final VillagerOxygenGear<T> oxygenGearModel;

    public VillagerOxygenGearLayer(RenderLayerParent<T, VillagerModel<T>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.oxygenGearModel = new VillagerOxygenGear<>(modelSet.bakeLayer(VillagerOxygenGear.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        var inventory = entity.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        boolean hasMask = !inventory.getStackInSlot(0).isEmpty();
        boolean hasConnectors = !inventory.getStackInSlot(1).isEmpty();
        boolean hasLeftTank = !inventory.getStackInSlot(2).isEmpty();
        boolean hasRightTank = !inventory.getStackInSlot(3).isEmpty();
        
        var headArmor = entity.getItemBySlot(EquipmentSlot.HEAD);
        boolean hasSpaceHelmet = headArmor.is(HpCTags.Items.PRESSURE_PROTECTORS_T1) ||
                headArmor.is(HpCTags.Items.PRESSURE_PROTECTORS_T2);
        
        if (hasSpaceHelmet) {
            hasMask = false;
        }
        
        if (!hasMask && !hasConnectors && !hasLeftTank && !hasRightTank) {
            return;
        }
        
        this.getParentModel().copyPropertiesTo(this.oxygenGearModel);
        this.oxygenGearModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        
        this.oxygenGearModel.mask.visible = hasMask;
        this.oxygenGearModel.shortNose.visible = hasMask;
        this.oxygenGearModel.connectors.visible = hasConnectors;
        this.oxygenGearModel.leftTank.visible = hasLeftTank;
        this.oxygenGearModel.rightTank.visible = hasRightTank;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.oxygenGearModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }
}
