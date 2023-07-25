package org.firstinspires.ftc.teamcode.common;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

/**
 * Latest wrapper to support the v8.2+ SDK's included libraries for Camera operation.
 * Allows TFOD and AprilTag processors to be used in OpModes.
 * Vuforia is not supported, as we don't have any effective uses for it, and this feature is
 * getting phased out by the SDK.
 *
 * @author Lucas Bubner, 2023
 */
// Using Java as opposed to Kotlin as null safety is not a major concern
// due to the initialisation routine of the WebcamName device.
public class Vision extends BunyipsComponent {
    public Vision(@NonNull BunyipsOpMode opMode, WebcamName webcam) {
        super(opMode);
    }

}