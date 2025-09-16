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
import java.util.Objects;

import static net.minecraft.client.render.VertexFormats.POSITION_COLOR;

public class PerspectiveWaypointRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> renderAll(Objects.requireNonNull(context.matrixStack()), context.consumers(), context.camera()));
    }

    public static void renderAll(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
        matrices.push();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Vec3d camPos = camera.getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);

        drawText("abcdefghijklmnoPqrstuvwxyzåäö!", 0, 100, 0, 0.023f, 0x00FF55, false, matrices, vertexConsumers, camera);

        RenderSystem.disableBlend();
        matrices.pop();
    }

    public static void drawText(String text, int x, int y, int z, float scale, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        matrices.push();

        // Translate to world position
        Vec3d centerPos = Vec3d.ofCenter(new BlockPos(x, y, z));
        matrices.translate(centerPos.x, centerPos.y, centerPos.z);

        // Rotate to face camera
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        // Scale text
        matrices.scale(scale, scale, scale);

        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;
        float offsetX = -textWidth / 2f;
        float padding = 1f;

        matrices.scale(-1,1,1);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        renderBackground(matrix, offsetX - padding, -textHeight - padding, textWidth + 2*padding, textHeight + 2*padding);

        matrices.scale(1,-1,1);
        matrix = matrices.peek().getPositionMatrix();
        textRenderer.draw(
                text,
                offsetX, 0,
                color,
                shadow,
                matrix,
                vertexConsumers,
                TextRenderer.TextLayerType.NORMAL,
                0,
                15728880
        );

        matrices.pop();
    }

    private static void renderBackground(Matrix4f matrix, float x, float y, float width, float height) {
        float zIndex = 0.01f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, POSITION_COLOR);
        int shadowColor = 0x40000000;

        buffer.vertex(matrix, x, y, zIndex).color(shadowColor);
        buffer.vertex(matrix, (x+width), y, zIndex).color(shadowColor);
        buffer.vertex(matrix, (x+width), (y+height), zIndex).color(shadowColor);
        buffer.vertex(matrix, x, (y+height), zIndex).color(shadowColor);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}