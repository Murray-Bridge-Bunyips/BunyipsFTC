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
@Autonomous(name = "<BERTIE-I> Test All Motor Encoders")
@Disabled
public class BertieTestMotorEncoders extends LinearOpMode {

    @Override
    public void runOpMode() {

        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "Front Right");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "Back Right");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "Front Left");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "Back Left");

        // Set all motors to forward drive, although we wouldn't do this to move forward
        frontRight.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);

        telemetry.addData("TEST READY", "Initialise to begin test");
        telemetry.addData("WARNING"," ALL MOVEMENT MOTORS WILL ENGAGE WHEN STARTED!!");
        telemetry.addData("!!!", " SET ROBOT ON STABLE STAND BEFORE STARTING");
        telemetry.update();

        waitForStart();

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight.setPower(1);
        frontLeft.setPower(1);
        backRight.setPower(1);
        backLeft.setPower(1);

        while (opModeIsActive()) {
            telemetry.addData("frontRight Encoder", frontRight.getCurrentPosition());
            telemetry.addData("frontLeft Encoder", frontLeft.getCurrentPosition());
            telemetry.addData("backRight Encoder", backRight.getCurrentPosition());
            telemetry.addData("backLeft Encoder", backLeft.getCurrentPosition());
            telemetry.update();
        }
    }
}