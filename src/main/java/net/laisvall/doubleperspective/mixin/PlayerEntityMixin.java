package net.laisvall.doubleperspective.mixin;

import net.laisvall.doubleperspective.DoublePerspectiveClient;
import net.laisvall.doubleperspective.data.PlayerEntityDataSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityDataSaver {

    @Unique
    private Vec3d secondPosition;
    @Unique
    private float secondYaw;
    @Unique
    private float secondPitch;
    @Unique
    private int secondFov;

    @Override
    public Vec3d getSecondPosition() {
        return this.secondPosition;
    }

    @Override
    public void setSecondPosition(Vec3d pos) {
        this.secondPosition = pos;
    }

    @Override
    public float getSecondYaw() {
        return this.secondYaw;
    }

    @Override
    public void setSecondYaw(float yaw) {
        this.secondYaw = yaw;
    }

    @Override
    public float getSecondPitch() {
        return this.secondPitch;
    }

    @Override
    public void setSecondPitch(float pitch) {
        this.secondPitch = pitch;
    }

    @Override
    public int getSecondFov() {
        return this.secondFov;
    }

    @Override
    public void setSecondFov(int fov) {
        this.secondFov = fov;
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void onTravel(Vec3d movementInput, CallbackInfo ci) {
        if (DoublePerspectiveClient.playerLocked) {
            ci.cancel();
        }
    }
}
