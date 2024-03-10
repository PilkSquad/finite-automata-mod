package net.joe.tutorialmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

import javax.swing.plaf.nimbus.State;
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

    public static final IntegerProperty MODEL = IntegerProperty.create("model", 0, 3);
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(MODEL);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            pPlayer.sendSystemMessage(Component.literal(
                    String.valueOf(getNumConnectedTransitions())
            ));

            // Toggle the model
            int currentIndex = pState.getValue(MODEL);
            int newIndex;
            // Toggle between 0 and 2
            if (currentIndex == 0) {
                newIndex = 2;
            } else {
                newIndex = 0;
            }

            // display whether the state is accepting or not
            if (currentIndex == 0) {
                pPlayer.sendSystemMessage(Component.nullToEmpty("The state is accepting"));
            } else {
                pPlayer.sendSystemMessage(Component.nullToEmpty("The state is non-accepting"));
            }

            BlockState newState = pState.setValue(MODEL, newIndex);
            pLevel.setBlock(pPos, newState, 3); // Update the block state

            // Optionally, you can play a sound effect here for feedback


            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos,
                         BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        if (!pLevel.isClientSide) {

            if (!(pNewState.getBlock() instanceof StateBlock)) {
                int transBlocksFreed = 0;

                for (TransitionBlock tb : ownedTransBlocks) {
                    tb.removeOwner();
                    System.out.println("freed transition block");
                }

                ownedTransBlocks.clear();
                ownedTransBlocksInt = 0;
            }
        }
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
        }
    }

    private boolean isUnownedTransitionBlock(Block currentBlock) {
        if (currentBlock instanceof TransitionBlock) {
            TransitionBlock tb = (TransitionBlock) currentBlock;
            return !tb.getIsOwned();
        } else return false;
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
