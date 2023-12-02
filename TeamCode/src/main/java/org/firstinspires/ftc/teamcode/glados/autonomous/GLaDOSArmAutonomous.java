package org.firstinspires.ftc.teamcode.glados.autonomous;

import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.BLUE_ELEMENT_B;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.BLUE_ELEMENT_G;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.BLUE_ELEMENT_R;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.RED_ELEMENT_B;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.RED_ELEMENT_G;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.RED_ELEMENT_R;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.Inches;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.RoadRunnerAutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.StartingPositions;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.GetTeamPropTask;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSArmCore;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSServoCore;

import java.util.List;

public class GLaDOSArmAutonomous extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private GLaDOSArmCore arm;
    private GetTeamPropTask initTask;
    private Vision vision;
    private TeamProp processor;

    @Override
    protected void onInitialisation() {
        config.init(this);
        // TODO: Use/make new arm controller (common?)
        arm = new GLaDOSArmCore(this, config.leftPixel, config.rightPixel, config.pixelAlignment, config.suspenderActuator, config.pixelMotion);
        initTask = new GetTeamPropTask(this, vision);
        vision = new Vision(this, config.webcam);
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
            case RED_LEFT:
            case RED_RIGHT:
                processor = new TeamProp(RED_ELEMENT_R, RED_ELEMENT_G, RED_ELEMENT_B);
                break;

            case BLUE_LEFT:
            case BLUE_RIGHT:
                processor = new TeamProp(BLUE_ELEMENT_R, BLUE_ELEMENT_G, BLUE_ELEMENT_B);
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
                addNewTrajectory(new Pose2d(-35.81, -71.43, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-48.13, -45.85), Math.toRadians(90.00))
                        .build();

                // TODO: Set proper values
                arm.getVerticalController().setTargetPosition(90);
                arm.getServoController().toggleServo(GLaDOSServoCore.ServoSide.LEFT);

            case RIGHT:


            case CENTER:
                addNewTrajectory()
                        .forward(Inches.fromCM(120))
                        .build();




        }
    }
}
