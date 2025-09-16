package net.laisvall.doubleperspective;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.laisvall.doubleperspective.client.PerspectiveEditScreen;
import net.laisvall.doubleperspective.client.PerspectiveManagerButtonInjector;
import net.laisvall.doubleperspective.client.PerspectiveWaypointRenderer;
import net.laisvall.doubleperspective.data.PerspectiveManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class DoublePerspectiveClient implements ClientModInitializer {

    private static KeyBinding switchPerspectiveKey;
    private static KeyBinding addPerspectiveKey;

    @Override
    public void onInitializeClient() {
        registerKeyBindings();
        registerRenderers();
        registerScreens();
    };

        PerspectiveManagerButtonInjector.init();
        PerspectiveWaypointRenderer.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.options == null) return;

            client.options.hudHidden = PerspectiveManager.getInstance().isPlayerLocked();

            handleSwitchPerspectiveKey(client);
            handleAddPerspectiveKey(client);
        });
    }

    private void registerKeyBindings() {
        KeyBinding switchPerspectiveKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.doubleperspective.switch_perspective",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_TAB,
                "key.categories.doubleperspective"
        ));

        KeyBinding addPerspectiveKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.doubleperspective.add_perspective",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                "key.categories.doubleperspective"
        ));
    }

    private void registerRenderers() {
    }

    private void registerScreens() {
    }

    private void handleSwitchPerspectiveKey(MinecraftClient client) {
        while (switchPerspectiveKey.wasPressed()) {
            PerspectiveManager.getInstance().switchToNextPerspective(client);
        }
    }

    private void handleAddPerspectiveKey(MinecraftClient client) {
        while (addPerspectiveKey.wasPressed()) {
            // Open GUI for adding a new perspective
                client.setScreen(new PerspectiveEditScreen(null));
        }
    }
}