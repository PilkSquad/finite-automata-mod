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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.swing.plaf.nimbus.State;

public class TransitionBlock extends Block {
    private BlockPos parentStateBlockPos = null;
    private boolean isOwned;

    public TransitionBlock(Properties pProperties) {
        super(pProperties);

        isOwned = false;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {
            if (isOwned) {
                pPlayer.sendSystemMessage(Component.literal(
                        "Parent state block located at (" +
                                parentStateBlockPos.getX() + ", " +
                                parentStateBlockPos.getY() + ", " +
                                parentStateBlockPos.getZ() + ")"
                ));
            } else {
                pPlayer.sendSystemMessage(Component.literal("No parent state block"));
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        if (!world.isClientSide) {
            boolean playerPlacedThisThang = false;
            Player paul = null;

            if (placer instanceof Player) {
                paul = (Player) placer;
                playerPlacedThisThang = true;
            }

            boolean found = false;

            for (Direction side : Direction.values()) {
                BlockPos adjacentPos = pos.relative(side);
                BlockState adjacentBlockState = world.getBlockState(adjacentPos);
                Block block = adjacentBlockState.getBlock();

                if (block instanceof StateBlock) {
                    parentStateBlockPos = adjacentPos;

                    // increment number of transitions the parent state block has
                    BlockState blockState = world.getBlockState(parentStateBlockPos);
                    StateBlock parentStateBlock = (StateBlock) blockState.getBlock();
                    parentStateBlock.addTransitionBlock(this);

                    isOwned = true;
                    if (playerPlacedThisThang) {
                        paul.sendSystemMessage(Component.literal("Tethered to adjacent state block"));
                    }
                    break;
                } else if (block instanceof TransitionBlock adjTransBlock) {
                    if (adjTransBlock.isOwned()) {
                        BlockPos transitionBlockPos = adjTransBlock.getParentStateBlockPos();
                        parentStateBlockPos = transitionBlockPos;

                        // increment number of transitions the parent state block has
                        BlockState blockState = world.getBlockState(parentStateBlockPos);
                        StateBlock parentStateBlock = (StateBlock) blockState.getBlock();
                        parentStateBlock.addTransitionBlock(this);

                        isOwned = true;
                        if (playerPlacedThisThang) {
                            paul.sendSystemMessage(Component.literal("Tethered to adjacent transition block's parent state"));
                        }
                        break;
                    }
                }
            }
        }
    }

    private BlockPos getParentStateBlockPos() {
        return parentStateBlockPos;
    }

    private boolean isOwned() {
        return isOwned;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos,
                         BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        if (!pLevel.isClientSide) {
            if (this.isOwned) {
                // decrement number of transitions the parent state block has
                BlockState blockState = pLevel.getBlockState(parentStateBlockPos);
                StateBlock parentStateBlock = (StateBlock) blockState.getBlock();
                parentStateBlock.removeTransitionBlock(this);
            }
        }
    }

    public boolean getIsOwned() {
        return isOwned;
    }

    public void addOwner(BlockPos stateBlockPos) {
        isOwned = true;
        parentStateBlockPos = stateBlockPos;
    }

    public void removeOwner() {
        isOwned = false;
        parentStateBlockPos = null;
    }
}
