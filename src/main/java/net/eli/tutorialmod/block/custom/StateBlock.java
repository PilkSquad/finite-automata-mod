package net.eli.tutorialmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StateBlock extends Block {
    public StateBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {
            // open menu
            // openMenu;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        if (!world.isClientSide) {
//            for (Direction side : Direction.values()) {
//                BlockPos adjacentPos = pos.relative(side);
//                BlockState adjacentBlockState = world.getBlockState(adjacentPos);
//                Block block = adjacentBlockState.getBlock();
//
//                if (block instanceof CountOutputBlock) {
//                    CountOutputBlock cob = (CountOutputBlock) block;
//                    cob.setTetheredInputPos(pos);
//                    tether_count++;
//                }
//            }

//            if (placer instanceof Player) {
//                Player player = (Player) placer;
//                if (tether_count > 0) {
//                    player.sendSystemMessage(Component.literal("Successfully tethered " + String.valueOf(tether_count) + " count output block(s)"));
//                } else {
//                    player.sendSystemMessage(Component.literal("No adjacent output block detected"));
//                }
//            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);

        if (!pLevel.isClientSide) {
            // haha
        }
    }
}