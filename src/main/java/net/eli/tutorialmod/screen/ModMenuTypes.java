package net.eli.tutorialmod.screen;

import net.eli.tutorialmod.TutorialMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<MenuType<GemPolishingStationMenu>> GEM_POLISHING_MENU =
            registerMenuType("gem_polishing_menu", GemPolishingStationMenu::new);

    public static final RegistryObject<MenuType<NewEntityMenu>> NEW_ENTITY_MENU =
            registerMenuType("new_entity_menu", NewEntityMenu::new);


    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
