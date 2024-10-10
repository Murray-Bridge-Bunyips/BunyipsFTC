package org.murraybridgebunyips.joker.debug;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.BlinkinLights;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.joker.Joker;

@TeleOp(name = "VisionTest")
public class VisionTest extends BunyipsOpMode {
    private final Joker robot = new Joker();
    private CartesianMecanumDrive drive;
    private HoldableActuator intake;
    private HoldableActuator lift;
    private BlinkinLights lights;
    private Vision webcam;

    @Override
    protected void onInit() {
        robot.init();
        intake = new HoldableActuator(robot.intakeMotor)
                .withBottomSwitch(robot.intakeInStop)
                .withTopSwitch(robot.intakeOutStop)
                .withPowerClamps(Joker.INTAKE_ARM_LOWER_POWER_CLAMP, Joker.INTAKE_ARM_UPPER_POWER_CLAMP);
        drive = new CartesianMecanumDrive(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight);
        lift = new HoldableActuator(robot.liftMotor)
                .withBottomSwitch(robot.liftBotStop);
        lights = new BlinkinLights(robot.lights, RevBlinkinLedDriver.BlinkinPattern.RED);
        robot.intakeGrip.setPosition(Joker.INTAKE_GRIP_OPEN_POSITION);
        robot.outtakeGrip.setPosition(Joker.OUTTAKE_GRIP_CLOSED_POSITION);
        robot.outtakeAlign.setPosition(Joker.OUTTAKE_ALIGN_IN_POSITION);
        webcam = new Vision(robot.camera);
        AprilTag at = new AprilTag();
        webcam.init(at);
        webcam.start(at);
        webcam.startPreview();
    }

    @Override
    protected void activeLoop() {

    }
}
