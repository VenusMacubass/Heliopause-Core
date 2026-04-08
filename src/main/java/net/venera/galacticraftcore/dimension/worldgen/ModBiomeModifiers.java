package net.venera.galacticraftcore.dimension.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.galacticraftcore.GalacticraftCore;

public class ModBiomeModifiers {  //In what biome and in what stage shall I place?
    //CF -> PF ->  BM
    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var placedBiomes = context.lookup(Registries.BIOME);
    }
    
    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
    }
}
