package org.firstinspires.ftc.team15215.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.team15215.glados.components.GLaDOSConfigCore;
import org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.localizers.TwoWheelTrackingLocalizer;
import org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.tuning.AutomaticFeedforwardTuner;

/**
 * Tuning wrapper for RoadRunner.
 */
@Autonomous(name = "RoadRunner Tuning")
public class GLaDOSRoadRunnerTuning extends AutomaticFeedforwardTuner {
    @Override
    public void runOpMode() throws InterruptedException {
        GLaDOSConfigCore ROBOT_CONFIG = new GLaDOSConfigCore();
        ROBOT_CONFIG.init(this);
        MecanumRoadRunnerDrive drive = new MecanumRoadRunnerDrive(ROBOT_CONFIG.driveConstants, ROBOT_CONFIG.mecanumCoefficients, hardwareMap.voltageSensor, ROBOT_CONFIG.imu, ROBOT_CONFIG.frontLeft, ROBOT_CONFIG.frontRight, ROBOT_CONFIG.backLeft, ROBOT_CONFIG.backRight);
        drive.setLocalizer(new TwoWheelTrackingLocalizer(ROBOT_CONFIG.localizerCoefficients, ROBOT_CONFIG.parallelEncoder, ROBOT_CONFIG.perpendicularEncoder, drive));
        super.runOpMode();
    }
}
