package net.venera.galacticraftcore.block.custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;

public class AdvancedRefineryBlock extends RefineryBlock{
    public static final MapCodec<AdvancedRefineryBlock> CODEC = simpleCodec(AdvancedRefineryBlock::new);

    public AdvancedRefineryBlock(Properties properties) {
        super(properties);
    }

//    public AdvancedRefineryBlock(Properties properties) {
//        super(properties, ModBlockEntities.ADVANCED_REFINERY_ENTITY);
//    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
