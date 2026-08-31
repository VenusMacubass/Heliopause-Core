package net.venera.heliocore.entity.client;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import net.venera.heliocore.HeliopauseCore;

public class WolfOxygenGear<T extends Wolf> extends WolfModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "wolf_oxygen_gear"), "main");

	public final ModelPart mask;
	public final ModelPart connectors;
	public final ModelPart leftTank;
	public final ModelPart rightTank;

	public WolfOxygenGear(ModelPart root) {
		super(root);
		
		this.mask = root.getChild("head").getChild("mask");
		this.connectors = root.getChild("upper_body").getChild("connectors");
		this.leftTank = root.getChild("body").getChild("left_tank");
		this.rightTank = root.getChild("body").getChild("right_tank");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0F, 13.5F, -7.0F));
		
		PartDefinition upperBody = partdefinition.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-1.0F, 12.0F, 10.0F));
		
		head.addOrReplaceChild("real_head", CubeListBuilder.create(), PartPose.ZERO);
		tail.addOrReplaceChild("real_tail", CubeListBuilder.create(), PartPose.ZERO);
		
		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offset(-2.5F, 16.0F, 7.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.offset(0.5F, 16.0F, 7.0F));
		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.offset(-2.5F, 16.0F, -4.0F));
		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.offset(0.5F, 16.0F, -4.0F));
		
		head.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(34, 16).addBox(-3.0F, -5.5F, -6.0F, 8.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		
		upperBody.addOrReplaceChild("connectors", CubeListBuilder.create().texOffs(23, 30).addBox(-1.0F, 1.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(60, 9).addBox(-1.0F, -3.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(60, 9).addBox(2.0F, -3.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 30).addBox(-1.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 30).addBox(2.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		body.addOrReplaceChild("left_tank", CubeListBuilder.create().texOffs(56, 0).addBox(1.0F, -2.0F, 3.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		body.addOrReplaceChild("right_tank", CubeListBuilder.create().texOffs(56, 0).addBox(-3.0F, -2.0F, 3.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}