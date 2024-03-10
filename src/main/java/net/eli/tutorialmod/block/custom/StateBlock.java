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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class StateBlock extends Block {
    private ArrayList<TransitionBlock> ownedTransBlocks;

    private int ownedTransBlocksInt;

    public StateBlock(Properties pProperties) {
        super(pProperties);

        ownedTransBlocks = new ArrayList<TransitionBlock>();
        ownedTransBlocksInt = 0;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {
            pPlayer.sendSystemMessage(Component.literal(
                    String.valueOf(getNumConnectedTransitions())
            ));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        if (!world.isClientSide) {
            // when we place a state block, there may be an adjacent transition block...
            // and that transition block may not have an owner...
            // get ready to perform 3-dimensional breadth-first-search...
            // because we have groups of contiguous unowned transition blocks to find!

            Set<BlockPos> visited = new HashSet<>();
            Stack<BlockPos> searchStack = new Stack<>();
            searchStack.push(pos);

            while (!stack.isEmpty()) {
                BlockPos currentPos = searchStack.pop();
                if (!visited.contains(currentPos)) {
                    visited.add(currentPos);
                    Block currentBlock = world.getBlockState(currentPos).getBlock();
                    if (isUnownedTransitionBlock(currentBlock)) {
                        // if we find an unowned transition block, set its owner to me
                        // and add the state block to ones I own
                        TransitionBlock tb = (TransitionBlock) currentBlock;
                        tb.addOwner(pos);
                        addTransitionBlock(tb);
                    }

                    for (Direction side : Direction.values()) {
                        BlockPos newPos = currentPos.relative(side);
                        if (!visited.contains(newPos)) {
                            searchStack.push(newPos);
                        }
                    }
                }
            }
        }
    }

    private boolean isUnownedTransitionBlock(Block currentBlock) {
        if (currentBlock instanceof TransitionBlock) {
            TransitionBlock tb = (TransitionBlock) currentBlock;
            return !tb.getIsOwned();
        } else return false;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);

        if (!pLevel.isClientSide) {
            int transBlocksFreed = 0;

            for (TransitionBlock tb : ownedTransBlocks) {
                tb.removeOwner();
                System.out.println("freed transition block");
            }

            ownedTransBlocks.clear();
            ownedTransBlocksInt = 0;
        }
    }

    public int getNumConnectedTransitions() {
        return ownedTransBlocksInt;
    }

    public void addTransitionBlock(TransitionBlock tb) {
        ownedTransBlocks.add(tb);
        ownedTransBlocksInt++;
    }

    public void removeTransitionBlock(TransitionBlock tb) {
        ownedTransBlocks.remove(tb);
        ownedTransBlocksInt--;
    }
}