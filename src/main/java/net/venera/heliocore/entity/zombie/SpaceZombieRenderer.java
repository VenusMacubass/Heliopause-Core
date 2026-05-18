package net.venera.heliocore.entity.zombie;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.venera.heliocore.HeliopauseCore;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SpaceZombieRenderer extends MobRenderer<SpaceZombieEntity, ZombieModel<SpaceZombieEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/entity/space_zombie.png");

    public SpaceZombieRenderer(EntityRendererProvider.Context context) {
        // Uses vanilla ZombieModel - no custom model needed!
        super(context, new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SpaceZombieEntity entity) {
        return TEXTURE; // Your custom texture
    }
}
