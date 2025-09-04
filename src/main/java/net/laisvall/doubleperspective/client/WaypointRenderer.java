package net.laisvall.doubleperspective.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.logging.Logger;

import static net.laisvall.doubleperspective.DoublePerspective.LOGGER;
import static net.minecraft.client.render.VertexFormats.POSITION_COLOR;

public class WaypointRenderer {
    public static void init() {
        WorldRenderEvents.LAST.register(context -> {
            renderAll(context.matrixStack(), context.consumers(), context.camera(), context.worldRenderer());
        });
    }

    public static void renderAll(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, WorldRenderer worldRenderer) {

        matrices.push();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Vec3d camPos = camera.getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);

        drawText("Hello Billboard1!", 0, 100, 0, 0.02f, 0xFFFFFF, true, matrices, vertexConsumers, camera);
        drawText("Hello Billboard2!", 0, 103, 0, 0.5f, 0xFF0000, true, matrices, vertexConsumers, camera);
        drawText("Hello Billboard3!", 0, 106, 0, 0.1f, 0x00FFFF, false, matrices, vertexConsumers, camera);

        renderRectangle(matrices, 0, 90, 0, 4, 1, 0xFF0000FF);
        renderRectangle(matrices, 0, 95, 0, 2, 3, 0xFFFFFFFF);

        matrices.pop();
    }

    public static void drawText(String text, int x, int y, int z, float scale, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        matrices.push();

        // Translate to world position
        Vec3d centerPos = Vec3d.ofCenter(new BlockPos(x, y, z));
        matrices.translate(centerPos.x, centerPos.y, centerPos.z);

        // Rotate to face camera

        //        Quaternionf cameraRotation = new Quaternionf()
        //                .rotateY((float) Math.toRadians(-camera.getYaw()))
        //                .rotateX((float) Math.toRadians(camera.getPitch()));
        //
        //        matrices.multiply(cameraRotation);

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        // Scale text
        matrices.scale(-1,-1,1);
        matrices.scale(scale, scale, scale);

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

    private static void renderRectangle(MatrixStack matrices, double x, double y, double z, float width, float height, int color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, POSITION_COLOR);

        buffer.vertex(matrix, (float)x, (float)y, (float)z).color(color);
        buffer.vertex(matrix, (float)(x+width), (float)y, (float)z).color(color);
        buffer.vertex(matrix, (float)(x+width), (float)(y+height), (float)z).color(color);
        buffer.vertex(matrix, (float)x, (float)(y+height), (float)z).color(color);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private static void renderRomb(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();

        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, POSITION_COLOR);

        buffer.vertex(matrix, 20, 20, 5).color(0xFF0000FF).notify();
        buffer.vertex(matrix, 5, 40, 5).color(0xFF0000FF);
        buffer.vertex(matrix, 35, 40, 5).color(0x00FF00FF);
        buffer.vertex(matrix, 20, 60, 5).color(0xFF00FFFF);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}