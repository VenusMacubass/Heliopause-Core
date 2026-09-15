package net.venera.heliocore.entity.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.venera.heliocore.HeliopauseCore;

public class HumanoidOxygenGear<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "humanoid_oxygen_gear"), "main");

	public final ModelPart mask;
	public final ModelPart connectors;
	public final ModelPart leftTank;
	public final ModelPart rightTank;

	public HumanoidOxygenGear(ModelPart root) {
		super(root);

		this.mask = this.head.getChild("mask");
		this.connectors = this.body.getChild("connectors");
		this.leftTank = this.body.getChild("left_tank");
		this.rightTank = this.body.getChild("right_tank");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// 1. Standard Humanoid Skeleton Bones Required by Minecraft
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

		// 2. Individual Gear Components
		head.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);

		// Grouping the connector/hose cubes into a single separate part
		body.addOrReplaceChild("connectors", CubeListBuilder.create()
						.texOffs(15, 35).addBox(-3.0F, 1.0F, 2.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(2, 1).addBox(2.0F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(2, 1).addBox(-3.0F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(15, 37).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		// Isolated Left Tank
		body.addOrReplaceChild("left_tank", CubeListBuilder.create()
						.texOffs(2, 33).addBox(1.0F, 2.0F, 2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		// Isolated Right Tank
		body.addOrReplaceChild("right_tank", CubeListBuilder.create()
						.texOffs(2, 33).addBox(-4.0F, 2.0F, 2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}