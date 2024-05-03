package org.firstinspires.ftc.teamcode.wheatley.debug;

import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.BLUE_ELEMENT_B;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.BLUE_ELEMENT_G;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.BLUE_ELEMENT_R;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.RED_ELEMENT_B;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.RED_ELEMENT_G;
import static org.firstinspires.ftc.teamcode.common.vision.TeamPropColours.RED_ELEMENT_R;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.StartingPositions;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.GetTeamPropTask;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

import java.util.List;

@Autonomous(name = "GetTeamPropTask Test")
public class WheatleyTeamPropAutoTest extends AutonomousBunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();
    private GetTeamPropTask initTask;
    private Vision vision;
    private TeamProp processor;

    @Override
    protected void onInitialisation() {
        config.init(this);
        vision = new Vision(this, config.webcam);
        initTask = new GetTeamPropTask(this, vision);
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
    protected void onQueueReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null) {
            return;
        }
        switch ((StartingPositions) selectedOpMode.require()) {
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
    }
}
