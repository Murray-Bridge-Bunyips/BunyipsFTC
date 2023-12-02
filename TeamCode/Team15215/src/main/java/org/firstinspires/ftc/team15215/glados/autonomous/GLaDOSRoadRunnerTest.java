package org.firstinspires.ftc.team15215.glados.autonomous;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.team15215.glados.components.GLaDOSConfigCore;
import org.murraybridgebunyips.ftc.bunyipslib.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.ftc.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.ftc.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.ftc.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.ftc.bunyipslib.RobotConfig;
import org.murraybridgebunyips.ftc.bunyipslib.tasks.AutoTask;

import java.util.List;

/**
 * Test to learn RoadRunner
 *
 * @author Lachlan Paul, 2023
 */
@Autonomous(name = "GLaDOS: RoadRunner Test", group = "GLADOS")
public class GLaDOSRoadRunnerTest extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInitialisation() {
        config.init(this);
        RobotConfig.setLastKnownPosition(null);
        drive = new DualDeadwheelMecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelEncoder, config.perpendicularEncoder);
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
                .forward(1)
                .build();
    }
}
