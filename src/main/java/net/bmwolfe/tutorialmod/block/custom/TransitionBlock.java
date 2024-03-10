package net.bmwolfe.tutorialmod.block.custom;

import net.bmwolfe.tutorialmod.block.entity.ModBlockEntities;
import net.bmwolfe.tutorialmod.block.entity.NewEntityBlockEntity;
import net.bmwolfe.tutorialmod.block.entity.TransitionBlockEntity;
import net.bmwolfe.tutorialmod.client.screen.ClientHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransitionBlock extends Block implements EntityBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);

    public TransitionBlock(Properties pProperties) {
        super(pProperties);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModBlockEntities.TRANSITION_BE.get().create(pPos, pState);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(pHand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if(!pLevel.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity be = pLevel.getBlockEntity(pPos);
        if(be instanceof TransitionBlockEntity) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openTransitionBlockScreen(pPos));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
    }