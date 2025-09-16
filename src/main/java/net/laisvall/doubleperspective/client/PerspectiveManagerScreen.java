package net.laisvall.doubleperspective.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PerspectiveManagerScreen extends Screen {
    private final Screen parent;

    private static final Identifier ICON_TEXTURE = Identifier.of("doubleperspective", "textures/gui/my_icon.png");

    public PerspectiveManagerScreen(Screen parent) {
        super(Text.literal("My Custom Screen"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addDrawableChild(
            ButtonWidget.builder(Text.literal("Back"), button -> {
                this.client.setScreen(parent);
            }).dimensions(this.width / 2 - 100, this.height / 2, 200, 20).build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                40,
                0xFFFFFF);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}