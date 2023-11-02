package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotorEx

/**
 * Odometer class for tracking motor position, base implementation of the Encoder class.
 * @author Lucas Bubner, 2023
 */
class Odometer(
    opMode: BunyipsOpMode,
    override val motor: DcMotorEx,
    override val wheelDiameterMM: Double?,
    override val ticksPerRevolution: Double?,
) : BunyipsComponent(opMode), Encoder {
    override var snapshot: Double = 0.0
}