package org.firstinspires.ftc.archived.bertie_independent

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@Autonomous(name = "<BERTIE-I> Test All Motor Encoders")
@Disabled
class BertieTestMotorEncoders : LinearOpMode() {
    override fun runOpMode() {
        val frontRight = hardwareMap.get(DcMotorEx::class.java, "Front Right")
        val backRight = hardwareMap.get(DcMotorEx::class.java, "Back Right")
        val frontLeft = hardwareMap.get(DcMotorEx::class.java, "Front Left")
        val backLeft = hardwareMap.get(DcMotorEx::class.java, "Back Left")

        // Set all motors to forward drive, although we wouldn't do this to move forward
        frontRight.direction = Direction.FORWARD
        backRight.direction = Direction.FORWARD
        frontLeft.direction = Direction.FORWARD
        backLeft.direction = Direction.FORWARD
        telemetry.addData("TEST READY", "Initialise to begin test")
        telemetry.addData("WARNING", " ALL MOVEMENT MOTORS WILL ENGAGE WHEN STARTED?")
        telemetry.addData("?!", " SET ROBOT ON STABLE STAND BEFORE STARTING")
        telemetry.update()
        waitForStart()
        frontRight.mode = RunMode.RUN_USING_ENCODER
        backRight.mode = RunMode.RUN_USING_ENCODER
        frontLeft.mode = RunMode.RUN_USING_ENCODER
        backLeft.mode = RunMode.RUN_USING_ENCODER
        frontRight.power = 1.0
        frontLeft.power = 1.0
        backRight.power = 1.0
        backLeft.power = 1.0
        while (opModeIsActive()) {
            telemetry.addData("frontRight Encoder", frontRight.currentPosition)
            telemetry.addData("frontLeft Encoder", frontLeft.currentPosition)
            telemetry.addData("backRight Encoder", backRight.currentPosition)
            telemetry.addData("backLeft Encoder", backLeft.currentPosition)
            telemetry.update()
        }
    }
}