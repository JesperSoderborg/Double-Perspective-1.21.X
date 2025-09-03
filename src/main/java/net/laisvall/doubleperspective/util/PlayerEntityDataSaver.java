package net.laisvall.doubleperspective.util;

import net.minecraft.util.math.Vec3d;

public interface PlayerEntityDataSaver {
    Vec3d getSecondPosition();
    void setSecondPosition(Vec3d pos);

    float getSecondYaw();
    void setSecondYaw(float yaw);

    float getSecondPitch();
    void setSecondPitch(float pitch);

    int getSecondFov();
    void setSecondFov(int fov);
}
