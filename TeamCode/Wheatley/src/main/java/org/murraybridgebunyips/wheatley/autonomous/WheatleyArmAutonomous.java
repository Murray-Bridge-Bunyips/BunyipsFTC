package org.murraybridgebunyips.wheatley.autonomous;

import static org.murraybridgebunyips.bunyipslib.vision.TeamPropColours.BLUE_ELEMENT_B;
import static org.murraybridgebunyips.bunyipslib.vision.TeamPropColours.BLUE_ELEMENT_G;
import static org.murraybridgebunyips.bunyipslib.vision.TeamPropColours.BLUE_ELEMENT_R;
import static org.murraybridgebunyips.bunyipslib.vision.TeamPropColours.RED_ELEMENT_B;
import static org.murraybridgebunyips.bunyipslib.vision.TeamPropColours.RED_ELEMENT_G;
import static org.murraybridgebunyips.bunyipslib.vision.TeamPropColours.RED_ELEMENT_R;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.murraybridgebunyips.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.Vision;
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask;
import org.murraybridgebunyips.bunyipslib.tasks.GetTeamPropTask;
import org.murraybridgebunyips.bunyipslib.vision.TeamProp;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;
import org.murraybridgebunyips.wheatley.components.WheatleyLift;

import java.util.List;

public class WheatleyArmAutonomous extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final WheatleyConfig config = new WheatleyConfig();
    private WheatleyLift arm;
    private GetTeamPropTask initTask;
    private Vision vision;
    private TeamProp processor;

    @Override
    protected void onInitialisation() {
        config.init(this);
        // TODO: Use/make new arm controller (common?)
//        arm = new WheatleyLift(this, config.ra, config.ls, config.rs);
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

                arm.setPosition(90);
                arm.toggleLeftClaw();

            case RIGHT:


            case CENTER:


        }
    }
}