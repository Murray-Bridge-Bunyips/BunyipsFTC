package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.data.AprilTagData;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

import java.util.List;

@TeleOp(name = "AprilTags")
public class GLaDOSAprilTagTest extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
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
                addDashboardTelemetry(d.getId() + ":hamming", d.getHamming());
                addDashboardTelemetry(d.getId() + ":decisionMargin", d.getDecisionMargin());
                addDashboardTelemetry(d.getId() + ":centerX", d.getCenter().x);
                addDashboardTelemetry(d.getId() + ":centerY", d.getCenter().y);
                if (d.getCorners() != null) {
                    for (int i = 0; i < d.getCorners().size(); i++) {
                        addDashboardTelemetry(d.getId() + ":cornerX" + (i + 1), d.getCorners().get(i).x);
                        addDashboardTelemetry(d.getId() + ":cornerY" + (i + 1), d.getCorners().get(i).y);
                    }
                }
                addDashboardTelemetry(d.getId() + ":label", d.getLabel());
                addDashboardTelemetry(d.getId() + ":tagsize", d.getTagsize());
                if (d.getFieldPosition() != null) {
                    addDashboardTelemetry(d.getId() + ":fieldPositionX", d.getFieldPosition().get(0));
                    addDashboardTelemetry(d.getId() + ":fieldPositionY", d.getFieldPosition().get(1));
                    addDashboardTelemetry(d.getId() + ":fieldPositionZ", d.getFieldPosition().get(2));
                }
                if (d.getFieldOrientation() != null) {
                    addDashboardTelemetry(d.getId() + ":fieldOrientationX", d.getFieldOrientation().x);
                    addDashboardTelemetry(d.getId() + ":fieldOrientationY", d.getFieldOrientation().y);
                    addDashboardTelemetry(d.getId() + ":fieldOrientationZ", d.getFieldOrientation().z);
                    addDashboardTelemetry(d.getId() + ":fieldOrientationW", d.getFieldOrientation().w);
                }
                addDashboardTelemetry(d.getId() + ":distanceUnit", d.getDistanceUnit());
                addDashboardTelemetry(d.getId() + ":x", d.getX());
                addDashboardTelemetry(d.getId() + ":y", d.getY());
                addDashboardTelemetry(d.getId() + ":z", d.getZ());
                addDashboardTelemetry(d.getId() + ":pitch", d.getPitch());
                addDashboardTelemetry(d.getId() + ":roll", d.getRoll());
                addDashboardTelemetry(d.getId() + ":yaw", d.getYaw());
                addDashboardTelemetry(d.getId() + ":range", d.getRange());
                addDashboardTelemetry(d.getId() + ":bearing", d.getBearing());
                addDashboardTelemetry(d.getId() + ":elevation", d.getElevation());
                addDashboardTelemetry(d.getId() + ":frameAcquisitionNanoTime", d.getFrameAcquisitionNanoTime());
                if (d.getRawPose() != null) {
                    addDashboardTelemetry(d.getId() + ":rawPoseX", d.getRawPose().x);
                    addDashboardTelemetry(d.getId() + ":rawPoseY", d.getRawPose().y);
                    addDashboardTelemetry(d.getId() + ":rawPoseZ", d.getRawPose().z);
                    addDashboardTelemetry(d.getId() + ":rawPoseRotationMatrix", d.getRawPose().R);
                }
            });
        } else {
            addTelemetry("No AprilTag data at % ns", System.nanoTime());
        }

        basicDrive
                .setSpeedUsingController(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x)
                .update();

        vision.update();
    }
}
