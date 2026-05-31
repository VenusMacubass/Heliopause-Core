package net.venera.heliocore.entity.client;// Made with Blockbench 5.0.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

		PartDefinition engine = partdefinition.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(0, 77).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(108, 15).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(104, 97).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 95).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition upper_wings = partdefinition.addOrReplaceChild("upper_wings", CubeListBuilder.create(), PartPose.offset(-10.0F, -10.0F, 10.0F));

		PartDefinition upperwings_1 = upper_wings.addOrReplaceChild("upperwings_1", CubeListBuilder.create().texOffs(96, 120).addBox(-4.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(104, 120).addBox(-3.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(32, 77).addBox(-2.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(22.0F, -1.0F, 2.0F));

		PartDefinition upper_wings_2 = upper_wings.addOrReplaceChild("upper_wings_2", CubeListBuilder.create().texOffs(112, 120).addBox(2.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(120, 120).addBox(1.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(32, 81).addBox(1.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -1.0F, 2.0F));

		PartDefinition upper_wings_3 = upper_wings.addOrReplaceChild("upper_wings_3", CubeListBuilder.create().texOffs(32, 122).addBox(-4.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(40, 122).addBox(-3.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(114, 91).addBox(-2.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.0F, -1.0F, -22.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition upper_wings_4 = upper_wings.addOrReplaceChild("upper_wings_4", CubeListBuilder.create().texOffs(48, 122).addBox(-4.0F, -10.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(124, 110).addBox(-3.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(118, 91).addBox(-2.0F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, -22.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition wings = partdefinition.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(-15.0F, 17.0F, 28.0F));

		PartDefinition wing_1 = wings.addOrReplaceChild("wing_1", CubeListBuilder.create().texOffs(56, 110).addBox(22.0F, -7.0F, -29.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(92, 110).addBox(26.0F, -5.0F, -29.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(8, 119).addBox(28.0F, -2.0F, -29.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 119).addBox(30.0F, 1.0F, -29.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition wing_2 = wings.addOrReplaceChild("wing_2", CubeListBuilder.create().texOffs(68, 110).addBox(-26.0F, -7.0F, -29.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(100, 110).addBox(-28.0F, -5.0F, -29.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(16, 119).addBox(-30.0F, -2.0F, -29.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(24, 119).addBox(-32.0F, 1.0F, -29.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(28.0F, 0.0F, 0.0F));

		PartDefinition wing_3 = wings.addOrReplaceChild("wing_3", CubeListBuilder.create().texOffs(32, 110).addBox(-29.0F, -7.0F, 22.0F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(108, 110).addBox(-29.0F, -5.0F, 26.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 120).addBox(-29.0F, -2.0F, 28.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(64, 120).addBox(-29.0F, 1.0F, 30.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(43.0F, 0.0F, -41.0F));

		PartDefinition wing_4 = wings.addOrReplaceChild("wing_4", CubeListBuilder.create().texOffs(44, 110).addBox(1.0F, -7.0F, 4.0F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(116, 110).addBox(1.0F, -5.0F, 2.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(72, 120).addBox(1.0F, -2.0F, 0.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(88, 120).addBox(1.0F, 1.0F, -2.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 0.0F, -45.0F));

		PartDefinition walls = partdefinition.addOrReplaceChild("walls", CubeListBuilder.create().texOffs(80, 110).addBox(-15.0F, -8.0F, -9.0F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(106, 58).addBox(-15.0F, -2.0F, -9.0F, 18.0F, 21.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(96, 15).addBox(-2.0F, -8.0F, -9.0F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(108, 26).addBox(-15.0F, -19.0F, -9.0F, 18.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(70, 58).addBox(-14.0F, -19.0F, 8.0F, 17.0F, 38.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 22).addBox(-15.0F, -19.0F, -8.0F, 1.0F, 38.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(36, 41).addBox(2.0F, -19.0F, -8.0F, 1.0F, 38.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -4.0F, 0.0F));

		PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(72, 0).addBox(-7.0F, 4.0F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(56, 97).addBox(-6.0F, 3.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(106, 80).addBox(-5.0F, 2.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 110).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(72, 15).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(18, 87).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(80, 117).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(106, 91).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(36, 22).addBox(-9.0F, 6.0F, -9.0F, 18.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(70, 41).addBox(-8.0F, 5.0F, -8.0F, 16.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -30.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, -9.0F, 18.0F, 4.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(0, 87).addBox(-4.0F, -36.0F, -9.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
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