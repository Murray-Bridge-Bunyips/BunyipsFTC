package org.murraybridgebunyips.vance.teleop;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.localizers.ThreeWheelLocalizer;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.tuning.RoadRunnerTuningOpMode;
import org.murraybridgebunyips.vance.Vance;

/**
 * brrrrrrrrrrrrrm, brrrrrrrrrrrrrrm, brrrrrrrrrrrrrrrrrrrm
 *
 * @author Lachlan Paul, 2024
 */
@TeleOp
public class VanceRoadRunnerTuning extends RoadRunnerTuningOpMode {
    private final Vance robot = new Vance();
    private MecanumRoadRunnerDrive drive;

    @NonNull
    @Override
    protected RoadRunnerDrive getBaseRoadRunnerDrive() {
        robot.init(this);
        drive = new MecanumRoadRunnerDrive(robot.driveConstants, robot.mecanumCoefficients, hardwareMap.voltageSensor,
                robot.imu, robot.fl, robot.fr, robot.bl, robot.br);
        drive.setLocalizer(new ThreeWheelLocalizer(robot.localiserCoefficients, robot.dwleft, robot.dwright, robot.dwx).withRelocalizingIMU(robot.imu));
        return drive;
    }
}
