package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.processors.AprilTag;
import org.firstinspires.ftc.teamcode.Joker;

@TeleOp(name = "DriveTest")
public class DriveTest extends BunyipsOpMode {
    private final Joker robot = new Joker();

    @Override
    protected void onInit() {
        robot.init();
        //robot.outtakeAlign.setPosition(Joker.OUTTAKE_ALIGN_IN_POSITION);
        DriveModel dm = new DriveModel.Builder()
                .build();
        AprilTag at = new AprilTag();
//        AprilTagPoseEstimator.enable(at, localizer)
//                .setCameraOffset(new Pose2d(8.5, 0, 0))
//                .setHeadingEstimate(false)
//                .setKalmanGains(4, 0.1);
        robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.LAWN_GREEN);
    }

    @Override
    protected void activeLoop() {
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;
        robot.drive.setPower(Controls.vel(leftStickX, leftStickY, rightStickX));
        robot.drive.update();

        telemetry.addData("pose", robot.drive.getPose());
    }
}