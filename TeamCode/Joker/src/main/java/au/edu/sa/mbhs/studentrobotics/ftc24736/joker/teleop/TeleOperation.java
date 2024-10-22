package au.edu.sa.mbhs.studentrobotics.ftc24736.joker.teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.BlinkinLights;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.ftc24736.joker.Joker;

@TeleOp(name = "TeleOp")
public class TeleOperation extends BunyipsOpMode {
    private final Joker robot = new Joker();
    private SimpleMecanumDrive drive;
    private HoldableActuator intake;
    private HoldableActuator lift;
    private BlinkinLights lights;

    @Override
    protected void onInit() {
        robot.init();
        intake = new HoldableActuator(robot.intakeMotor)
                .withBottomSwitch(robot.intakeInStop)
                .withTopSwitch(robot.intakeOutStop)
                .enableUserSetpointControl(() -> 8)
                .withPowerClamps(Joker.INTAKE_ARM_LOWER_POWER_CLAMP, Joker.INTAKE_ARM_UPPER_POWER_CLAMP);
        drive = new SimpleMecanumDrive(robot.frontLeft, robot.backLeft, robot.backRight, robot.frontRight);
        lift = new HoldableActuator(robot.liftMotor)
              .withBottomSwitch(robot.liftBotStop)
              .withPowerClamps(-1, 1);
        lights = new BlinkinLights(robot.lights, RevBlinkinLedDriver.BlinkinPattern.RED);
        robot.intakeGrip.setPosition(Joker.INTAKE_GRIP_OPEN_POSITION);
        robot.outtakeGrip.setPosition(Joker.OUTTAKE_GRIP_CLOSED_POSITION);
        robot.outtakeAlign.setPosition(Joker.OUTTAKE_ALIGN_IN_POSITION);
    }

    @Override
    protected void activeLoop() {
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;
        double gp2LeftStickY = -gamepad2.left_stick_y;
        double gp2RightStickY = -gamepad2.right_stick_y;
        if (gamepad2.getDebounced(Controls.A)) {
            robot.toggleGrips();
        }
        if (gamepad2.getDebounced(Controls.X)) {
            robot.toggleOuttake();
        }
        if (robot.handoverPoint.isPressed()) {
            lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            lift.withPowerClamps(-0.2, 0.2);
        }
        else {
            lights.resetPattern();
            lift.withPowerClamps(-1, 1);
        }
        drive.setPower(Controls.makeRobotVel(leftStickX, leftStickY, rightStickX));
        intake.setPower(gp2LeftStickY);
        lift.setPower(gp2RightStickY);
        drive.update();
        intake.update();
        lift.update();
        lights.update();
        telemetry.addData("currentPosition", robot.intakeMotor.getCurrentPosition());
        telemetry.addData("targetPosition", robot.intakeMotor.getTargetPosition());
    }
}