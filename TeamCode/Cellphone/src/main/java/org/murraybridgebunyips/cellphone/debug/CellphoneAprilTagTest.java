package org.murraybridgebunyips.cellphone.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.roadrunner.util.DashboardUtil;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.data.AprilTagData;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.cellphone.components.CellphoneConfig;

import java.util.ArrayList;

/** test for AprilTagPoseEstimator sanity checking */
@TeleOp
public class CellphoneAprilTagTest extends BunyipsOpMode {
    private final CellphoneConfig config = new CellphoneConfig();
    private AprilTag aprilTag;

    static class QuaternionMaker {
        public double w, x, y, z;

        public QuaternionMaker(double rollDegrees, double pitchDegrees, double yawDegrees) {
            double roll = Math.toRadians(rollDegrees);
            double pitch = Math.toRadians(pitchDegrees);
            double yaw = Math.toRadians(yawDegrees);

            double cy = Math.cos(yaw * 0.5);
            double sy = Math.sin(yaw * 0.5);
            double cp = Math.cos(pitch * 0.5);
            double sp = Math.sin(pitch * 0.5);
            double cr = Math.cos(roll * 0.5);
            double sr = Math.sin(roll * 0.5);

            w = cr * cp * cy + sr * sp * sy;
            x = sr * cp * cy - cr * sp * sy;
            y = cr * sp * cy + sr * cp * sy;
            z = cr * cp * sy - sr * sp * cy;
        }

        public Quaternion make() {
            return new Quaternion((float)w, (float)x, (float)y, (float)z, 0);
        }
    }

    @Override
    protected void onInit() {
        config.init();
        Vision vision = new Vision(config.cameraB);
        // add fake angled tag
        AprilTagMetadata meta = new AprilTagMetadata(13, "test", 7, new VectorF(0, 0, 0), DistanceUnit.INCH,
                new QuaternionMaker(0, 0, 45).make());
        aprilTag = new AprilTag((b) -> b.setTagLibrary(new AprilTagLibrary.Builder().addTags(AprilTagGameDatabase.getCenterStageTagLibrary()).addTag(meta).build()));
        vision.init(aprilTag).start(aprilTag);
        vision.startPreview();
    }

    @Override
    protected void activeLoop() {
        ArrayList<AprilTagData> data = aprilTag.getData();
        if (data.isEmpty())
            return;

        for (int i = 0; i < data.size(); i++) {
            AprilTagData aprilTag = data.get(i);
            if (!aprilTag.getMetadata().isPresent() || !aprilTag.getFtcPose().isPresent()) {
                // No luck with this ID
                continue;
            }

            VectorF tagPos = aprilTag.getMetadata().get().fieldPosition;
            Orientation tagOri = aprilTag.getMetadata().get().fieldOrientation
                    .toOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);

            double camX = aprilTag.getFtcPose().get().x;
            double camY = aprilTag.getFtcPose().get().y;
            double tagX = tagPos.get(0);
            double tagY = tagPos.get(1);
            double tagRotation = tagOri.thirdAngle;
            // x'=x*cos(t)-y*sin(t)
            // y'=x*sin(t)+y*cos(t)
            double relativeOffsetX = camX * Math.cos(tagRotation) - camY * Math.sin(tagRotation);
            double relativeOffsetY = camX * Math.sin(tagRotation) + camY * Math.cos(tagRotation);
            Vector2d pos = new Vector2d(
                    tagX - relativeOffsetX,
                    tagY - relativeOffsetY
            );
            Pose2d o = new Pose2d(9, 0, 0);
            pos = pos.minus(o.vec());

            double heading = Math.PI / 2.0 + tagRotation - Math.toRadians(aprilTag.getFtcPose().get().yaw) + o.getHeading();
            Pose2d estimatedPose = new Pose2d(pos.getX(), pos.getY(), heading);

            telemetry.add("Pose estimate based on AprilTagPoseEstimator: (% cm, % cm, % deg)", Centimeters.convertFrom(estimatedPose.getX(), Inches), Centimeters.convertFrom(estimatedPose.getY(), Inches), Math.toDegrees(estimatedPose.getHeading()));

            telemetry.dashboardFieldOverlay().setStroke("#FF0000");
            DashboardUtil.drawRobot(telemetry.dashboardFieldOverlay(), estimatedPose);
            break;
        }
    }
}
