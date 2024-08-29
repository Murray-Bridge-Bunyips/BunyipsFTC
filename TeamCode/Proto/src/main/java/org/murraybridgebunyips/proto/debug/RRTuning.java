package org.murraybridgebunyips.proto.debug;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.localizers.TwoWheelLocalizer;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.tuning.RoadRunnerTuning;
import org.murraybridgebunyips.proto.Proto;

/**
 * For RoadRunner tuning.
 */
@TeleOp(name = "RoadRunner Tuning", group = "debug")
public class RRTuning extends RoadRunnerTuning {
    @NonNull
    @Override
    protected RoadRunnerDrive getBaseRoadRunnerDrive() {
        Proto robot = new Proto();
        MecanumRoadRunnerDrive drive = new MecanumRoadRunnerDrive(
                robot.driveConstants, robot.mecanumCoefficients, hardwareMap.voltageSensor,
                robot.imu, robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight
        );
        drive.setLocalizer(new TwoWheelLocalizer(robot.localizerCoefficients, robot.parallelDeadwheel, robot.perpendicularDeadwheel, drive));
        return drive;
    }
}
