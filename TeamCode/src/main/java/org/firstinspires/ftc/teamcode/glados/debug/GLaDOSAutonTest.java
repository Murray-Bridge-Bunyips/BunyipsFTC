package org.firstinspires.ftc.teamcode.glados.debug;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.WaitTask;

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
        return new WaitTask(this, 5.0);
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
    }
}
