package net.laisvall.doubleperspective.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class WaypointRenderer {
    public static void init() {
        WorldRenderEvents.LAST.register(context -> {
            renderAll(context.matrixStack(), context.consumers(), context.camera(), context.worldRenderer());
        });
    }

    public static void renderAll(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, WorldRenderer worldRenderer) {

        matrices.push();

        Vec3d camPos = camera.getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);

        drawText("Hello Billboard1!", 0, 100, 0, 0.02f, 0xFFFFFF, true, matrices, vertexConsumers, camera);
        drawText("Hello Billboard2!", 0, 103, 0, 0.005f, 0xFF0000, true, matrices, vertexConsumers, camera);
        drawText("Hello Billboard3!", 0, 106, 0, 0.1f, 0x00FFFF, false, matrices, vertexConsumers, camera);

        matrices.pop();
    }

    public static void drawText(String text, int x, int y, int z, float scale, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        matrices.push();

        // Translate to world position
        Vec3d centerPos = Vec3d.ofCenter(new BlockPos(x, y, z));
        matrices.translate(centerPos.x, centerPos.y, centerPos.z);

        // Rotate to face camera
        Quaternionf camRotation = new Quaternionf()
                .rotateY((float)Math.toRadians(-camera.getYaw()))
                .rotateX((float)Math.toRadians(camera.getPitch()));
        matrices.multiply(camRotation);

        // Scale text
        matrices.scale(-scale, -scale, scale);

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