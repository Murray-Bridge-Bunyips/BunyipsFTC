package org.murraybridgebunyips.vance.auto;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTiles;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingConfiguration;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.tasks.MessageTask;
import org.murraybridgebunyips.vance.Vance;

/**
 * i forgor the auto goal so this is just testing
 * will change name uhhhhhh sometime
 *
 * @author Lachlan Paul, 2024
 */
@Autonomous
public class VanceAuto extends AutonomousBunyipsOpMode implements RoadRunner {
    private final Vance robot = new Vance();
    private RoadRunnerDrive drive;
    private MessageTask waitMessage;

    @Override
    protected void onInitialise() {
        robot.init();
        drive = new MecanumDrive(robot.driveConstants, robot.mecanumCoefficients,
                robot.imu, robot.fl, robot.fr, robot.bl, robot.br);

        setOpModes(
                StartingConfiguration.redLeft().tile(2),
                StartingConfiguration.redRight().tile(2),
                StartingConfiguration.blueLeft().tile(2),
                StartingConfiguration.blueRight().tile(2)
        );

        waitMessage = new MessageTask(Seconds.of(10), "<style=\"color:red;\">DO NOT PANIC because the robot isn't moving, it is waiting for others to move</>");
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position startingPosition = (StartingConfiguration.Position) selectedOpMode.require();

        setPose(startingPosition.toFieldPose());

        // We need to wait for other robots when we're far away from the basket, so we use a message task to delay our cross-country road trip
        if (startingPosition.isRight()) {
            addTask(waitMessage);
        }

        // Vance is a goofy mf(marquee function) so we need to manually set it's pose.
        // Just steal it from rrpathgen and it will be fine.
        // TODO: Add code for putting things in the baskets, and then parking.
        //  The blue pathing will most likely be almost identical so we won't worry about that till the whole thing is written.
        //  Might not be able to test fully until after room renos and the new field arrives.
        if (startingPosition.isRed()) {
            if (startingPosition.isLeft()) {
                setPose(new Pose2d(-1.46, -2.71, 90.00), FieldTiles, Degrees);
            } else { // always right
                setPose(new Pose2d(1.56, -2.70, 90.00), FieldTiles, Degrees);
            }

            makeTrajectory()
                    .splineTo(new Vector2d(1.47, -2.14), FieldTiles, 99.06, Degrees)
                    .splineTo(new Vector2d(-0.95, -1.56), FieldTiles, 168.05, Degrees)
                    // todo: arm
                    .splineTo(new Vector2d(-2.45, -2.46), FieldTiles, 230.00, Degrees)
                    .addTask();
        } else {  // always blue
            if (startingPosition.isLeft()) {
                setPose(new Pose2d(1.53, 2.66, 270.00), FieldTiles, Degrees);
            } else { // this is always right, isn't that right, Mr Wright?
                setPose(new Pose2d(-1.50, 2.60, 270.00), FieldTiles, Degrees);
            }

            makeTrajectory()
                    .splineTo(new Vector2d(2.42, 2.39), FieldTiles, 50.00, Degrees)
                    .splineTo(new Vector2d(1.78, 1.63), FieldTiles, 229.80, Degrees)
                    // todo: arm
                    .splineTo(new Vector2d(1.09, 0.41), FieldTiles, 240.85, Degrees)
                    .addTask();
        }
    }

    @NonNull
    @Override
    public RoadRunnerDrive getDrive() {
        return drive;
    }
}
