package net.venera.heliocore.entity.client;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.venera.heliocore.HeliopauseCore;

public class CreeperOxygenGear<T extends Creeper> extends CreeperModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "creeper_oxygen_gear"), "main");

	public final ModelPart mask;
	public final ModelPart connectors;
	public final ModelPart leftTank;
	public final ModelPart rightTank;

	public CreeperOxygenGear(ModelPart root) {
		super(root);

		this.mask = root.getChild("head").getChild("mask");
		this.connectors = root.getChild("body").getChild("connectors");
		this.leftTank = root.getChild("body").getChild("left_tank");
		this.rightTank = root.getChild("body").getChild("right_tank");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 18.0F, 4.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 18.0F, 4.0F));
		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 18.0F, -4.0F));
		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 18.0F, -4.0F));

		head.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);

		body.addOrReplaceChild("connectors", CubeListBuilder.create()
				.texOffs(22, 30).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 28).addBox(-1.0F, 1.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 30).addBox(-3.0F, 2.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 30).addBox(2.0F, 2.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 30).addBox(-3.0F, 2.0F, 2.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		body.addOrReplaceChild("left_tank", CubeListBuilder.create().texOffs(28, 21).addBox(1.0F, 3.0F, 2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		body.addOrReplaceChild("right_tank", CubeListBuilder.create().texOffs(28, 21).addBox(-4.0F, 3.0F, 2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}