package net.eli.tutorialmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eli.tutorialmod.TutorialMod;
import net.eli.tutorialmod.block.entity.NewEntityBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.TextTable;

public class NewEntityScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TutorialMod.MOD_ID, "textures/gui/new_entity_gui.png"); //change this for our new texture

    private final BlockPos position;

    private NewEntityBlockEntity blockEntity;
    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private Button button;

    private EditBox editBox;

    private static final Component Title = Component.translatable("gui." + TutorialMod.MOD_ID + "new_entity_block_screen");
    private static final Component EXAMPLE_BUTTON =
            Component.translatable("gui." + TutorialMod.MOD_ID + "new_entity_block_screen.button.example_button");
    private static final Component EXAMPLE_EDIT_BOX =
            Component.translatable("gui." + TutorialMod.MOD_ID + "new_entity_block_screen.edit_box.example_edit_box");
    private static final Component StringTextComponent =
            Component.translatable("gui." + TutorialMod.MOD_ID + "new_entity_block_screen.string_text_component.string_text_component");

    public NewEntityScreen(BlockPos pPos) {
        super(Title);

        this.position = pPos;
        this.imageHeight=166;
        this.imageWidth=176;
        //this.StringTextComponent = "";
    }


    @Override
    protected void init() {
        super.init();
        //Put in the center
        this.leftPos = (this.width - this.imageWidth)/2;
        this.topPos = (this.height - this.imageHeight)/2;

        if(this.minecraft == null) return;

        Level level = this.minecraft.level;
        if(level == null) return;

        BlockEntity be = level.getBlockEntity(this.position);
        if(be instanceof NewEntityBlockEntity blockEntity){
            this.blockEntity = blockEntity;
        } else {
            System.err.printf("BlockEntity at %s is not of type NewEntityBlockEntity", this.position);
            return;
        }

        this.button = addRenderableWidget(
                Button.builder(
                        EXAMPLE_BUTTON,
                        this::handleExampleButton)
                        .bounds(this.leftPos + 8, this.topPos + 8, 20, 20)
                        .tooltip(Tooltip.create(EXAMPLE_BUTTON))
                        .build()
        );

        this.editBox = addRenderableWidget(
                new EditBox(
                        font,
                        this.leftPos + 10,
                        this.topPos + 10,
                        25,
                        25,
                        EXAMPLE_EDIT_BOX
                       // new StringTextComponent("Initial Text"))
                ));

    }

    private void handleExampleButton(Button button){

    }

//    @Override
//    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, TEXTURE);
//        int x = (width - imageWidth) / 2;
//        int y = (height - imageHeight) / 2;
//
//        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
//
//    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        super.render(guiGraphics, mouseX, mouseY, delta);

        guiGraphics.drawString(this.font, Title, this.leftPos, this.topPos, 0x404040, false);

        guiGraphics.drawString(this.font, "Seconds Existed %d".formatted(this.blockEntity.getCounter()), this.leftPos + 20, this.topPos + 20, 0xFF0000, false);

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}