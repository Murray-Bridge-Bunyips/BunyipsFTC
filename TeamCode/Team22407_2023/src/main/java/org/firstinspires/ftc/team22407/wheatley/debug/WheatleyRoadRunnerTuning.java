package org.firstinspires.ftc.team22407.wheatley.debug;

import org.firstinspires.ftc.team22407.wheatley.components.WheatleyConfig;
import org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.tuning.AutomaticFeedforwardTuner;

/**
 * Tuning wrapper for RoadRunner.
 */
public class WheatleyRoadRunnerTuning extends AutomaticFeedforwardTuner {
    @Override
    public void runOpMode() throws InterruptedException {
        WheatleyConfig ROBOT_CONFIG = new WheatleyConfig();
        ROBOT_CONFIG.init(this);
        MecanumRoadRunnerDrive drive = new MecanumRoadRunnerDrive(ROBOT_CONFIG.driveConstants, ROBOT_CONFIG.mecanumCoefficients, hardwareMap.voltageSensor, ROBOT_CONFIG.imu, ROBOT_CONFIG.fl, ROBOT_CONFIG.fr, ROBOT_CONFIG.bl, ROBOT_CONFIG.br);
        super.runOpMode();
    }
}
