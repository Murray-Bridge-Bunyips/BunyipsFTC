package org.firstinspires.ftc.teamcode.common.roadrunner.drive.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.roadrunner.drive.MecanumRoadRunnerDrive;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;


/*
 * This is a simple routine to test turning capabilities.
 */
@Config
@Autonomous(name = "TurnTest", group = "tuning")
@Disabled
public class TurnTest extends LinearOpMode {
    // Temporarily match this config to your robot's config
    private static final GLaDOSConfigCore ROBOT_CONFIG = new GLaDOSConfigCore();
    public static double ANGLE = 90; // deg

    @Override
    public void runOpMode() throws InterruptedException {
        ROBOT_CONFIG.init(this);
        MecanumRoadRunnerDrive drive = new MecanumRoadRunnerDrive(ROBOT_CONFIG.driveConstants, ROBOT_CONFIG.mecanumCoefficients, hardwareMap.voltageSensor, ROBOT_CONFIG.imu, ROBOT_CONFIG.frontLeft, ROBOT_CONFIG.frontRight, ROBOT_CONFIG.backLeft, ROBOT_CONFIG.backRight);

        waitForStart();

        if (isStopRequested()) return;

        drive.turn(Math.toRadians(ANGLE));
    }
}