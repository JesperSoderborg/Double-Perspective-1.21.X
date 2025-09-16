package net.laisvall.doubleperspective.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class PerspectiveEditScreen extends Screen {
    private final Screen parent;

    public PerspectiveEditScreen(Screen parent) {
        super(Text.literal("Edit Perspective"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // TODO: text fields for name, coords, maybe preview screenshot
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Done"), b -> this.client.setScreen(parent))
                        .dimensions(this.width / 2 - 50, this.height - 30, 100, 20)
                        .build()
        );
    }
}