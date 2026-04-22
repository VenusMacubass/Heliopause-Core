package net.venera.galacticraftcore.render.sky;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.venera.galacticraftcore.GalacticraftCore;
import org.joml.Matrix4f;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = GalacticraftCore.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class SkyOverlayEvents {

    private static final ResourceLocation SATURN_TEXTURE = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "textures/environment/saturn_from_earth.png");

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        boolean isOverworld = level.dimension() == Level.OVERWORLD;
        boolean isMoon = level.dimension().location().equals(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon"));

        if (isOverworld || isMoon) {
            Tesselator tesselator = Tesselator.getInstance();

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            
            Matrix4f matrix4f = new Matrix4f(event.getModelViewMatrix());

            float drifterRotation = (level.getGameTime() % 240000L) / 240000.0F * 360.0F;

            matrix4f.rotateY((float) Math.toRadians(-45.0))
                    .rotateX((float) Math.toRadians(drifterRotation))
                    .rotateY((float) Math.toRadians(30.0));

            float planetSize = 0.1F;

            RenderSystem.setShaderTexture(0, SATURN_TEXTURE);

            // Safety check: Reset the color to pure white so the texture renders properly
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            // ... (rest of your addVertex code remains exactly the same)

            builder.addVertex(matrix4f, -planetSize, 100.0F, -planetSize).setUv(0.0F, 0.0F);
            builder.addVertex(matrix4f, planetSize, 100.0F, -planetSize).setUv(1.0F, 0.0F);
            builder.addVertex(matrix4f, planetSize, 100.0F, planetSize).setUv(1.0F, 1.0F);
            builder.addVertex(matrix4f, -planetSize, 100.0F, planetSize).setUv(0.0F, 1.0F);

            BufferUploader.drawWithShader(builder.buildOrThrow());
            RenderSystem.disableBlend();
        }
    }
}