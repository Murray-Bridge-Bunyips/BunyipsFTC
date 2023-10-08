package org.firstinspires.ftc.teamcode.glados.autonomous;

import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

import java.util.Arrays;
import java.util.List;

/**
 * Drive to the parking zone immediately.
 */
public class GLaDOSParkAuto extends AutonomousBunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInitialisation() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return Arrays.asList(new OpModeSelection("RED_LEFT"), new OpModeSelection("RED_RIGHT"), new OpModeSelection("BLUE_LEFT"), new OpModeSelection("BLUE_RIGHT"));
    }

    @Override
    protected AutoTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {

    }
}
