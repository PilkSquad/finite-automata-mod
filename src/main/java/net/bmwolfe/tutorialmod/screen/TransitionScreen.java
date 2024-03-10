package net.bmwolfe.tutorialmod.screen;

import net.bmwolfe.tutorialmod.TutorialMod;
import net.bmwolfe.tutorialmod.block.entity.NewEntityBlockEntity;
import net.bmwolfe.tutorialmod.block.entity.TransitionBlockEntity;
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

public class TransitionScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TutorialMod.MOD_ID, "textures/gui/transition_gui.png"); //change this for our new texture

    private final BlockPos position;

    private TransitionBlockEntity blockEntity;
    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private EditBox editBox;
    private EditBox editBox1;

    private static final Component Title = Component.translatable("gui." + TutorialMod.MOD_ID + "transition_block_screen");
    private static final Component ACCEPTED_INPUT_BOX =
            Component.translatable("gui." + TutorialMod.MOD_ID + "transition_block_screen.edit_box.accepted_input_box");
    private static final Component STATE_ID_BOX =
            Component.translatable("gui." + TutorialMod.MOD_ID + "transition_block_screen.edit_box.state_id_box");

    public TransitionScreen(BlockPos pPos) {
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
        if(be instanceof TransitionBlockEntity blockEntity){
            this.blockEntity = blockEntity;
        } else {
            System.err.printf("BlockEntity at %s is not of type TransitionBlockEntity", this.position);
            return;
        }

        this.editBox = addRenderableWidget(new EditBox(this.font, this.leftPos + 130, this.topPos + 45, 20, 20, Component.literal("Hello")));
        this.editBox1 = addRenderableWidget(new EditBox(this.font, this.leftPos + 130, this.topPos + 95, 20, 20, Component.literal("Hello")));


        editBox.setCanLoseFocus(false);
        editBox.active = true;
        editBox.setFocused(true);

    }

    private void handleExampleButton(Button button){

    }

    private String getEditBoxText(EditBox editBox){
        return editBox.getValue();
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, 200, 180);

        super.render(guiGraphics, mouseX, mouseY, delta);

        guiGraphics.drawString(this.font, "Accepted Character:", this.leftPos + 20, this.topPos + 50, 0, false);
        guiGraphics.drawString(this.font, "Next State ID:", this.leftPos + 20, this.topPos + 100, 0, false);

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}