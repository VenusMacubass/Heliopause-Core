package net.venera.heliocore.render.sky;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.venera.heliocore.HeliopauseCore;
import org.joml.Matrix4f;

public class MoonSkyRenderer {

    private static final ResourceLocation EARTH_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/earth_from_moon.png");
    private static final ResourceLocation SUN_TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/environment/sun_from_space.png");
    // 1. The memory cache for our stars
    private static VertexBuffer starBuffer;
    

    public static void renderSky(ClientLevel level, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, Runnable setupFog) {
        final int STAR_COUNT = 2000;
        setupFog.run();

        if (starBuffer == null) {
            createStars(STAR_COUNT);
        }

        RenderSystem.depthMask(false);
        
        RenderSystem.disableCull();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        Tesselator tesselator = Tesselator.getInstance();
        RenderSystem.setShaderFogStart(Float.MAX_VALUE);
        RenderSystem.disableBlend();

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Matrix4f starMatrix = new Matrix4f(modelViewMatrix);
        float starRotation = level.getTimeOfDay(partialTick) * 360.0F;
        starMatrix.rotateY((float) Math.toRadians(-90.0))
                .rotateX((float) Math.toRadians(starRotation));

        starBuffer.bind();
        starBuffer.drawWithShader(starMatrix, projectionMatrix, GameRenderer.getPositionShader());
        VertexBuffer.unbind();
        
        setupFog.run();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f sunMatrix = new Matrix4f(modelViewMatrix);
        float timeRotation = level.getTimeOfDay(partialTick) * 360.0F;

        sunMatrix.rotateY((float) Math.toRadians(-90.0))
                .rotateX((float) Math.toRadians(timeRotation));

        float sunSize = 30.0F;

        RenderSystem.setShaderTexture(0, SUN_TEXTURE);
        BufferBuilder sunBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        sunBuilder.addVertex(sunMatrix, -sunSize, 100.0F, -sunSize).setUv(0.0F, 0.0F);
        sunBuilder.addVertex(sunMatrix, sunSize, 100.0F, -sunSize).setUv(1.0F, 0.0F);
        sunBuilder.addVertex(sunMatrix, sunSize, 100.0F, sunSize).setUv(1.0F, 1.0F);
        sunBuilder.addVertex(sunMatrix, -sunSize, 100.0F, sunSize).setUv(0.0F, 1.0F);

        BufferUploader.drawWithShader(sunBuilder.buildOrThrow());
        Matrix4f earthMatrix = new Matrix4f(modelViewMatrix);
        earthMatrix.rotateX((float) Math.toRadians(45.0))
                .rotateY((float) Math.toRadians(45.0));

        int totalDays = (int) (level.getDayTime() / 24000L);
        int phase = totalDays % 8;

        float u1 = (phase % 4) * 0.25F;
        float u2 = u1 + 0.25F;
        float v1 = (phase / 4) * 0.5F;
        float v2 = v1 + 0.5F;

        float earthSize = 30.0F;

        RenderSystem.setShaderTexture(0, EARTH_TEXTURE);
        BufferBuilder earthBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        earthBuilder.addVertex(earthMatrix, -earthSize, 100.0F, -earthSize).setUv(u1, v1);
        earthBuilder.addVertex(earthMatrix, earthSize, 100.0F, -earthSize).setUv(u2, v1);
        earthBuilder.addVertex(earthMatrix, earthSize, 100.0F, earthSize).setUv(u2, v2);
        earthBuilder.addVertex(earthMatrix, -earthSize, 100.0F, earthSize).setUv(u1, v2);

        BufferUploader.drawWithShader(earthBuilder.buildOrThrow());
        RenderSystem.depthMask(true);
        RenderSystem.enableCull(); 
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void createStars(int starCount) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        RandomSource random = RandomSource.create(10842L);

        for (int i = 0; i < starCount; ++i) {
            double x = random.nextFloat() * 2.0F - 1.0F;
            double y = random.nextFloat() * 2.0F - 1.0F;
            double z = random.nextFloat() * 2.0F - 1.0F;

            double lengthSq = x * x + y * y + z * z;

            if (lengthSq < 1.0 && lengthSq > 0.01) {
                // 1. Get the Normalized Forward Vector (Points outward from center)
                double invLen = 1.0 / Math.sqrt(lengthSq);
                double nx = x * invLen;
                double ny = y * invLen;
                double nz = z * invLen;

                // Position on the 100-radius sphere
                double px = nx * 100.0;
                double py = ny * 100.0;
                double pz = nz * 100.0;

                // 2. Calculate the Right Vector (Cross product of World Up and Forward)
                double rxVec = nz;
                double ryVec = 0;
                double rzVec = -nx;

                // Safety check for stars exactly at the North/South poles
                if (Math.abs(ny) > 0.999) {
                    rxVec = 0;
                    ryVec = -nz;
                    rzVec = ny;
                }

                // Normalize the Right Vector
                double rLen = Math.sqrt(rxVec * rxVec + ryVec * ryVec + rzVec * rzVec);
                rxVec /= rLen;
                ryVec /= rLen;
                rzVec /= rLen;

                // 3. Calculate True Up Vector (Cross product of Forward and Right)
                double uxVec = ny * rzVec - nz * ryVec;
                double uyVec = nz * rxVec - nx * rzVec;
                double uzVec = nx * ryVec - ny * rxVec;

                // Star size and rotation
                double size = 0.09 + random.nextDouble() * 0.1;
                double randRot = random.nextDouble() * Math.PI * 2.0;
                double sinRot = Math.sin(randRot);
                double cosRot = Math.cos(randRot);

                // 4. Draw the 4 corners using the tangent vectors
                for (int j = 0; j < 4; ++j) {
                    // Base 2D coordinates for the quad
                    double vx = ((j & 2) - 1) * size;
                    double vy = (((j + 1) & 2) - 1) * size;

                    // Apply the random 2D spin
                    double rotX = vx * cosRot - vy * sinRot;
                    double rotY = vy * cosRot + vx * sinRot;

                    // Map the 2D quad onto the 3D spherical tangent plane!
                    double finalX = px + (rotX * rxVec) + (rotY * uxVec);
                    double finalY = py + (rotX * ryVec) + (rotY * uyVec);
                    double finalZ = pz + (rotX * rzVec) + (rotY * uzVec);

                    builder.addVertex((float) finalX, (float) finalY, (float) finalZ);
                }
            }
        }

        starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        starBuffer.bind();
        starBuffer.upload(builder.buildOrThrow());
        VertexBuffer.unbind();
    }
}
