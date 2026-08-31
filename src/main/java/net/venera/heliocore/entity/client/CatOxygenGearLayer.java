package net.venera.heliocore.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;

public class CatOxygenGearLayer extends RenderLayer<Cat, CatModel<Cat>> {
    private final CatOxygenGear<Cat> gearModel;
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/entity/oxygen_gear/cat_oxygen_gear.png");

    public CatOxygenGearLayer(RenderLayerParent<Cat, CatModel<Cat>> parentRenderer, EntityModelSet modelSet) {
        super(parentRenderer);
        this.gearModel = new CatOxygenGear<>(modelSet.bakeLayer(CatOxygenGear.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Cat cat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var inventory = cat.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        boolean hasMask = !inventory.getStackInSlot(0).isEmpty();
        boolean hasConnectors = !inventory.getStackInSlot(1).isEmpty();
        boolean hasLeftTank = !inventory.getStackInSlot(2).isEmpty();
        boolean hasRightTank = !inventory.getStackInSlot(3).isEmpty();
        
        if (!hasMask && !hasConnectors && !hasLeftTank && !hasRightTank) {
            return;
        }
        
        this.getParentModel().copyPropertiesTo(this.gearModel);
        
        this.gearModel.prepareMobModel(cat, limbSwing, limbSwingAmount, partialTicks);
        this.gearModel.setupAnim(cat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        
        this.gearModel.mask.visible = hasMask;
        this.gearModel.connectors.visible = hasConnectors;
        this.gearModel.leftTank.visible = hasLeftTank;
        this.gearModel.rightTank.visible = hasRightTank;
        
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.gearModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }
}