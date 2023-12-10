package org.firstinspires.ftc.teamcode.bertie_independent;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@Autonomous(name = "<BERTIE-I> Test Arm Revolutions")
@Disabled
public class BertieTestArmRevolutions extends LinearOpMode {
    @Override
    public void runOpMode() {
        double armPosition;
        DcMotorEx armMotor = hardwareMap.get(DcMotorEx.class, "Arm Motor");

        telemetry.addData("Ready", "Hardware mapped and variables declared. ENSURE ARM MOTOR IS READY TO MOVE FROM RESTING POSITION. Continue when ready.");
        telemetry.update();

        waitForStart();

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (opModeIsActive()) {
            armMotor.setTargetPosition(1800);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armPosition = armMotor.getCurrentPosition();
            while (armPosition < 1800) {
                armMotor.setPower(0.25);
                armPosition = armMotor.getCurrentPosition();
                telemetry.addData("armMotor armPosition", armPosition);
                telemetry.update();
            }
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setTargetPosition(100);
            armPosition = armMotor.getCurrentPosition();
            while (armPosition > 105) {
                armMotor.setPower(-0.25);
                armPosition = armMotor.getCurrentPosition();
                telemetry.addData("armMotor armPosition", armPosition);
                telemetry.update();
            }
        }
    }
}