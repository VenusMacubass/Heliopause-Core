package net.venera.heliocore.block.hpc_custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.venera.heliocore.block.HpCBlocks;

public class Tier1RocketBlock extends BaseRocketBlock{
    public Tier1RocketBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public Block getTopBlock() {
        return HpCBlocks.T1_ROCKET_TOP.get();
    }
    
    @Override
    public Block getMiddleBlock() {
        return HpCBlocks.T1_ROCKET_MID.get(); 
    }

    @Override
    public Block getBottomBlock() {
        return HpCBlocks.T1_ROCKET_BOT.get();
    }

    @Override
    public int getRocketTier() {
        return 1;
    }
}
