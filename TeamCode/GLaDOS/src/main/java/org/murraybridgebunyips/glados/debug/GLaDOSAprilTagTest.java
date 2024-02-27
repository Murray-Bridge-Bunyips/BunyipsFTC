package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.cameras.C920;
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

    @Override
    protected void onInit() {
        config.init();
        vision = new Vision(config.webcam);
        aprilTag = new AprilTag();
        vision.init(aprilTag);
        vision.start(aprilTag);
        vision.startPreview();
    }

    @Override
    protected void activeLoop() {
        List<AprilTagData> data = aprilTag.getData();
        if (!data.isEmpty()) {
            // TODO: convert objects to telemetry single numerical
            addDashboardTelemetry("id", data.get(0).getId());
            addDashboardTelemetry("hamming", data.get(0).getHamming());
            addDashboardTelemetry("decisionMargin", data.get(0).getDecisionMargin());
            addDashboardTelemetry("center", data.get(0).getCenter());
            addDashboardTelemetry("corners", data.get(0).getCorners());
            addDashboardTelemetry("label", data.get(0).getLabel());
            addDashboardTelemetry("tagsize", data.get(0).getTagsize());
            addDashboardTelemetry("fieldPosition", data.get(0).getFieldPosition());
            addDashboardTelemetry("fieldOrientation", data.get(0).getFieldOrientation());
            addDashboardTelemetry("distanceUnit", data.get(0).getDistanceUnit());
            addDashboardTelemetry("x", data.get(0).getX());
            addDashboardTelemetry("y", data.get(0).getY());
            addDashboardTelemetry("z", data.get(0).getZ());
            addDashboardTelemetry("pitch", data.get(0).getPitch());
            addDashboardTelemetry("roll", data.get(0).getRoll());
            addDashboardTelemetry("yaw", data.get(0).getYaw());
            addDashboardTelemetry("range", data.get(0).getRange());
            addDashboardTelemetry("bearing", data.get(0).getBearing());
            addDashboardTelemetry("elevation", data.get(0).getElevation());
        }
    }
}
