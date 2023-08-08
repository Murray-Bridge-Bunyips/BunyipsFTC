package org.firstinspires.ftc.teamcode.jerry.components

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.MecanumDrive
import org.firstinspires.ftc.teamcode.common.RobotVector
import org.firstinspires.ftc.teamcode.common.RelativeVector
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Jerry robot drivetrain operation module.
 */
class JerryDrive(
    opMode: BunyipsOpMode,
    private val bl: DcMotorEx, private val br: DcMotorEx,
    private val fl: DcMotorEx, private val fr: DcMotorEx
) : MecanumDrive(opMode, fl, bl, fr, br)