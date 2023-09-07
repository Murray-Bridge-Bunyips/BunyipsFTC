package org.firstinspires.ftc.teamcode.jerry.components

import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.MecanumDrive

/**
 * Jerry robot drivetrain operation module.
 */
class JerryDrive(
    opMode: BunyipsOpMode,
    private val bl: DcMotorEx, private val br: DcMotorEx,
    private val fl: DcMotorEx, private val fr: DcMotorEx
) : MecanumDrive(opMode, fl, bl, fr, br)