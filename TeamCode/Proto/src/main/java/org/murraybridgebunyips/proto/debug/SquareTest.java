package org.murraybridgebunyips.proto.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTile;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.PurePursuit;
import org.murraybridgebunyips.bunyipslib.Tasks;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.TurnTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;
import org.murraybridgebunyips.bunyipslib.tasks.groups.SequentialTaskGroup;
import org.murraybridgebunyips.proto.Proto;

/**
 * Pure pursuit square test.
 */
public class SquareTest extends BunyipsOpMode {
    private final Proto robot = new Proto();
    private DualDeadwheelMecanumDrive drive;
    private PurePursuit pp;
    private Task forwardAndTurn;

    @Override
    protected void onInit() {
        robot.init();
        drive = new DualDeadwheelMecanumDrive(
                robot.driveConstants, robot.mecanumCoefficients, robot.imu,
                robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight,
                robot.localizerCoefficients, robot.parallelDeadwheel, robot.perpendicularDeadwheel
        ).withName("Drive");
        pp = new PurePursuit(drive).withLookaheadRadius(Inches.of(6));
        forwardAndTurn = new SequentialTaskGroup(
                pp.makePath(new Pose2d()).forward(1, FieldTile).buildTask(),
                new TurnTask(drive, Degrees.of(90)),
                pp.makePath(new Pose2d(24, 0, Math.PI / 2)).forward(1, FieldTile).buildTask(),
                new TurnTask(drive, Degrees.of(180)),
                pp.makePath(new Pose2d(24, 24, Math.PI)).forward(1, FieldTile).buildTask(),
                new TurnTask(drive, Degrees.of(-90)),
                pp.makePath(new Pose2d(0, 24, 3 * Math.PI / 2)).forward(1, FieldTile).buildTask(),
                new TurnTask(drive, Degrees.of(0))
        );
        Tasks.register(forwardAndTurn);
    }

    @Override
    protected void activeLoop() {
        Tasks.runRepeatedly(forwardAndTurn);
        drive.update();
    }
}
