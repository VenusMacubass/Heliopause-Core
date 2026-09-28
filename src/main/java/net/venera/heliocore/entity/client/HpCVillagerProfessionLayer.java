package net.venera.heliocore.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.resources.metadata.animation.VillagerMetaDataSection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.entity.villager.HpCVillagers;

public class HpCVillagerProfessionLayer extends VillagerProfessionLayer<Villager, VillagerModel<Villager>> {

    private static final Int2ObjectMap<ResourceLocation> LUNAR_LEVEL_LOCATIONS = Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.put(1, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "stone"));
        map.put(2, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "iron"));
        map.put(3, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "gold"));
        map.put(4, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "emerald"));
        map.put(5, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "diamond"));
    });

    private final Object2ObjectMap<VillagerType, VillagerMetaDataSection.Hat> typeHatCache = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<VillagerProfession, VillagerMetaDataSection.Hat> professionHatCache = new Object2ObjectOpenHashMap<>();

    public HpCVillagerProfessionLayer(RenderLayerParent<Villager, VillagerModel<Villager>> renderer, ResourceManager resourceManager, String path) {
        super(renderer, resourceManager, path);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Villager villager,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (villager.isInvisible()) return;

        VillagerData data = villager.getVillagerData();
        VillagerType type = data.getType();

        // If this is a regular Overworld villager, run the normal vanilla belt-buckle layer
        if (type != HpCVillagers.LUNAR_HIGHLANDS.get() && type != HpCVillagers.LUNAR_MARIA.get()) {
            super.render(poseStack, buffer, packedLight, villager, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            return;
        }
        
        VillagerProfession profession = data.getProfession();
        VillagerMetaDataSection.Hat typeHat = this.getHatData(this.typeHatCache, "type", BuiltInRegistries.VILLAGER_TYPE, type);
        VillagerMetaDataSection.Hat profHat = this.getHatData(this.professionHatCache, "profession", BuiltInRegistries.VILLAGER_PROFESSION, profession);
        VillagerModel<Villager> model = this.getParentModel();

        model.hatVisible(profHat == VillagerMetaDataSection.Hat.NONE || (profHat == VillagerMetaDataSection.Hat.PARTIAL && typeHat != VillagerMetaDataSection.Hat.FULL));
        ResourceLocation typeTexture = getTexturePath("type", BuiltInRegistries.VILLAGER_TYPE.getKey(type));
        renderColoredCutoutModel(model, typeTexture, poseStack, buffer, packedLight, villager, -1);
        model.hatVisible(true);

        if (profession != VillagerProfession.NONE && !villager.isBaby()) {
            ResourceLocation profTexture = getTexturePath("profession", BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession));
            renderColoredCutoutModel(model, profTexture, poseStack, buffer, packedLight, villager, -1);

            if (profession != VillagerProfession.NITWIT) {
                ResourceLocation levelKey = LUNAR_LEVEL_LOCATIONS.get(Mth.clamp(data.getLevel(), 1, LUNAR_LEVEL_LOCATIONS.size()));
                ResourceLocation levelTexture = getTexturePath("profession_level", levelKey);
                renderColoredCutoutModel(model, levelTexture, poseStack, buffer, packedLight, villager, -1);
            }
        }
    }

    private ResourceLocation getTexturePath(String folder, ResourceLocation key) {
        return key.withPath(path -> "textures/entity/villager/" + folder + "/" + path + ".png");
    }
}
