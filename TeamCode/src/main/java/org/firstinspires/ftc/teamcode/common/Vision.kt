package org.firstinspires.ftc.teamcode.common

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName

/**
 * Latest wrapper to support the v8.2+ SDK's included libraries for Camera operation.
 * Allows TFOD and AprilTag processors to be used in OpModes.
 *
 * Vuforia is not supported, as we don't have any effective uses for it, and this feature is
 * getting phased out by the SDK.
 *
 * @author Lucas Bubner, 2023
 */
class Vision(
    opmode: BunyipsOpMode,
    webcam: WebcamName?,
) : BunyipsComponent(opmode) {
    // TODO
}