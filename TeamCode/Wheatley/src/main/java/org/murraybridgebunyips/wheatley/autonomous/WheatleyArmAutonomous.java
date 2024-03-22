package org.murraybridgebunyips.wheatley.autonomous;

import static org.murraybridgebunyips.common.personalitycore.CompanionCubeColours.BLUE_ELEMENT_B;
import static org.murraybridgebunyips.common.personalitycore.CompanionCubeColours.BLUE_ELEMENT_G;
import static org.murraybridgebunyips.common.personalitycore.CompanionCubeColours.BLUE_ELEMENT_R;
import static org.murraybridgebunyips.common.personalitycore.CompanionCubeColours.RED_ELEMENT_B;
import static org.murraybridgebunyips.common.personalitycore.CompanionCubeColours.RED_ELEMENT_G;
import static org.murraybridgebunyips.common.personalitycore.CompanionCubeColours.RED_ELEMENT_R;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.GetTeamPropTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.TeamProp;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

import java.util.List;

/**
 * Lockland Pull
 */
@Autonomous(name = "Arm Autonomous")
// TODO: unimplemented
@Disabled
public class WheatleyArmAutonomous extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;
    //    private PersonalityCoreArm arm;
    private GetTeamPropTask initTask;
    private Vision vision;
    private TeamProp processor;

    @Override
    protected void onInitialise() {
        config.init();
//        arm = new PersonalityCoreArm(config.pixelMotion, config.pixelAlignment,
//                config.suspenderHook, config.suspenderActuator, config.leftPixel, config.rightPixel
//        );
        vision = new Vision(config.webcam);
        processor = new TeamProp();
        initTask = new GetTeamPropTask(processor);
        vision.init(processor);
        vision.flip();
        vision.start(processor);
    }

    @Override
    protected MecanumDrive setDrive() {
        return new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br
        );
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

        switch ((StartingPositions) selectedOpMode.getObj()) {
            case STARTING_RED_LEFT:
            case STARTING_RED_RIGHT:
                processor.setColours(RED_ELEMENT_R, RED_ELEMENT_G, RED_ELEMENT_B);
                break;
            case STARTING_BLUE_LEFT:
            case STARTING_BLUE_RIGHT:
                processor.setColours(BLUE_ELEMENT_R, BLUE_ELEMENT_G, BLUE_ELEMENT_B);
                break;
        }
    }

    @Override
    protected void onStart() {
        if (processor != null)
            addRetainedTelemetry("Spike mark locked: %", initTask.getPosition().toString());

        switch (initTask.getPosition()) {
            case LEFT:
                addNewTrajectory(new Pose2d(-36.43, -71.81, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-47.21, -45.13), Math.toRadians(90.00))
                        .build();

//                arm.faceClawToGround();
//                arm.toggleClaw(DualServos.ServoSide.LEFT);

                break;
            case RIGHT:
                addNewTrajectory(new Pose2d(-36.57, -71.24, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-32.78, -39.79), Math.toRadians(82.34))
                        .build();

//                arm.faceClawToGround();
//                arm.toggleClaw(DualServos.ServoSide.LEFT);
                break;

            case FORWARD:
                addNewTrajectory(new Pose2d(-36.58, -74.71, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-36.00, -37.35), Math.toRadians(90.29))
                        .build();

//                arm.faceClawToGround();
//                arm.toggleClaw(DualServos.ServoSide.LEFT);
                break;

        }
    }
}