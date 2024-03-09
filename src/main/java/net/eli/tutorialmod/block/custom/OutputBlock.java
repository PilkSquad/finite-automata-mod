package net.eli.tutorialmod.block.custom;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class OutputBlock extends Block {
    public OutputBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        boolean found = false;

        if (!world.isClientSide) {
//            for (Direction side : Direction.values()) {
//                BlockPos adjacentPos = pos.relative(side);
//                BlockState adjacentBlockState = world.getBlockState(adjacentPos);
//                Block block = adjacentBlockState.getBlock();
//
//                if (block instanceof CountInputBlock) {
//                    setTetheredInputPos(adjacentPos);
//                    found = true;
//                    break;
//                }
//            }
//
//            if (placer instanceof Player player) {
//                if (found) {
//                    player.sendSystemMessage(Component.literal("Successfully tethered"));
//                } else {
//                    player.sendSystemMessage(Component.literal("No adjacent input block detected"));
//                }
//            }
        }
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
        super.onBlockStateChange(level, pos, oldState, newState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        // haha

        return InteractionResult.SUCCESS;
    }
}