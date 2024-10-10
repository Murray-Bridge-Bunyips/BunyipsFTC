package org.murraybridgebunyips.joker.autonomous;

//import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;

import androidx.annotation.Nullable;

//import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
//import org.murraybridgebunyips.bunyipslib.PurePursuit;
import org.murraybridgebunyips.bunyipslib.Reference;
//import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.BlinkinLights;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.joker.Joker;
import org.murraybridgebunyips.joker.components.GoToHandoverPoint;

@Autonomous(name = "Autonomous")
public class Auto extends AutonomousBunyipsOpMode {
    private final Joker robot = new Joker();
//    private CartesianMecanumDrive drive;
//    private MecanumDrive.MecanumLocalizer localizer;
//    private PurePursuit pp;
    private HoldableActuator intake;
    private HoldableActuator lift;
    private BlinkinLights lights;

    @Override
    protected void onInitialise() {
        robot.init();
//        drive = new CartesianMecanumDrive(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight);
//        localizer = new MecanumDrive.MecanumLocalizer();
//        pp = new PurePursuit((p) -> drive.setSpeedXYR(p.getX(), p.getY(), p.getHeading()), localizer::getPoseEstimate);
        intake = new HoldableActuator(robot.intakeMotor)
                .withBottomSwitch(robot.intakeInStop)
                .withTopSwitch(robot.intakeOutStop)
                .withPowerClamps(-0.3, 0.3);
        lift = new HoldableActuator(robot.liftMotor)
                .withBottomSwitch(robot.liftBotStop);
        lights = new BlinkinLights(robot.lights, RevBlinkinLedDriver.BlinkinPattern.RED);
        robot.intakeGrip.setPosition(Joker.INTAKE_GRIP_OPEN_POSITION);
        robot.outtakeGrip.setPosition(Joker.OUTTAKE_GRIP_CLOSED_POSITION);
        robot.outtakeAlign.setPosition(Joker.OUTTAKE_ALIGN_IN_POSITION);
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
//        pp.makePath()
//                .forward(10, Centimeters)
//                .addTask();

        addTask(new GoToHandoverPoint(lift, robot.handoverPoint));
    }
}