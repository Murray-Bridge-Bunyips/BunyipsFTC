package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.data.AprilTagData;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

import java.util.List;

/**
 * Test OpMode for AprilTags
 */
@TeleOp(name = "AprilTags")
@Disabled
public class GLaDOSAprilTagTest extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private Vision vision;
    private AprilTag aprilTag;
    private CartesianMecanumDrive basicDrive;

    @Override
    protected void onInit() {
        config.init();
        vision = new Vision(config.webcam);
        aprilTag = new AprilTag();
        vision.init(aprilTag);
        vision.start(aprilTag);
        vision.startPreview();
        basicDrive = new CartesianMecanumDrive(config.frontLeft, config.frontRight, config.backLeft, config.backRight);
    }

    @Override
    protected void activeLoop() {
        List<AprilTagData> data = aprilTag.getData();
        if (!data.isEmpty()) {
//            addTelemetry(data);
            data.forEach(d -> {
                telemetry.addDashboard(d.getId() + ":hamming", d.getHamming());
                telemetry.addDashboard(d.getId() + ":decisionMargin", d.getDecisionMargin());
                telemetry.addDashboard(d.getId() + ":centerX", d.getCenter().x);
                telemetry.addDashboard(d.getId() + ":centerY", d.getCenter().y);
                if (d.getCorners() != null) {
                    for (int i = 0; i < d.getCorners().size(); i++) {
                        telemetry.addDashboard(d.getId() + ":cornerX" + (i + 1), d.getCorners().get(i).x);
                        telemetry.addDashboard(d.getId() + ":cornerY" + (i + 1), d.getCorners().get(i).y);
                    }
                }
                telemetry.addDashboard(d.getId() + ":label", d.getLabel());
                telemetry.addDashboard(d.getId() + ":tagsize", d.getTagsize());
                if (d.getFieldPosition() != null) {
                    telemetry.addDashboard(d.getId() + ":fieldPositionX", d.getFieldPosition().get(0));
                    telemetry.addDashboard(d.getId() + ":fieldPositionY", d.getFieldPosition().get(1));
                    telemetry.addDashboard(d.getId() + ":fieldPositionZ", d.getFieldPosition().get(2));
                }
                if (d.getFieldOrientation() != null) {
                    telemetry.addDashboard(d.getId() + ":fieldOrientationX", d.getFieldOrientation().x);
                    telemetry.addDashboard(d.getId() + ":fieldOrientationY", d.getFieldOrientation().y);
                    telemetry.addDashboard(d.getId() + ":fieldOrientationZ", d.getFieldOrientation().z);
                    telemetry.addDashboard(d.getId() + ":fieldOrientationW", d.getFieldOrientation().w);
                }
                telemetry.addDashboard(d.getId() + ":distanceUnit", d.getDistanceUnit());
                telemetry.addDashboard(d.getId() + ":x", d.getX());
                telemetry.addDashboard(d.getId() + ":y", d.getY());
                telemetry.addDashboard(d.getId() + ":z", d.getZ());
                telemetry.addDashboard(d.getId() + ":pitch", d.getPitch());
                telemetry.addDashboard(d.getId() + ":roll", d.getRoll());
                telemetry.addDashboard(d.getId() + ":yaw", d.getYaw());
                telemetry.addDashboard(d.getId() + ":range", d.getRange());
                telemetry.addDashboard(d.getId() + ":bearing", d.getBearing());
                telemetry.addDashboard(d.getId() + ":elevation", d.getElevation());
                telemetry.addDashboard(d.getId() + ":frameAcquisitionNanoTime", d.getFrameAcquisitionNanoTime());
                if (d.getRawPose() != null) {
                    telemetry.addDashboard(d.getId() + ":rawPoseX", d.getRawPose().x);
                    telemetry.addDashboard(d.getId() + ":rawPoseY", d.getRawPose().y);
                    telemetry.addDashboard(d.getId() + ":rawPoseZ", d.getRawPose().z);
                    telemetry.addDashboard(d.getId() + ":rawPoseRotationMatrix", d.getRawPose().R);
                }
            });
        } else {
            telemetry.add("No AprilTag data at % ns", System.nanoTime());
        }

        basicDrive
                .setSpeedUsingController(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x)
                .update();

        vision.update();
    }
}
