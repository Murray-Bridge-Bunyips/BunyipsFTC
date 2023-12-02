package org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;


/*
 * This is a simple routine to test turning capabilities.
 */
@Config
public abstract class TurnTest extends LinearOpMode {
    protected MecanumRoadRunnerDrive drive;


    public static double ANGLE = 90; // deg

    @Override
    public void runOpMode() throws InterruptedException {
        if (drive == null) throw new NullPointerException("drive is null!");

        waitForStart();

        if (isStopRequested()) return;

        drive.turn(Math.toRadians(ANGLE));
    }
}