package com.octagon.airships.client.gui.multiblock;

import com.octagon.airships.block.tileentity.TileEntityEezoCoreMultiblock;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.rpc.IRadiusChanger;
import openmods.gui.SyncedGuiContainer;
import openmods.gui.component.GuiComponentLabel;
import openmods.gui.component.GuiComponentTextButton;
import openmods.gui.component.GuiComponentTextbox;
import openmods.gui.listener.IMouseDownListener;

public class GuiEezoCoreBase extends SyncedGuiContainer<ContainerEezoCoreBase> {
    private int selectedRadius;

    public GuiEezoCoreBase(ContainerEezoCoreBase container) {
        super(container, 176, 80, "masseffectships.gui.eezoCoreBase");

        TileEntityEezoCoreMultiblock multiblock = container.getOwner();

        final IRadiusChanger rpcProxy = multiblock.createRpcProxy();

        final GuiComponentLabel label = new GuiComponentLabel(28, 30, "masseffectships.misc.radius");
        root.addComponent(label);

        final GuiComponentTextbox textBoxRadius = new GuiComponentTextbox(28, 40, 78, 16);
        root.addComponent(textBoxRadius);
        selectedRadius = multiblock.getRadius();
        textBoxRadius.setText(selectedRadius + "");

        final GuiComponentTextButton buttonMinus = new GuiComponentTextButton(10, 40, 16, 16, 0xFFFFFF);
        buttonMinus.setText("-").setListener((IMouseDownListener) (component, x, y, button) -> {
            selectedRadius -= 1;
            textBoxRadius.setText(selectedRadius + "");
        });
        root.addComponent(buttonMinus);

        final GuiComponentTextButton buttonPlus = new GuiComponentTextButton(108, 40, 16, 16, 0xFFFFFF);
        buttonPlus.setText("+").setListener((IMouseDownListener) (component, x, y, button) -> {
            selectedRadius += 1;
            textBoxRadius.setText(selectedRadius + "");
        });
        root.addComponent(buttonPlus);

        GuiComponentTextButton buttonSave = new GuiComponentTextButton(126, 40, 40, 16, 0xFFFFFF);
        buttonSave.setText("Save").setListener((IMouseDownListener) (component, x, y, button) -> {
            if(selectedRadius < 1) selectedRadius = 1;
            if(selectedRadius > Config.Sizes.MAX_CORE_RADIUS) selectedRadius = Config.Sizes.MAX_CORE_RADIUS;
            rpcProxy.changeRadius(selectedRadius);
        });
        root.addComponent(buttonSave);

        textBoxRadius.setListener(value -> {
            try {
                selectedRadius = Integer.parseInt(value, 16);
            } catch (NumberFormatException e) {
                // NO-OP, user derp
            }
        });

        dispatcher().triggerAll();
    }
}
