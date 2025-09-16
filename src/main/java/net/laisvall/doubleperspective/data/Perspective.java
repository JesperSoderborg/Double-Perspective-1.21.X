package net.laisvall.doubleperspective.data;

import net.minecraft.util.math.Vec3d;

public class Perspective {
    private final String name;
    private final Vec3d position;
    private final float yaw;
    private final float pitch;
    private final int fov;
    private final String screenshotFile; // optional thumbnail filename

    public Perspective(String name, Vec3d position, float yaw, float pitch, int fov, String screenshotFile) {
        this.name = name;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.fov = fov;
        this.screenshotFile = screenshotFile;
    }

    // Getters
    public String getName() { return name; }
    public Vec3d getPosition() { return position; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public int getFov() { return fov; }
    public String getScreenshotFile() { return screenshotFile; }
}