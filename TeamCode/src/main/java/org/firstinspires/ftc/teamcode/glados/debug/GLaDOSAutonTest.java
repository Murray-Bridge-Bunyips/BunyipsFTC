package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;

import java.util.Arrays;
import java.util.List;

@Autonomous(name = "GLADOS: AutonTest", group = "GLADOS")
public class GLaDOSAutonTest extends AutonomousBunyipsOpMode {
    @Override
    protected void onInitialisation() {

    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return Arrays.asList(new OpModeSelection("test"), new OpModeSelection("test2"));
    }

    @Override
    protected AutoTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(OpModeSelection selectedOpMode) {
        log(selectedOpMode.getName());
    }
}
