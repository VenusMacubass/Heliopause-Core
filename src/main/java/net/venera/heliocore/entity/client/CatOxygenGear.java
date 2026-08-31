package net.venera.heliocore.entity.client;

import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.venera.heliocore.HeliopauseCore;

public class CatOxygenGear<T extends Cat> extends CatModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "cat_oxygen_gear"), "main");

	public final ModelPart mask;
	public final ModelPart connectors;
	public final ModelPart leftTank;
	public final ModelPart rightTank;

	public CatOxygenGear(ModelPart root) {
		super(root);
		
		this.mask = this.head.getChild("mask");
		this.connectors = this.body.getChild("connectors");
		this.leftTank = this.body.getChild("left_tank");
		this.rightTank = this.body.getChild("right_tank");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, -9.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, 1.5708F, 0.0F, 0.0F));
		
		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.offset(1.1F, 14.1F, -5.0F));
		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.offset(-1.1F, 14.1F, -5.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.offset(1.1F, 18.0F, 5.0F));
		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offset(-1.1F, 18.0F, 5.0F));
		partdefinition.addOrReplaceChild("tail1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 15.5F, 8.0F, 1.5708F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 15.5F, 16.0F, 1.5708F, 0.0F, 0.0F));
		
		head.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(38, 18).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		body.addOrReplaceChild("connectors", CubeListBuilder.create().texOffs(22, 28).addBox(-1.0F, 3.0F, -2.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		body.addOrReplaceChild("left_tank", CubeListBuilder.create().texOffs(29, 25).addBox(0.0F, 6.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		body.addOrReplaceChild("right_tank", CubeListBuilder.create().texOffs(29, 25).addBox(-2.0F, 6.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}