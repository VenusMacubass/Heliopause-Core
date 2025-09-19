package net.venera.galacticraftcore.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GalacticraftCore.MOD_ID);

    public static final Supplier<CreativeModeTab> GALACTICRAFT_CORE_ITEMS = CREATIVE_MODE_TAB.register("galacticraftcore_items",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.IRIDIUM_INGOT.get()))
                    .title(Component.translatable("creativetab.galacticraftcore_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.IRIDIUM_INGOT.get());
                        output.accept(ModItems.ALUMINIUM_INGOT.get());
                        output.accept(ModItems.STANDARD_WRENCH.get());
                        output.accept(ModItems.COPPER_CANISTER.get());
                        output.accept(ModItems.TIN_CANISTER.get());
                        output.accept(ModItems.DEHYDRATED_APPLE.get());
                        output.accept(ModItems.DEHYDRATED_CARROT.get());
                        output.accept(ModItems.DEHYDRATED_POTATO.get());
                        output.accept(ModItems.DEHYDRATED_MELON.get());
                        output.accept(ModItems.RADIOACTIVE_CORE.get());
                    })
                    .build()
    );

    public static final Supplier<CreativeModeTab> GALACTICRAFT_CORE_BLOCKS = CREATIVE_MODE_TAB.register("galacticraftcore_blocks",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.ALUMINIUM_ORE.get()))
                    .title(Component.translatable("creativetab.galacticraftcore_blocks"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "galacticraftcore_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.ALUMINIUM_ORE.get());
                        output.accept(ModBlocks.ALUMINIUM_BLOCK.get());
                        output.accept(ModBlocks.RADIOACTIVE_BLOCK.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
