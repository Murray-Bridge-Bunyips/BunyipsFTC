package org.murraybridgebunyips.brainbot.components;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.murraybridgebunyips.bunyipslib.RobotConfig;

public class BrainbotConfig extends RobotConfig {
    public CameraName camera;
    @Override
    protected void configureHardware() {
        camera = ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.BACK);
    }
}
