package net.eli.tutorialmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class LightningItem extends Item {
    public LightningItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        if (!level.isClientSide()) {
            LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            lightning.setPos(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(lightning);
        }

        return InteractionResult.SUCCESS;
    }

}