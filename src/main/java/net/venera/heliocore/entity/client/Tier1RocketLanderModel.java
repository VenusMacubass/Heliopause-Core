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

public class Tier1RocketLanderModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LANDER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "tier_1_rocket_lander"), "main");
	private final ModelPart walls;
	private final ModelPart legs_sw;
	private final ModelPart legs_ne;
	private final ModelPart legs_se;
	private final ModelPart legs_nw;
	private final ModelPart bb_main;

	public Tier1RocketLanderModel(ModelPart root) {
		this.walls = root.getChild("walls");
		this.legs_sw = root.getChild("legs_sw");
		this.legs_ne = root.getChild("legs_ne");
		this.legs_se = root.getChild("legs_se");
		this.legs_nw = root.getChild("legs_nw");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition walls = partdefinition.addOrReplaceChild("walls", CubeListBuilder.create().texOffs(0, 142).addBox(-12.0F, -32.0F, -10.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 127).addBox(-18.0F, -32.0F, -10.0F, 6.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-18.0F, -34.0F, -10.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(106, 138).addBox(-4.0F, -32.0F, -10.0F, 6.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 116).addBox(-18.0F, -20.0F, -10.0F, 20.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 93).addBox(-18.0F, -32.0F, 8.0F, 20.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 68).addBox(-18.0F, -32.0F, -8.0F, 2.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(84, 68).addBox(0.0F, -32.0F, -8.0F, 2.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(96, 143).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 5.0F, 0.0F));

		PartDefinition legs_sw = partdefinition.addOrReplaceChild("legs_sw", CubeListBuilder.create().texOffs(122, 140).addBox(-1.0F, -4.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(122, 28).addBox(15.0F, 15.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(26, 141).addBox(6.0F, -9.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(80, 143).addBox(16.0F, 1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 5.0F, 9.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r1 = legs_sw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 137).addBox(-1.0F, -2.0F, -1.0F, 11.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r2 = legs_sw.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(124, 130).addBox(-1.0F, -2.0F, -1.0F, 12.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, 4.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r3 = legs_sw.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 39).addBox(-1.0F, -2.0F, -1.0F, 15.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition legs_ne = partdefinition.addOrReplaceChild("legs_ne", CubeListBuilder.create().texOffs(138, 140).addBox(-1.0F, -4.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(124, 110).addBox(-21.0F, 15.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(146, 28).addBox(-10.0F, -9.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(148, 0).addBox(-20.0F, 1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 5.0F, -9.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r4 = legs_ne.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(80, 138).addBox(-10.0F, -2.0F, -1.0F, 11.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r5 = legs_ne.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(124, 135).addBox(-11.0F, -2.0F, -1.0F, 12.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, 4.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r6 = legs_ne.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(120, 105).addBox(-14.0F, -2.0F, -1.0F, 15.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition legs_se = partdefinition.addOrReplaceChild("legs_se", CubeListBuilder.create().texOffs(148, 8).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(124, 120).addBox(-3.0F, 15.0F, 15.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(148, 16).addBox(-2.0F, -9.0F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(148, 75).addBox(-2.0F, 1.0F, 16.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 5.0F, 9.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r7 = legs_se.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(122, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r8 = legs_se.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(120, 75).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 17.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r9 = legs_se.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(120, 39).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 8.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition legs_nw = partdefinition.addOrReplaceChild("legs_nw", CubeListBuilder.create().texOffs(148, 83).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 127).addBox(-3.0F, 15.0F, 15.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(148, 91).addBox(-2.0F, -9.0F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(148, 118).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(148, 110).addBox(-2.0F, 1.0F, 16.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 5.0F, -9.0F, 0.0F, 2.3562F, 0.0F));

		PartDefinition cube_r10 = legs_nw.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(122, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r11 = legs_nw.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(120, 90).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 17.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r12 = legs_nw.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(120, 57).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 8.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(48, 47).addBox(-9.0F, -19.0F, -9.0F, 18.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 24.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 47).addBox(9.0F, -46.0F, -8.0F, 8.0F, 30.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).addBox(-8.0F, -48.0F, 10.0F, 16.0F, 34.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(44, 116).addBox(-14.0F, -43.0F, -7.0F, 4.0F, 23.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		walls.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		legs_sw.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		legs_ne.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		legs_se.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		legs_nw.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}