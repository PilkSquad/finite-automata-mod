package net.eli.tutorialmod.client.screen;

import net.eli.tutorialmod.screen.NewEntityScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ClientHooks {
    public static void openExampleBlockScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new NewEntityScreen(pos));
    }
}
