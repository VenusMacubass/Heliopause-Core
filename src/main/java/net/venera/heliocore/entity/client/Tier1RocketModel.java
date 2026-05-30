package net.venera.heliocore.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.venera.heliocore.HeliopauseCore;

public class Tier1RocketModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "tier_1_rocket"), "main");
	private final ModelPart engine;
	private final ModelPart upper_wings;
	private final ModelPart upperwings_1;
	private final ModelPart upper_wings_2;
	private final ModelPart upper_wings_3;
	private final ModelPart upper_wings_4;
	private final ModelPart wings;
	private final ModelPart wing_1;
	private final ModelPart wing_2;
	private final ModelPart wing_3;
	private final ModelPart wing_4;
	private final ModelPart walls;
	private final ModelPart top;
	private final ModelPart bb_main;

	public Tier1RocketModel(ModelPart root) {
		this.engine = root.getChild("engine");
		this.upper_wings = root.getChild("upper_wings");
		this.upperwings_1 = this.upper_wings.getChild("upperwings_1");
		this.upper_wings_2 = this.upper_wings.getChild("upper_wings_2");
		this.upper_wings_3 = this.upper_wings.getChild("upper_wings_3");
		this.upper_wings_4 = this.upper_wings.getChild("upper_wings_4");
		this.wings = root.getChild("wings");
		this.wing_1 = this.wings.getChild("wing_1");
		this.wing_2 = this.wings.getChild("wing_2");
		this.wing_3 = this.wings.getChild("wing_3");
		this.wing_4 = this.wings.getChild("wing_4");
		this.walls = root.getChild("walls");
		this.top = root.getChild("top");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition engine = partdefinition.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(82, 48).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 37).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(56, 13).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition upper_wings = partdefinition.addOrReplaceChild("upper_wings", CubeListBuilder.create(), PartPose.offset(-10.0F, -2.0F, 10.0F));

		PartDefinition upperwings_1 = upper_wings.addOrReplaceChild("upperwings_1", CubeListBuilder.create().texOffs(102, 100).addBox(-4.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 0).addBox(-3.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 92).addBox(-2.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(20.0F, -1.0F, 0.0F));

		PartDefinition upper_wings_2 = upper_wings.addOrReplaceChild("upper_wings_2", CubeListBuilder.create().texOffs(104, 7).addBox(2.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 14).addBox(1.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 96).addBox(1.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition upper_wings_3 = upper_wings.addOrReplaceChild("upper_wings_3", CubeListBuilder.create().texOffs(104, 21).addBox(-4.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 28).addBox(-3.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 100).addBox(-2.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, -1.0F, -20.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition upper_wings_4 = upper_wings.addOrReplaceChild("upper_wings_4", CubeListBuilder.create().texOffs(104, 35).addBox(-4.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 106).addBox(-3.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 102).addBox(-2.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -20.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition wings = partdefinition.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(-15.0F, 17.0F, 28.0F));

		PartDefinition wing_1 = wings.addOrReplaceChild("wing_1", CubeListBuilder.create().texOffs(0, 92).addBox(22.0F, -7.0F, -29.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 92).addBox(26.0F, -5.0F, -29.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(96, 36).addBox(28.0F, -2.0F, -29.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 102).addBox(30.0F, 1.0F, -29.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing_2 = wings.addOrReplaceChild("wing_2", CubeListBuilder.create().texOffs(12, 92).addBox(-26.0F, -7.0F, -29.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(94, 77).addBox(-28.0F, -5.0F, -29.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 97).addBox(-30.0F, -2.0F, -29.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 102).addBox(-32.0F, 1.0F, -29.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(30.0F, 0.0F, 0.0F));

		PartDefinition wing_3 = wings.addOrReplaceChild("wing_3", CubeListBuilder.create().texOffs(82, 77).addBox(-29.0F, -7.0F, 22.0F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(94, 87).addBox(-29.0F, -5.0F, 26.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 97).addBox(-29.0F, -2.0F, 28.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(102, 77).addBox(-29.0F, 1.0F, 30.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(43.0F, 0.0F, -43.0F));

		PartDefinition wing_4 = wings.addOrReplaceChild("wing_4", CubeListBuilder.create().texOffs(82, 89).addBox(1.0F, -7.0F, 4.0F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(96, 26).addBox(1.0F, -5.0F, 2.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(94, 97).addBox(1.0F, -2.0F, 0.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(102, 85).addBox(1.0F, 1.0F, -2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 0.0F, -43.0F));

		PartDefinition walls = partdefinition.addOrReplaceChild("walls", CubeListBuilder.create().texOffs(0, 102).addBox(-13.0F, -11.0F, -7.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 81).addBox(-13.0F, -6.0F, -7.0F, 14.0F, 25.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(82, 101).addBox(-3.0F, -11.0F, -7.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(82, 67).addBox(-13.0F, -13.0F, -7.0F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 48).addBox(-13.0F, -13.0F, 6.0F, 14.0F, 32.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-13.0F, -13.0F, -6.0F, 1.0F, 32.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(26, 48).addBox(0.0F, -13.0F, -6.0F, 1.0F, 32.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -4.0F, 0.0F));

		PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 18).addBox(-7.0F, 2.0F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(56, 0).addBox(-6.0F, 1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(56, 26).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(82, 58).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(82, 70).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 92).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(102, 93).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 42).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -20.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		engine.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		upper_wings.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		wings.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		walls.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		top.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}