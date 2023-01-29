package org.firstinspires.ftc.teamcode.bertie_independent

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorEx

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@Autonomous(name = "<BERTIE-I> Test Arm Revolutions")
@Disabled
class BertieTestArmRevolutions : LinearOpMode() {
    override fun runOpMode() {
        var armPosition: Double
        val armMotor = hardwareMap.get(DcMotorEx::class.java, "Arm Motor")
        telemetry.addData(
            "Ready",
            "Hardware mapped and variables declared. ENSURE ARM MOTOR IS READY TO MOVE FROM RESTING POSITION. Continue when ready."
        )
        telemetry.update()
        waitForStart()
        armMotor.mode = RunMode.STOP_AND_RESET_ENCODER
        while (opModeIsActive()) {
            armMotor.targetPosition = 1800
            armMotor.mode = RunMode.RUN_TO_POSITION
            armPosition = armMotor.currentPosition.toDouble()
            while (armPosition < 1800) {
                armMotor.power = 0.25
                armPosition = armMotor.currentPosition.toDouble()
                telemetry.addData("armMotor armPosition", armPosition)
                telemetry.update()
            }
            armMotor.mode = RunMode.RUN_TO_POSITION
            armMotor.targetPosition = 100
            armPosition = armMotor.currentPosition.toDouble()
            while (armPosition > 105) {
                armMotor.power = -0.25
                armPosition = armMotor.currentPosition.toDouble()
                telemetry.addData("armMotor armPosition", armPosition)
                telemetry.update()
            }
        }
    }
}