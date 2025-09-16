package net.laisvall.doubleperspective.client;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PerspectiveManagerButtonInjector {
    public static void init() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GameMenuScreen) {
                Identifier iconTexture = Identifier.of("mymod", "textures/gui/icon.png");

                TextIconButtonWidget iconButton = TextIconButtonWidget.builder(Text.literal(""),
                                button -> client.setScreen(new PerspectiveManagerScreen(screen)),
                                true)
                        .dimension(20, 20)
                        .texture(iconTexture, 16, 16)
                        .build();

                ClickableWidget continueButton = Screens.getButtons(screen).stream()
                        .filter(b -> b.getMessage().getString().equals("Back to Game"))
                        .findFirst()
                        .orElse(null);

                if (continueButton != null) {
                    int x = continueButton.getX() + continueButton.getWidth() + 5;
                    int y = continueButton.getY() + (continueButton.getHeight() / 2) - 10;
                    iconButton.setPosition(x, y);
                }

                Screens.getButtons(screen).add(iconButton);
            }
        });
    }
}