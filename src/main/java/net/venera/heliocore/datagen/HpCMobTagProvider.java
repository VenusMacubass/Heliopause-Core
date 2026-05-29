package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

import static net.venera.heliocore.util.HpCTags.Entities.OXYGEN_SLOTS_RECEIVERS;

public class HpCMobTagProvider extends EntityTypeTagsProvider {
    public HpCMobTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, HeliopauseCore.MOD_ID, existingFileHelper);
    }
    

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(OXYGEN_SLOTS_RECEIVERS)
                .addTag(EntityTypeTags.UNDEAD)
                .addTag(EntityTypeTags.ARTHROPOD)
                .addTag(EntityTypeTags.RAIDERS)
                .addTag(EntityTypeTags.AQUATIC)
                .addTag(EntityTypeTags.FROG_FOOD); 
        
        tag(OXYGEN_SLOTS_RECEIVERS)
                .add(EntityType.CREEPER, EntityType.ENDERMAN, EntityType.GHAST, EntityType.BLAZE, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN);
        
        tag(OXYGEN_SLOTS_RECEIVERS)
                .add(EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN, EntityType.HORSE)
                .add(EntityType.VILLAGER, EntityType.WANDERING_TRADER);
    }
  
}
