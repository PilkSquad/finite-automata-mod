package net.joe.tutorialmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class StateBlock extends Block {

    public StateBlock(Properties pProperties) {
        super(pProperties);
    }

    public static final IntegerProperty MODEL = IntegerProperty.create("model", 0, 3);
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MODEL);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            // Toggle the model
            int currentIndex = pState.getValue(MODEL);
//          int nextModel = (currentModel + 1) % 4; // Cycle through 0, 1, 2, 3
            int newIndex;

            // Toggle between 0 and 2
            if (currentIndex == 0) {
                newIndex = 2;
            } else {
                newIndex = 0;
            }

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
        if (!pState.is(pNewState.getBlock())) {
            // Check if the block was not moved by a piston and the new state is not of the same block type
            if (!pMovedByPiston && pLevel instanceof ServerLevel) {
                ItemStack itemStack = new ItemStack(this);
                double x = pPos.getX() + 0.5;
                double y = pPos.getY() + 0.5;
                double z = pPos.getZ() + 0.5;

                // Spawn the item in the world
                ItemEntity itemEntity = new ItemEntity((ServerLevel)pLevel, x, y, z, itemStack);
                pLevel.addFreshEntity(itemEntity);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
