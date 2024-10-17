package org.murraybridgebunyips.joker.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Millimeters;

import androidx.annotation.Nullable;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.EncoderTicks;
import org.murraybridgebunyips.bunyipslib.PurePursuit;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.localizers.MecanumLocalizer;
import org.murraybridgebunyips.bunyipslib.subsystems.BlinkinLights;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.joker.Joker;
import org.murraybridgebunyips.joker.components.GoToHandoverPoint;

import java.util.Arrays;

@Autonomous(name = "Autonomous")
public class Auto extends AutonomousBunyipsOpMode {
    private final Joker robot = new Joker();
    private CartesianMecanumDrive drive;
    private MecanumLocalizer localizer;
    private PurePursuit pp;
    private HoldableActuator intake;
    private HoldableActuator lift;
    private BlinkinLights lights;

    @Override
    protected void onInitialise() {
        robot.init();
        drive = new CartesianMecanumDrive(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight);
        localizer = new MecanumLocalizer(15.25, (ticks) -> EncoderTicks.toInches(ticks, Inches.convertFrom(37.5, Millimeters), 0.05, 28),
                () -> Arrays.asList(robot.frontLeft.getCurrentPosition(), robot.backLeft.getCurrentPosition(), robot.backRight.getCurrentPosition(), robot.frontRight.getCurrentPosition()));
        pp = new PurePursuit(drive::setPower, localizer::getPoseEstimate);
        intake = new HoldableActuator(robot.intakeMotor)
                .withBottomSwitch(robot.intakeInStop)
                .withTopSwitch(robot.intakeOutStop)
                .enableUserSetpointControl(() -> 8)
                .withPowerClamps(Joker.INTAKE_ARM_LOWER_POWER_CLAMP, Joker.INTAKE_ARM_UPPER_POWER_CLAMP);
        lift = new HoldableActuator(robot.liftMotor)
                .withBottomSwitch(robot.liftBotStop);
        lights = new BlinkinLights(robot.lights, RevBlinkinLedDriver.BlinkinPattern.RED);
        robot.intakeGrip.setPosition(Joker.INTAKE_GRIP_OPEN_POSITION);
        robot.outtakeGrip.setPosition(Joker.OUTTAKE_GRIP_CLOSED_POSITION);
        robot.outtakeAlign.setPosition(Joker.OUTTAKE_ALIGN_IN_POSITION);
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        pp.makePath()
                .forward(10, Centimeters)
                .addTask();

        addTask(new GoToHandoverPoint(lift, robot.handoverPoint));
    }
}