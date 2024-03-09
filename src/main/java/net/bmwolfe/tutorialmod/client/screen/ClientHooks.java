package net.bmwolfe.tutorialmod.client.screen;

import net.bmwolfe.tutorialmod.screen.NewEntityScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ClientHooks {
    public static void openExampleBlockScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new NewEntityScreen(pos));
    }
}
