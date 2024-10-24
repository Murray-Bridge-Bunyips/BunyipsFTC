package org.murraybridgebunyips.cellphone.components;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.murraybridgebunyips.bunyipslib.RobotConfig;

/**
 * Cellphone
 */
public class CellphoneConfig extends RobotConfig {
    /**
     * Back camera
     */
    public CameraName cameraB;
    /**
     * Front camera
     */
    public CameraName cameraF;
    @Override
    protected void onRuntime() {
        cameraB = ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.BACK);
        cameraF = ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.FRONT);
    }
}
