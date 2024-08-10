package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.localizers.TwoWheelLocalizer;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.tuning.LocalizationTest;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Tuning wrapper for RoadRunner.
 */
@Autonomous(name = "RoadRunner Tuning")
//@Disabled
public class GLaDOSRoadRunnerTuning extends LocalizationTest {
    @Override
    public void runOpMode() {
        GLaDOSConfigCore ROBOT_CONFIG = new GLaDOSConfigCore();
        ROBOT_CONFIG.init(this);
        drive = new MecanumRoadRunnerDrive(null, ROBOT_CONFIG.driveConstants, ROBOT_CONFIG.mecanumCoefficients, hardwareMap.voltageSensor, ROBOT_CONFIG.imu, ROBOT_CONFIG.frontLeft, ROBOT_CONFIG.frontRight, ROBOT_CONFIG.backLeft, ROBOT_CONFIG.backRight);
        drive.setLocalizer(new TwoWheelLocalizer(ROBOT_CONFIG.localizerCoefficients, ROBOT_CONFIG.parallelDeadwheel, ROBOT_CONFIG.perpendicularDeadwheel, drive));
        super.runOpMode();
    }
}
