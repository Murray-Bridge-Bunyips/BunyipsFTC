package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.CompanionCubeColours.BLUE_ELEMENT_B;
import static org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.CompanionCubeColours.BLUE_ELEMENT_G;
import static org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.CompanionCubeColours.BLUE_ELEMENT_R;
import static org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.CompanionCubeColours.RED_ELEMENT_B;
import static org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.CompanionCubeColours.RED_ELEMENT_G;
import static org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.CompanionCubeColours.RED_ELEMENT_R;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.DualClaws;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.PersonalityCoreArm;
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask;
import org.murraybridgebunyips.bunyipslib.tasks.InstantTask;
import org.murraybridgebunyips.bunyipslib.tasks.GetTeamPropTask;
import org.murraybridgebunyips.bunyipslib.vision.processors.TeamProp;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;
import org.murraybridgebunyips.glados.tasks.GLaDOSRunManagementRailTask;

import java.util.List;

/**
 * Autonomous for placing a pixel on the spike mark indicated by the team prop.
 */
@Autonomous(name = "Spike Mark Placer")
public class GLaDOSSpikePlacerAutonomous extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private PersonalityCoreArm arm;
    private GetTeamPropTask initTask;
    private Vision vision;
    private TeamProp processor;
    private StartingPositions startingPosition;

    @Override
    protected void onInitialise() {
        config.init();
        vision = new Vision(config.webcam);
        initTask = new GetTeamPropTask(vision);
        arm = new PersonalityCoreArm(config.pixelMotion, config.pixelAlignment, config.suspenderHook, config.suspenderActuator, config.leftPixel, config.rightPixel);
    }

    @Override
    protected MecanumDrive setDrive() {
        return new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelEncoder, config.perpendicularEncoder);
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return StartingPositions.use();
    }

    @Override
    protected RobotTask setInitTask() {
        return initTask;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        startingPosition = (StartingPositions) selectedOpMode.getObj();

        switch (startingPosition) {
            case STARTING_RED_LEFT:
            case STARTING_RED_RIGHT:
                processor = new TeamProp(RED_ELEMENT_R, RED_ELEMENT_G, RED_ELEMENT_B);
                break;

            case STARTING_BLUE_LEFT:
            case STARTING_BLUE_RIGHT:
                processor = new TeamProp(BLUE_ELEMENT_R, BLUE_ELEMENT_G, BLUE_ELEMENT_B);
                break;
        }
        vision.init(processor);
        vision.flip();
        initTask.setTeamProp(processor);

        addTask(new InstantTask(() -> arm.setClawRotatorDegrees(10).update()));
        addTask(new GLaDOSRunManagementRailTask(1.0, arm.getManagementRail(), 1.0));
        addTask(new InstantTask(() -> arm.openClaw(DualClaws.ServoSide.LEFT).update()));
    }

    @Override
    protected void onStart() {
        if (processor != null)
            addRetainedTelemetry("Spike mark locked: %", initTask.getPosition().toString());

        switch (initTask.getPosition()) {
            case LEFT:

                break;
            case CENTER:
//                addNewTrajectory(new Pose2d(11.40, -62.00, Math.toRadians(180.00)))
//                        .lineToLinearHeading(new Pose2d(16.40, -48.10, Math.toRadians(90.00)))
//                        .lineToLinearHeading(new Pose2d(11.71, -34.52, Math.toRadians(90.00)))
//                        .buildWithPriority();
                addNewTrajectory()
                        .turn(Math.toRadians(-90.0))
                        .buildWithPriority();
                break;
            case RIGHT:
//                addNewTrajectory(new Pose2d(11.40, -62.00, Math.toRadians(180.00)))
//                        .lineToSplineHeading(new Pose2d(24.99, -43.42, Math.toRadians(90.00)))
//                        .buildWithPriority();
                addNewTrajectory()
                        .turn(Math.toRadians(-180.0))
                        .buildWithPriority();
                break;
        }

        addNewTrajectory(new Pose2d(0, 0, Math.toRadians(180.00)))
                .lineTo(new Vector2d(0, Inches.fromCM(-83)))
                .buildWithPriority();
    }
}
