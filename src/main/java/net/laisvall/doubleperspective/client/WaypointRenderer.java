package net.laisvall.doubleperspective.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class WaypointRenderer {

    /**
     * Registers the render callback. Call this once during client initialization.
     */
    public static void init() {
        WorldRenderEvents.LAST.register(context -> {
            MatrixStack matrices = context.matrixStack();
            VertexConsumerProvider vertexConsumers = context.consumers();

            drawText("Hello Billboard1!", 0, 100, 0, 0.02f, 0xFFFFFF, true, matrices, vertexConsumers);
            drawText("Hello Billboard2!", 0, 103, 0, 0.005f, 0xFF0000, true, matrices, vertexConsumers);
            drawText("Hello Billboard3!", 0, 106, 0, 0.1f, 0x00FFFF, false, matrices, vertexConsumers);
        });
    }

    /**
     * Draws floating text in the world, facing the player.
     *
     * @param textStr The string to render.
     * @param x       World X coordinate
     * @param y       World Y coordinate
     * @param z       World Z coordinate
     * @param scale   Text scale
     * @param color   Text color (0xFFFFFF)
     * @param shadow  Draw shadow
     * @param matrices MatrixStack from the render event
     * @param vertexConsumers VertexConsumerProvider from the render event
     */
    public static void drawText(String textStr, int x, int y, int z, float scale, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();

        matrices.push();

        // Translate to world position relative to camera
        BlockPos blockPos = new BlockPos(x, y, z);
        Vec3d centerPos = Vec3d.ofCenter(blockPos);
        matrices.translate(centerPos.x - camPos.x, centerPos.y - camPos.y, centerPos.z - camPos.z);

        // Rotate to face camera
        // Quaternionf camRotation = camera.getRotation();
        // matrices.multiply(camRotation);

        // Scale text
        matrices.scale(-scale, -scale, scale);

        TextRenderer textRenderer = client.textRenderer;
        Text text = Text.of(textStr);
        int textWidth = textRenderer.getWidth(text);
        float offsetX = -textWidth / 2f;

        textRenderer.draw(
                text,
                offsetX, 0,
                color,
                shadow,
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.NORMAL,
                0,
                15728880
        );

        matrices.pop();
    }
}