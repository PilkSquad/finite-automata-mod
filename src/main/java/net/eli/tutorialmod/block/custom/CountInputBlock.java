package net.eli.tutorialmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CountInputBlock extends Block {
    private int cib_count;
    private static CountInputBlock instance = null;

    private CountInputBlock(Properties pProperties) {
        super(pProperties);

        cib_count = 0;
    }

    public static synchronized CountInputBlock getInstance(Properties pProperties) {
        if (instance == null) {
            instance = new CountInputBlock(pProperties);
        }

        return instance;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {
            // increment counter
            this.cib_count++;
            // only pPlayer hears this sound, use "null" to have the sound played so all can hear
            pLevel.playSound(pPlayer, pPos, SoundEvents.NOTE_BLOCK_BANJO.get(), SoundSource.BLOCKS, 1f, 1f);
            // print the new count
            pPlayer.sendSystemMessage(Component.literal(String.valueOf(this.cib_count)));
        }

        return InteractionResult.SUCCESS;
    }

    public int returnCount() {
        return cib_count;
    }
}