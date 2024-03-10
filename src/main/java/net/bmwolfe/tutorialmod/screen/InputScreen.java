package net.bmwolfe.tutorialmod.screen;

import net.bmwolfe.tutorialmod.TutorialMod;
import net.bmwolfe.tutorialmod.block.entity.InputBlockEntity;
import net.bmwolfe.tutorialmod.block.entity.NewEntityBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InputScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TutorialMod.MOD_ID, "textures/gui/input_gui.png"); //change this for our new texture

    private final BlockPos position;

    private InputBlockEntity blockEntity;
    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private Button button;

    private EditBox editBox;

    private EditBox editBox1;

    private static final Component Title = Component.translatable("gui." + TutorialMod.MOD_ID + "input_block_screen");
    private static final Component COMPUTE_BUTTON =
            Component.translatable("gui." + TutorialMod.MOD_ID + "input_block_screen.button.compute_button");
    private static final Component INPUT_EDIT_BOX =
            Component.translatable("gui." + TutorialMod.MOD_ID + "input_block_screen.edit_box.input_edit_box");
    private static final Component INITIAL_STATE_FIELD =
            Component.translatable("gui." + TutorialMod.MOD_ID + "input_block_screen.edit_box.initial_state_field");

    public InputScreen(BlockPos pPos) {
        super(Title);

        this.position = pPos;
        this.imageHeight=166;
        this.imageWidth=176;
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
        if(be instanceof InputBlockEntity blockEntity){
            this.blockEntity = blockEntity;
        } else {
            System.err.printf("BlockEntity at %s is not of type InputBlockEntity", this.position);
            return;
        }

        this.button = addRenderableWidget(
                Button.builder(
                        COMPUTE_BUTTON,
                        this::handleComputeButton)
                        .bounds(this.leftPos + 75, this.topPos + 135, 50, 20)
                        .tooltip(Tooltip.create(COMPUTE_BUTTON))
                        .build());

        this.editBox = addRenderableWidget(new EditBox(this.font, this.leftPos + getImageWidth()/2-62, this.topPos + 90, 150, 30, Component.literal("Hello")));
        this.editBox1 = addRenderableWidget(new EditBox(this.font, this.leftPos + 150, this.topPos + 10, 20, 20, Component.literal("Hello")));


        editBox.setCanLoseFocus(false);
        editBox.active = true;
        editBox.setFocused(true);

    }

    private void handleComputeButton(Button button){

    }

    private String getEditBoxText(EditBox editBox){
        return editBox.getValue();
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, 200, 180);

        super.render(guiGraphics, mouseX, mouseY, delta);

        guiGraphics.drawString(this.font, "Input your string to test below", this.leftPos + getImageWidth()/2-65, this.topPos + 70, 0, false);
        guiGraphics.drawString(this.font, "Choose initial state:", this.leftPos + 40, this.topPos + 15, 0, false);


    }

    public int getImageWidth() {
        return imageWidth;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}