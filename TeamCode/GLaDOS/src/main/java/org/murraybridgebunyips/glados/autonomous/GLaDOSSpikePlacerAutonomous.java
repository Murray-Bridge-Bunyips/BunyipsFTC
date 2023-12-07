package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.personalitycore.CompanionCubeColours.BLUE_ELEMENT_B;
import static org.murraybridgebunyips.bunyipslib.personalitycore.CompanionCubeColours.BLUE_ELEMENT_G;
import static org.murraybridgebunyips.bunyipslib.personalitycore.CompanionCubeColours.BLUE_ELEMENT_R;
import static org.murraybridgebunyips.bunyipslib.personalitycore.CompanionCubeColours.RED_ELEMENT_B;
import static org.murraybridgebunyips.bunyipslib.personalitycore.CompanionCubeColours.RED_ELEMENT_G;
import static org.murraybridgebunyips.bunyipslib.personalitycore.CompanionCubeColours.RED_ELEMENT_R;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.DualClaws;
import org.murraybridgebunyips.bunyipslib.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.Vision;
import org.murraybridgebunyips.bunyipslib.personalitycore.PersonalityCoreArm;
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask;
import org.murraybridgebunyips.bunyipslib.tasks.GetTeamPropTask;
import org.murraybridgebunyips.bunyipslib.vision.TeamProp;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

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

    @Override
    protected void onInitialisation() {
        config.init(this);
        vision = new Vision(this, config.webcam);
        initTask = new GetTeamPropTask(this, vision);
        drive = new DualDeadwheelMecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelEncoder, config.perpendicularEncoder);
        arm = new PersonalityCoreArm(this, config.pixelMotion, config.pixelAlignment, config.suspenderHook, config.suspenderActuator, config.leftPixel, config.rightPixel);
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return StartingPositions.use();
    }

    @Override
    protected AutoTask setInitTask() {
        return initTask;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.getObj()) {
            case STARTING_RED_LEFT:
            case STARTING_RED_RIGHT:
                processor = new TeamProp(RED_ELEMENT_R, RED_ELEMENT_G, RED_ELEMENT_B, true);
                break;

            case STARTING_BLUE_LEFT:
            case STARTING_BLUE_RIGHT:
                processor = new TeamProp(BLUE_ELEMENT_R, BLUE_ELEMENT_G, BLUE_ELEMENT_B, true);
                break;
        }
        vision.init(processor);
        initTask.setTeamProp(processor);
    }

    @Override
    protected void onStart() {
        if (processor != null)
            addRetainedTelemetry("Spike mark locked: %", initTask.getPosition().toString());

        switch (initTask.getPosition()) {
            case LEFT:
                addNewTrajectory(new Pose2d(-35.77, -61.22, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-45.61, -34.52), Math.toRadians(110.22))
                        .addDisplacementMarker(() -> arm.openClaw(DualClaws.ServoSide.LEFT).update())
                        .build();
                break;

            case RIGHT:
                addNewTrajectory(new Pose2d(-36.86, -61.38, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-28.11, -39.51), Math.toRadians(68.20))
                        .addDisplacementMarker(() -> arm.openClaw(DualClaws.ServoSide.LEFT).update())
                        .build();
                break;

            case CENTER:
                addNewTrajectory(new Pose2d(10.31, -61.22, Math.toRadians(181.67)))
                        .lineToLinearHeading(new Pose2d(12.18, -39.36, Math.toRadians(87.22)))
                        .build();
                break;
        }
    }
}
