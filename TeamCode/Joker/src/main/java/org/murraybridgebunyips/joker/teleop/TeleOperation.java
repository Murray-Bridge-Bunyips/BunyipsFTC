package org.murraybridgebunyips.joker.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.joker.Joker;

@TeleOp(name = "TeleOp")
public class TeleOperation extends BunyipsOpMode {
    private final Joker robot = new Joker();
    private CartesianMecanumDrive drive;
    private HoldableActuator intake;
    private HoldableActuator lift;

    @Override
    protected void onInit() {
        robot.init();
        intake = new HoldableActuator(robot.intakeMotor)
                .withBottomSwitch(robot.intakeInStop)
                .withTopSwitch(robot.intakeOutStop)
                .withPowerClamps(-0.3, 0.3);
        drive = new CartesianMecanumDrive(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight);
        lift = new HoldableActuator(robot.liftMotor)
                .withBottomSwitch(robot.liftBotStop);
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
        drive.setSpeedUsingController(leftStickX, leftStickY, rightStickX);
        intake.setPower(gp2LeftStickY);
        lift.setPower(gp2RightStickY);
        drive.update();
        intake.update();
        lift.update();
    }
}