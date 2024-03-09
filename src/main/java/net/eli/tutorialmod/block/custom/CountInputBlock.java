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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CountInputBlock extends Block {
    private static int count;

    public CountInputBlock(Properties pProperties) {
        super(pProperties);

        count = 0;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {
            // increment counter
            incrementCount();
            // only pPlayer hears this sound, use "null" to have the sound played so all can hear
            // pLevel.playSound(pPlayer, pPos, SoundEvents.NOTE_BLOCK_BANJO.get(), SoundSource.BLOCKS, 1f, 1f);
        }

        return InteractionResult.SUCCESS;
    }

    public int getCount() {
        return count;
    }

    private void incrementCount() {
        count++;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        int tether_count = 0;

        if (!world.isClientSide) {
            for (Direction side : Direction.values()) {
                BlockPos adjacentPos = pos.relative(side);
                BlockState adjacentBlockState = world.getBlockState(adjacentPos);
                Block block = adjacentBlockState.getBlock();

                if (block instanceof CountOutputBlock) {
                    CountOutputBlock cob = (CountOutputBlock) block;
                    cob.setTetheredInputPos(pos);
                    tether_count++;
                }
            }

            if (placer instanceof Player) {
                Player player = (Player) placer;
                if (tether_count > 0) {
                    player.sendSystemMessage(Component.literal("Successfully tethered " + String.valueOf(tether_count) + " count output block(s)"));
                } else {
                    player.sendSystemMessage(Component.literal("No adjacent output block detected"));
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        resetCount();

        if (!pLevel.isClientSide) {
            for (Direction side : Direction.values()) {
                BlockPos adjacentPos = pPos.relative(side);
                BlockState adjacentBlockState = pLevel.getBlockState(adjacentPos);
                Block block = adjacentBlockState.getBlock();

                if (block instanceof CountOutputBlock) {
                    CountOutputBlock cob = (CountOutputBlock) block;
                    cob.resetTetheredInput();
                    break;
                }
            }
        }
    }

    private void resetCount() {
        count = 0;
    }
}