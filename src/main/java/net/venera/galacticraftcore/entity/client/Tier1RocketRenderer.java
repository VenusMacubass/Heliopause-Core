package net.venera.galacticraftcore.entity.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.venera.galacticraftcore.entity.rideable.Tier1RocketEntity;

public class Tier1RocketRenderer extends EntityRenderer<Tier1RocketEntity> {
    public Tier1RocketRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Tier1RocketEntity tier1RocketEntity) {
        return null;
    }

    @Override
    public boolean shouldRender(Tier1RocketEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }
}
