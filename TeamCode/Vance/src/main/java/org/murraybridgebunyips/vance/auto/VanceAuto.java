package org.murraybridgebunyips.vance.auto;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.vance.Vance;

/**
 * i forgor the auto goal so this is just testing
 * will change name uhhhhhh sometime
 *
 * @author Lachlan Paul, 2024
 */
@Autonomous
public class VanceAuto extends AutonomousBunyipsOpMode implements RoadRunner {
    private final Vance robot = new Vance();
    private RoadRunnerDrive drive;
    @Override
    protected void onInitialise() {
        robot.init();
        drive = new MecanumDrive(robot.driveConstants, robot.mecanumCoefficients,
                robot.imu, robot.fl, robot.fr, robot.bl, robot.br);
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        makeTrajectory()
                .forward(10, Centimeters)
                .addTask();
    }

    @NonNull
    @Override
    public RoadRunnerDrive getDrive() {
        return drive;
    }
}
