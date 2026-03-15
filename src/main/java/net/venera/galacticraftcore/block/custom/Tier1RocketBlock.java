package net.venera.galacticraftcore.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.venera.galacticraftcore.block.ModBlocks;

public class Tier1RocketBlock extends BaseRocketBlock{
    public Tier1RocketBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public Block getTopBlock() {
        return ModBlocks.T1_ROCKET_TOP.get();
    }
    
    @Override
    public Block getMiddleBlock() {
        return ModBlocks.T1_ROCKET_MID.get(); 
    }

    @Override
    public Block getBottomBlock() {
        return ModBlocks.T1_ROCKET_BOT.get();
    }

    @Override
    public int getRocketTier() {
        return 1;
    }
}
