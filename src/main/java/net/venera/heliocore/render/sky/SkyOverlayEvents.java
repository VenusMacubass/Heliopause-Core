package net.venera.heliocore.render.sky;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.venera.heliocore.HeliopauseCore;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@EventBusSubscriber(modid = HeliopauseCore.MOD_ID, value = Dist.CLIENT)
public class SkyOverlayEvents {
    private static final ResourceLocation MERCURY_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/mercury_from_earth.png");
    private static final ResourceLocation VENUS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/venus_from_earth.png");
    private static final ResourceLocation MARS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/mars_from_earth.png");
    private static final ResourceLocation JUPITER_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/jupiter_from_earth.png");
    private static final ResourceLocation SATURN_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/saturn_from_earth.png");
    private static final ResourceLocation URANUS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/uranus_from_earth.png");
    private static final ResourceLocation NEPTUNE_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/neptune_from_earth.png");

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        boolean isOverworld = level.dimension() == Level.OVERWORLD;
        boolean isMoon = level.dimension().location().equals(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon"));

        if (isOverworld || isMoon) {
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
            float exactTime = level.getDayTime() + partialTick;
            float sunRotation = level.getTimeOfDay(partialTick) * 360.0F;
            
            Matrix4f vanillaMat = new Matrix4f();
            vanillaMat.rotateY((float) Math.toRadians(-90.0));
            vanillaMat.rotateX((float) Math.toRadians(sunRotation));
            
            Vector3f sunVec = new Vector3f(0, 1, 0);
            vanillaMat.transformDirection(sunVec);
            sunVec.normalize();
            
            Vector3f moonVec = new Vector3f(0, -1, 0);
            vanillaMat.transformDirection(moonVec);
            moonVec.normalize();
            
            float mercuryRotation = ((exactTime % 57600.0F) / 57600.0F * 360.0F) + 45.0F;
            drawPlanet(
                    event.getModelViewMatrix(),
                    MERCURY_TEXTURE,
                    0.1F,            //Size
                    mercuryRotation,   //Current position in the sky
                    83.0F,           //Rises in the North-West
                    7.0F,             //Tilted orbit
                    sunVec, moonVec, isMoon
            );
            
            float venusRotation = ((exactTime % 240000.0F) / 240000.0F * 360.0F) + 210.0F;
            drawPlanet(event.getModelViewMatrix(), VENUS_TEXTURE, 0.15F, venusRotation, 96.0F, -5F, sunVec, moonVec, isMoon);

            float marsRotation = ((exactTime % 240000.0F) / 240000.0F * 360.0F) + 120.0F;
            drawPlanet(event.getModelViewMatrix(), MARS_TEXTURE, 0.1F, marsRotation, 78.0F, 12F, sunVec, moonVec, isMoon);

            float jupiterRotation = ((exactTime % 720000.0F) / 720000.0F * 360.0F) + 300.0F;
            drawPlanet(event.getModelViewMatrix(), JUPITER_TEXTURE, 0.15F, jupiterRotation, 91.0F, 1.5F, sunVec, moonVec, isMoon);
            
            float saturnRotation = ((exactTime % 240000.0F) / 240000.0F * 360.0F) + 80.0F;
            drawPlanet(event.getModelViewMatrix(), SATURN_TEXTURE, 0.1F, saturnRotation, 98.0F, -4.5F, sunVec, moonVec, isMoon);
            
            float uranusRotation = ((exactTime % 20160000.0F) / 20160000.0F * 360.0F) + 170.0F;
            drawPlanet(event.getModelViewMatrix(), URANUS_TEXTURE, 0.05F, uranusRotation, 85.0F, 6F, sunVec, moonVec, isMoon);
            
            float neptuneRotation = ((exactTime % 39360000.0F) / 39360000.0F * 360.0F) + 250.0F;
            drawPlanet(event.getModelViewMatrix(), NEPTUNE_TEXTURE, 0.05F, neptuneRotation, 102F, -8F, sunVec, moonVec, isMoon);
        }
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void drawPlanet(Matrix4f baseMatrix, ResourceLocation texture, float size, float orbitRotation, float horizonSpawnAngle, float orbitTilt, Vector3f sunVec, Vector3f moonVec, boolean isMoon) {
        Matrix4f planetMat = new Matrix4f();
        planetMat.rotateY((float) Math.toRadians(horizonSpawnAngle))
                .rotateX((float) Math.toRadians(orbitRotation))
                .rotateY((float) Math.toRadians(orbitTilt));

        Vector3f planetVec = new Vector3f(0, 1, 0);
        planetMat.transformDirection(planetVec);
        planetVec.normalize();
        
        float dotSun = planetVec.dot(sunVec);
        float angleToSun = (float) Math.toDegrees(Math.acos(dotSun));
        float sunAlpha = Mth.clamp((angleToSun - 10.0F) / 6.5F, 0.0F, 1.0F);
        
        float moonAlpha = 1.0F;
        if (!isMoon) {
            float dotMoon = planetVec.dot(moonVec);
            float angleToMoon = (float) Math.toDegrees(Math.acos(dotMoon));
            moonAlpha = Mth.clamp((angleToMoon - 15.0F) / 7.0F, 0.0F, 1.0F);
        }

        float finalAlpha = Math.min(moonAlpha, sunAlpha);
        if (finalAlpha <= 0.01F) return;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, finalAlpha);

        Matrix4f matrix = new Matrix4f(baseMatrix);
        matrix.rotateY((float) Math.toRadians(horizonSpawnAngle))
                .rotateX((float) Math.toRadians(orbitRotation))
                .rotateY((float) Math.toRadians(orbitTilt));

        RenderSystem.setShaderTexture(0, texture);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.addVertex(matrix, -size, 100.0F, -size).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, size, 100.0F, -size).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, size, 100.0F, size).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, -size, 100.0F, size).setUv(0.0F, 1.0F);

        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}