package net.venera.heliocore.entity.client;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Spider;
import net.venera.heliocore.HeliopauseCore;

public class SpiderOxygenGear<T extends Spider> extends SpiderModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "spider_oxygen_gear"), "main");

	public final ModelPart mask;
	public final ModelPart connectors;
	public final ModelPart leftTank;
	public final ModelPart rightTank;

	public SpiderOxygenGear(ModelPart root) {
		super(root);

		this.mask = root.getChild("head").getChild("mask");
		this.connectors = root.getChild("body0").getChild("connectors");
		this.leftTank = root.getChild("body1").getChild("left_tank");
		this.rightTank = root.getChild("body1").getChild("right_tank");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, -3.0F));
		PartDefinition body0 = partdefinition.addOrReplaceChild("body0", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 0.0F));
		PartDefinition body1 = partdefinition.addOrReplaceChild("body1", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 9.0F));

		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, 15.0F, 4.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 15.0F, 4.0F));
		partdefinition.addOrReplaceChild("right_middle_hind_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, 15.0F, 1.0F));
		partdefinition.addOrReplaceChild("left_middle_hind_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 15.0F, 1.0F));
		partdefinition.addOrReplaceChild("right_middle_front_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, 15.0F, -2.0F));
		partdefinition.addOrReplaceChild("left_middle_front_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 15.0F, -2.0F));
		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, 15.0F, -5.0F));
		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 15.0F, -5.0F));

		head.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);

		body0.addOrReplaceChild("connectors", CubeListBuilder.create()
				.texOffs(56, 25).addBox(-3.0F, -5.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(39, 25).addBox(-3.0F, -4.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(38, 24).addBox(-2.0F, -4.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(56, 20).addBox(-2.0F, -4.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(56, 22).addBox(-3.0F, -6.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(56, 25).addBox(2.0F, -5.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(39, 25).addBox(2.0F, -4.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(38, 24).addBox(1.0F, -4.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(56, 20).addBox(1.0F, -4.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(56, 22).addBox(2.0F, -6.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		body1.addOrReplaceChild("left_tank", CubeListBuilder.create().texOffs(40, 20).addBox(1.0F, -7.0F, -6.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		body1.addOrReplaceChild("right_tank", CubeListBuilder.create().texOffs(40, 20).addBox(-4.0F, -7.0F, -6.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}