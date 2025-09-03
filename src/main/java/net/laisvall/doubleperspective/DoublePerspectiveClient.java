package net.laisvall.doubleperspective;

import net.laisvall.doubleperspective.util.PlayerEntityDataSaver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class DoublePerspectiveClient implements ClientModInitializer {

    private static KeyBinding switchKey;
    private static KeyBinding setPositionKey;

    public static boolean positionToggled = false;
    public static boolean secondPositionSaved = false;
    public static boolean playerLocked = false;

    private Vec3d originalPos;
    private float originalYaw;
    private float originalPitch;
    private int originalFov;

    private Vec3d secondPos;
    private float secondYaw;
    private float secondPitch;
    private int secondFov;

    @Override
    public void onInitializeClient() {
        switchKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Switch Perspective",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_TAB,
                "Double Perspective"
        ));
        setPositionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Set/Remove Position",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                "Double Perspective"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.options != null) {
                client.options.hudHidden = playerLocked;

                while (setPositionKey.wasPressed()) {
                    PlayerEntityDataSaver dataSaver = (PlayerEntityDataSaver) client.player;

                    if (!secondPositionSaved) {
                        dataSaver.setSecondPosition(client.player.getPos());
                        dataSaver.setSecondYaw(client.player.getYaw());
                        dataSaver.setSecondPitch(client.player.getPitch());
                        dataSaver.setSecondFov(client.options.getFov().getValue());

                        if (positionToggled) {
                            playerLocked = true;
                        }

                        client.player.sendMessage(Text.of("§aPerspective saved"), true);
                        secondPositionSaved = true;

                    } else {
                        if (positionToggled) {
                            playerLocked = false;
                        }

                        client.player.sendMessage(Text.of("§cPerspective removed"), true);
                        secondPositionSaved = false;
                    }
                }

                while (switchKey.wasPressed()) {
                    PlayerEntityDataSaver dataSaver = (PlayerEntityDataSaver) client.player;
                    client.player.setMovementSpeed(0);

                    if (!positionToggled) {
                        if (secondPositionSaved) {
                            originalPos = client.player.getPos();
                            originalYaw = client.player.getYaw();
                            originalPitch = client.player.getPitch();
                            originalFov = client.options.getFov().getValue();

                            secondPos = dataSaver.getSecondPosition();
                            secondYaw = dataSaver.getSecondYaw();
                            secondPitch = dataSaver.getSecondPitch();
                            secondFov = dataSaver.getSecondFov();

                            client.player.setPos(secondPos.x, secondPos.y, secondPos.z);
                            client.player.setYaw(secondYaw);
                            client.player.setPitch(secondPitch);
                            client.options.getFov().setValue(secondFov);

                            playerLocked = true;

                            client.player.sendMessage(Text.of("§aSwitched to second position"), true);
                            positionToggled = true;

                        } else {
                            client.player.sendMessage(Text.of("§cSave position to enable switching"), true);
                        }

                    } else {
                        client.player.setPos(originalPos.x, originalPos.y, originalPos.z);
                        client.player.setYaw(originalYaw);
                        client.player.setPitch(originalPitch);
                        client.options.getFov().setValue(originalFov);

                        playerLocked = false;

                        client.player.sendMessage(Text.of("§cSwitched to original position"), true);
                        positionToggled = false;
                    }
                }
            }
        });
    }
}

