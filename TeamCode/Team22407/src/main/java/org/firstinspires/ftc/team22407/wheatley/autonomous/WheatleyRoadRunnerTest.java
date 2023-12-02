package org.firstinspires.ftc.team22407.wheatley.autonomous;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.team22407.wheatley.components.WheatleyConfig;
import org.murraybridgebunyips.ftc.bunyipslib.Inches;
import org.murraybridgebunyips.ftc.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.ftc.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.ftc.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.ftc.bunyipslib.RobotConfig;
import org.murraybridgebunyips.ftc.bunyipslib.tasks.AutoTask;

import java.util.List;

/**
 * Test for RoadRunner.
 */
@Autonomous(name = "WHEATLEY: RoadRunner Test", group = "WHEATLEY")
public class WheatleyRoadRunnerTest extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInitialisation() {
        config.init(this);
        RobotConfig.setLastKnownPosition(null);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return null;
    }

    @Override
    protected AutoTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        addNewTrajectory()
                .setTimeout(-1)
                .forward(Inches.fromM(1))
                .turn(Math.toRadians(180))
                .back(Inches.fromM(1))
                .forward(Inches.fromM(2))
                .turn(Math.toRadians(180))
                .build();
    }
}
