package org.murraybridgebunyips.glados.autonomous.l5;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTile;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.ANGLED_INITIAL_FORWARD_DIST_FT;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_FORWARD_DIST_CM;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_FORWARD_INITIAL_FORWARD_DIST_FT;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_LEFT_TURN_DEG;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_RIGHT_TURN_DEG;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.Direction;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.tasks.RoadRunnerTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;
import org.murraybridgebunyips.bunyipslib.tasks.groups.ParallelTaskGroup;
import org.murraybridgebunyips.glados.autonomous.l3.GLaDOSBackdropPlacerATLeftPark;

import java.util.Objects;

/**
 * Composite OpMode of l4, l3, and l1 OpModes.
 *
 * @author Lucas Bubner, 2024
 */
@Autonomous(name = "Ultimate Preload (Purple on Left, Placing Yellow, Left Park)", group = "L5")
public class GLaDOSUltimatePreloadLeftPark extends GLaDOSBackdropPlacerATLeftPark {
    @Override
    protected void onStart() {
        super.onStart();

        RoadRunnerTask taskOneDrive = makeTrajectory(startingPosition.getPose())
                .forward(spikeMark == Direction.FORWARD ? M_FORWARD_INITIAL_FORWARD_DIST_FT : ANGLED_INITIAL_FORWARD_DIST_FT, FieldTile)
                .withName("Move Forward to Spike Marks")
                .buildTask();

        Task taskOne = new ParallelTaskGroup(
                arm.deltaTask(ARM_DELTA).withName("Extend Arm"),
                taskOneDrive
        ).withName("Move to Spike Marks");

        Task taskTwo = null;
        switch (spikeMark) {
            case FORWARD:
                taskTwo = makeTrajectory(taskOneDrive.getEndPose())
                        .forward(M_FORWARD_DIST_CM, Centimeters)
                        .withName("Align to Center Mark")
                        .buildTask();
                break;
            case LEFT:
                taskTwo = makeTrajectory(taskOneDrive.getEndPose())
                        .turn(M_LEFT_TURN_DEG, Degrees)
                        .withName("Rotate to Left Mark")
                        .buildTask();
                break;
            case RIGHT:
                taskTwo = makeTrajectory(taskOneDrive.getEndPose())
                        .turn(M_RIGHT_TURN_DEG, Degrees)
                        .withName("Rotate to Right Mark")
                        .buildTask();
                break;
        }

        Task taskThree = claws.openTask(DualServos.ServoSide.LEFT).withName("Open Left Claw");
        Task taskFour = arm.deltaTask(-ARM_DELTA).withName("Retract Arm");
        // TODO: correction?
//        Task taskFive = makeTrajectory()
//
//                .buildTask();

        // Add backwards to queue
//        addTaskFirst(taskFive);
        addTaskFirst(taskFour);
        addTaskFirst(taskThree);
        addTaskFirst(Objects.requireNonNull(taskTwo));
        addTaskFirst(taskOne);
    }
}
