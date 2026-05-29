package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class HpCCuriosDataProvider extends CuriosDataProvider {
    public HpCCuriosDataProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super("curios", output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createSlot("gauntlets")
                .size(1)
                .order(100);

        this.createSlot("oxygen_mask")
                .size(1)
                .order(200);

        this.createSlot("oxygen_connectors")
                .size(1)
                .order(201);

        this.createSlot("oxygen_tank_1")
                .size(1)
                .order(202);

        this.createSlot("oxygen_tank_2")
                .size(1)
                .order(203);

        this.createSlot("head_gear")
                .size(1)
                .order(301);

        this.createSlot("body_gear")
                .size(1)
                .order(302);

        this.createSlot("legs_gear")
                .size(1)
                .order(303);

        this.createSlot("hands_and_feet_gear")
                .size(1)
                .order(304);
        
        
        this.createEntities("player")
                .addPlayer()
                .addSlots("gauntlets", "oxygen_mask", "oxygen_connectors", 
                          "oxygen_tank_1", "oxygen_tank_2", "head_gear", 
                          "body_gear", "legs_gear", "hands_and_feet_gear");
        
        
    }
    
}
