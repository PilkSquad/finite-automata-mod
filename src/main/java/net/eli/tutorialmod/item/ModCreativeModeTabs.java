package net.eli.tutorialmod.item;

import net.eli.tutorialmod.TutorialMod;
import net.eli.tutorialmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TutorialMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("tutorial_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.INPUT_BLOCK.get())).title(Component.translatable("creativetab.tutorial_tab"))
                    .displayItems((pParameters, pOutput) -> {
//                        pOutput.accept(ModItems.SAPPHIRE.get());
//                        pOutput.accept(ModItems.RAW_SAPPHIRE.get());
//
//                        pOutput.accept(Items.DIAMOND);
//
//                        pOutput.accept(ModBlocks.SAPPHIRE_BLOCK.get());
//
//                        pOutput.accept(ModItems.METAL_DETECTOR.get());
//
//                        pOutput.accept(ModBlocks.SOUND_BLOCK.get());
//
//                        pOutput.accept(ModBlocks.GEM_POLISHING_STATION.get());
//
//                        pOutput.accept(ModBlocks.NEW_ENTITY.get());

                        pOutput.accept(ModBlocks.COUNT_INPUT_BLOCK.get());

                        pOutput.accept(ModBlocks.COUNT_OUTPUT_BLOCK.get());

                        pOutput.accept(ModBlocks.INPUT_BLOCK.get());
                        pOutput.accept(ModBlocks.OUTPUT_BLOCK.get());
                        pOutput.accept(ModBlocks.TRANSITION_BLOCK.get());
                        pOutput.accept(ModBlocks.STATE_BLOCK.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}