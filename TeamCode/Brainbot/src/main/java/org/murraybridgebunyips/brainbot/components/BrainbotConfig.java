package org.murraybridgebunyips.brainbot.components;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.murraybridgebunyips.bunyipslib.RobotConfig;

public class BrainbotConfig extends RobotConfig {
    public CameraName cameraB;
    public CameraName cameraF;
    @Override
    protected void configureHardware() {
        cameraB = ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.BACK);
        cameraF = ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.FRONT);
    }
}
