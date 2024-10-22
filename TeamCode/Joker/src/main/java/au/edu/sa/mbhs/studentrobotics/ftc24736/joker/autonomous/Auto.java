package au.edu.sa.mbhs.studentrobotics.ftc24736.joker.autonomous;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;

import androidx.annotation.Nullable;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Reference;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.MecanumLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.purepursuit.PurePursuit;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.BlinkinLights;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.ftc24736.joker.Joker;
import au.edu.sa.mbhs.studentrobotics.ftc24736.joker.components.GoToHandoverPoint;

@Autonomous(name = "Autonomous")
public class Auto extends AutonomousBunyipsOpMode {
    private final Joker robot = new Joker();
    private SimpleMecanumDrive drive;
    private PurePursuit pp;
    private HoldableActuator intake;
    private HoldableActuator lift;
    private BlinkinLights lights;

    @Override
    protected void onInitialise() {
        robot.init();
        DriveModel dm = new DriveModel.Builder()

                .build();
        drive = new SimpleMecanumDrive(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight)
                .withLocalizer(new MecanumLocalizer(dm, robot.frontLeft, robot.backLeft, robot.backRight, robot.frontRight, robot.imu));
        pp = new PurePursuit(drive);
        intake = new HoldableActuator(robot.intakeMotor)
                .withBottomSwitch(robot.intakeInStop)
                .withTopSwitch(robot.intakeOutStop)
                .enableUserSetpointControl(() -> 8)
                .withPowerClamps(Joker.INTAKE_ARM_LOWER_POWER_CLAMP, Joker.INTAKE_ARM_UPPER_POWER_CLAMP);
        lift = new HoldableActuator(robot.liftMotor)
                .withBottomSwitch(robot.liftBotStop);
        lights = new BlinkinLights(robot.lights, RevBlinkinLedDriver.BlinkinPattern.LAWN_GREEN);
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