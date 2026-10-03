package net.venera.heliocore.entity.client;

import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.venera.heliocore.HeliopauseCore;

public class VillagerOxygenGear<T extends Entity> extends VillagerModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "villager_oxygen_gear"), "main");

	public final ModelPart mask;
	public final ModelPart shortNose;
	public final ModelPart connectors;
	public final ModelPart leftTank;
	public final ModelPart rightTank;

	public VillagerOxygenGear(ModelPart root) {
		super(root);

		ModelPart headPart = root.getChild("head");
		this.mask = headPart.getChild("mask");
		this.shortNose = headPart.getChild("short_nose");

		ModelPart bodyPart = root.getChild("body");
		this.connectors = bodyPart.getChild("connectors");
		this.leftTank = bodyPart.getChild("left_tank");
		this.rightTank = bodyPart.getChild("right_tank");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// 1. Standard Villager Skeleton Bones Required by Minecraft
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		hat.addOrReplaceChild("hat_rim", CubeListBuilder.create(), PartPose.ZERO);
		head.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.ZERO); // Required by super(root)

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		body.addOrReplaceChild("jacket", CubeListBuilder.create(), PartPose.ZERO);

		partdefinition.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 2.95F, -1.05F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));

		// 2. Individual Gear Components
		// Mask (from Blockbench 'headwear')
		head.addOrReplaceChild("mask", CubeListBuilder.create()
						.texOffs(28, 0).addBox(-4.0F, -10.0F, -6.0F, 8.0F, 10.0F, 10.0F, new CubeDeformation(0.51F)),
				PartPose.ZERO);

		// Shortened Nose
		head.addOrReplaceChild("short_nose", CubeListBuilder.create()
						.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -2.0F, 0.0F));

		// Connectors (from Blockbench 'body')
		body.addOrReplaceChild("connectors", CubeListBuilder.create()
						.texOffs(16, 6).addBox(-1.0F, 0.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(2, 6).addBox(-3.0F, 1.0F, 3.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(17, 4).addBox(2.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(17, 4).addBox(-3.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		// Left Tank
		body.addOrReplaceChild("left_tank", CubeListBuilder.create()
						.texOffs(11, 8).addBox(0.0F, 2.0F, 3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		// Right Tank
		body.addOrReplaceChild("right_tank", CubeListBuilder.create()
						.texOffs(11, 8).addBox(-4.0F, 2.0F, 3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}