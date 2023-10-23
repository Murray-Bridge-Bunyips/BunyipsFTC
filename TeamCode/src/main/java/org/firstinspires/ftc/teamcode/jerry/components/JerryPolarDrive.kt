package org.firstinspires.ftc.teamcode.jerry.components

import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.FieldCentricMecanumDrive
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.RelativeVector

class JerryPolarDrive(
    opMode: BunyipsOpMode,
    bl: DcMotorEx, br: DcMotorEx,
    fl: DcMotorEx, fr: DcMotorEx,
    imu: IMUOp, startingDirection: RelativeVector
) : FieldCentricMecanumDrive(opMode, fl, bl, fr, br, imu, true, startingDirection)