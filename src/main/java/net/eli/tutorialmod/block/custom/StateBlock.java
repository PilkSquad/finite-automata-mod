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
            TransitionBlock tb = null;
            int unownedTransitionBlocksFound = 0;

            while (!searchStack.isEmpty()) {
                BlockPos currentPos = searchStack.pop();

                if (!visited.contains(currentPos)) {
                    visited.add(currentPos);
                    Block currentBlock = world.getBlockState(currentPos).getBlock();
                    if (isUnownedTransitionBlock(currentBlock)) {
                        // own all found transition blocks
                        unownedTransitionBlocksFound++;
                        tb = (TransitionBlock) currentBlock;
                        addTransitionBlock(tb); // Ensure this method properly associates the block with 'pos'

//                        System.out.println("Found unowned transition block");
                    }

                    for (Direction side : Direction.values()) {
                        BlockPos newPos = currentPos.relative(side);
                        BlockState newPosBlockState = world.getBlockState(newPos);
                        Block newPosBlock = newPosBlockState.getBlock();
//                        System.out.println(String.valueOf(newPosBlock.getDescriptionId()));
                        if (!visited.contains(newPos) && isUnownedTransitionBlock(newPosBlock)) {
                            searchStack.push(newPos);
//                            System.out.println("Found new potential transition block");
                        }
                    }
                }
            }

            if (tb != null) {
                // setting one transition block's owner sets off a chain reaction
                tb.addOwner(pos);

                if (placer instanceof Player) {
                    Player pPlayer = (Player) placer;
                    pPlayer.sendSystemMessage(Component.literal("Tethered " +
                            String.valueOf(unownedTransitionBlocksFound) +
                            " transition block(s)"));
                }
            }
        }
    }

    private boolean isUnownedTransitionBlock(Block block) {
        if (block instanceof TransitionBlock) {
            TransitionBlock tb = (TransitionBlock) block;
            return !tb.getIsOwned();
        } else return false;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);

        if (!pLevel.isClientSide) {
            for (TransitionBlock tb : this.ownedTransBlocks) {
                tb.removeOwner();
//                System.out.println("freed transition block");
            }

            this.ownedTransBlocks.clear();
            this.ownedTransBlocksInt = 0;
        }
    }

    public int getNumConnectedTransitions() {
        return this.ownedTransBlocksInt;
    }

    public void addTransitionBlock(TransitionBlock tb) {
        this.ownedTransBlocks.add(tb);
        this.ownedTransBlocksInt++;
    }

    public void removeTransitionBlock(TransitionBlock tb) {
        this.ownedTransBlocks.remove(tb);
        this.ownedTransBlocksInt--;
    }
}