package org.firstinspires.ftc.teamcode.lisa_independent

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range

/*
    Deprecated in usage of common class OOP, but code remains for archival purposes
 */
@Disabled
@TeleOp(name = "<LISA-I> TeleOp")
class LisaTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val leftMotor = hardwareMap.get(DcMotor::class.java, "Left Motor")
        val rightMotor = hardwareMap.get(DcMotor::class.java, "Right Motor")
        var turn: Double
        var drive: Double
        var leftPower: Double
        var rightPower: Double
        waitForStart()
        leftMotor.direction = DcMotorSimple.Direction.FORWARD
        rightMotor.direction = DcMotorSimple.Direction.REVERSE
        rightMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        while (opModeIsActive()) {
            // Motor efficiency is halved as they are very strong,
            // unless A is pressed which is overdrive or B is pressed which is smoothdrive
            if (gamepad1.a) {
                turn = Range.clip(gamepad1.left_stick_x, -1f, 1f).toDouble()
                drive = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()
                leftPower = Range.clip(drive + turn, -1.0, 1.0)
                rightPower = Range.clip(drive - turn, -1.0, 1.0)
            } else if (gamepad1.b) {
                turn = Range.clip(gamepad1.left_stick_x.toDouble(), -0.25, 0.25)
                drive = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()
                leftPower = Range.clip(drive + turn, -0.25, 0.25)
                rightPower = Range.clip(drive - turn, -0.25, 0.25)
            } else {
                turn = Range.clip(gamepad1.left_stick_x.toDouble(), -0.5, 0.5)
                drive = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()
                leftPower = Range.clip(drive + turn, -0.5, 0.5)
                rightPower = Range.clip(drive - turn, -0.5, 0.5)
            }
            leftMotor.power = leftPower
            rightMotor.power = rightPower
            telemetry.addData("Left Motor Power", leftMotor.power)
            telemetry.addData("Right Motor Power", rightMotor.power)
            telemetry.update()
        }
    }
}