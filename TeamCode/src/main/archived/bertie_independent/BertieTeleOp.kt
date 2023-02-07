package org.firstinspires.ftc.archived.bertie_independent

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@TeleOp(name = "<BERTIE-I> Original TeleOp")
@Disabled
class BertieTeleOp : LinearOpMode() {
    // Primary thread that is ran from the Driver Station.
    override fun runOpMode() {
        // Declare variables
        var armPosition: Double
        var frontRightPower: Double
        var backRightPower: Double
        var backLeftPower: Double
        var frontLeftPower: Double

        // Map hardware
        val armMotor = hardwareMap.get(DcMotorEx::class.java, "Arm Motor")
        val frontRight = hardwareMap.get(DcMotorEx::class.java, "Front Right")
        val backRight = hardwareMap.get(DcMotorEx::class.java, "Back Right")
        val spinIntake = hardwareMap.get(CRServo::class.java, "Spin Intake")
        val frontLeft = hardwareMap.get(DcMotorEx::class.java, "Front Left")
        val backLeft = hardwareMap.get(DcMotorEx::class.java, "Back Left")
        val carouselRight = hardwareMap.get(CRServo::class.java, "Carousel Right")
        val carouselLeft = hardwareMap.get(CRServo::class.java, "Carousel Left")

        // Coded by Lucas Bubner
        armPosition = armMotor.currentPosition.toDouble()
        frontRight.direction = DcMotorSimple.Direction.REVERSE
        backRight.direction = DcMotorSimple.Direction.REVERSE
        spinIntake.direction = DcMotorSimple.Direction.REVERSE
        armMotor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        armMotor.mode = RunMode.STOP_AND_RESET_ENCODER
        waitForStart()
        while (opModeIsActive()) {
            // Standardised movements, and accelerated movements if the driver holds down the A button while moving
            spinIntake.power = gamepad2.left_stick_y.toDouble()
            frontLeftPower = Range.clip(
                (gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x).toDouble(),
                -1.0,
                1.0
            )
            backLeftPower = Range.clip(
                (gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x).toDouble(),
                -1.0,
                1.0
            )
            frontRightPower = Range.clip(
                (gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x).toDouble(),
                -1.0,
                1.0
            )
            backRightPower = Range.clip(
                (gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x).toDouble(),
                -1.0,
                1.0
            )
            if (!gamepad1.a) {
                frontLeftPower /= 2.0
                backLeftPower /= 2.0
                frontRightPower /= 2.0
                backRightPower /= 2.0
            }
            frontLeft.power = frontLeftPower
            backLeft.power = backLeftPower
            frontRight.power = frontRightPower
            backRight.power = backRightPower

            // Carousel movements
            if (gamepad2.a || gamepad2.b) {
                carouselRight.power = (if (gamepad2.a) 1 else -1).toDouble()
                carouselLeft.power = (if (gamepad2.a) -1 else 1).toDouble()
            } else {
                carouselRight.power = 0.0
                carouselLeft.power = 0.0
            }
            // Arm movements
            armPosition = (armPosition + -gamepad2.right_stick_y * 14).toInt().toDouble()
            armMotor.targetPosition =
                if (armPosition.toInt() < 1850) armPosition.toInt() else armMotor.targetPosition
            telemetry.addData("ArmPosition", armPosition)
            telemetry.addData("TargetPosition", armMotor.targetPosition)
            telemetry.addData("CurrentPosition", armMotor.currentPosition)
            telemetry.update()
            armMotor.mode = RunMode.RUN_TO_POSITION
            if (armMotor.targetPosition > armMotor.currentPosition + 5 || armMotor.targetPosition < armMotor.currentPosition - 5) {
                armMotor.power =
                    if (armMotor.targetPosition > armMotor.currentPosition) 0.25 else -0.25
            } else {
                armMotor.power = 0.0
            }
        }
    }
}