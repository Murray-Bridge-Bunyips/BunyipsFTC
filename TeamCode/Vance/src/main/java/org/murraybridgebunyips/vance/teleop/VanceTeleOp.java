package org.murraybridgebunyips.vance.teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.BlinkinLights;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.vance.VanceConfig;

/**
 * TeleOp for Vance
 * @author Lachlan Paul, 2024
 */
@TeleOp
public class VanceTeleOp extends CommandBasedBunyipsOpMode {
    private final VanceConfig config = new VanceConfig();
    private MecanumDrive drive;
//    private BlinkinLights lights;
    @Override
    protected void onInitialise() {
        config.init();
        drive = new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                config.imu, config.fl, config.fr, config.bl, config.br
        );
//        lights = new BlinkinLights(config.lights, RevBlinkinLedDriver.BlinkinPattern.RAINBOW_FOREST_PALETTE);

        gamepad1.set(Controls.AnalogGroup.STICKS, Controller.SQUARE);
    }

    @Override
    protected void assignCommands() {
        drive.setDefaultTask(new HolonomicDriveTask(gamepad1, drive, () -> false));
    }
}
