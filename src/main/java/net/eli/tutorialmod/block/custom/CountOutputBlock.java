package net.eli.tutorialmod.block.custom;

import net.eli.tutorialmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CountOutputBlock extends Block {
    private BlockPos tetheredInputPos = null;

    public CountOutputBlock(Properties pProperties) {
        super(pProperties);
    }

    public BlockPos getTetheredInputPos() {
        return tetheredInputPos;
    }

    public void setTetheredInputPos(BlockPos pPos) {
        tetheredInputPos = pPos;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        boolean found = false;

        if (!world.isClientSide) {
            for (Direction side : Direction.values()) {
                BlockPos adjacentPos = pos.relative(side);
                BlockState adjacentBlockState = world.getBlockState(adjacentPos);
                Block block = adjacentBlockState.getBlock();

                if (block instanceof CountInputBlock) {
                    setTetheredInputPos(adjacentPos);
                    found = true;
                    break;
                }
            }

            if (placer instanceof Player) {
                Player player = (Player) placer;
                if (found) {
                    player.sendSystemMessage(Component.literal("Successfully tethered"));
                } else {
                    player.sendSystemMessage(Component.literal("No adjacent input block detected"));
                }
            }
        }
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
        super.onBlockStateChange(level, pos, oldState, newState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (tetheredInputPos == null) return InteractionResult.FAIL;

        BlockState blockState = pLevel.getBlockState(tetheredInputPos);
        Block block = blockState.getBlock();

        if ( !(block instanceof CountInputBlock) ) return InteractionResult.CONSUME;

        if (!pLevel.isClientSide) {
            // output current count to chat
            pPlayer.sendSystemMessage(Component.literal(String.valueOf(
                    this.getCountFromInputBlock(pLevel)
            )));
        }

        return InteractionResult.SUCCESS;
    }

    public int getCountFromInputBlock(Level world) {
        if (tetheredInputPos != null) {
            BlockState blockState = world.getBlockState(tetheredInputPos);
            Block block = blockState.getBlock();

            // Check if the block at the position is an instance of your custom block class
            if (block instanceof CountInputBlock) {
                CountInputBlock countInputBlock = (CountInputBlock) block;
                return countInputBlock.getCount();
            }
        }

        return -1;
    }

    public void resetTetheredInput() {
        tetheredInputPos = null;
    }

}