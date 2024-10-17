package org.murraybridgebunyips.cellphone.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Second;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import android.speech.SpeechRecognizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.localization.Localizer;
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
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Dbg;
import org.murraybridgebunyips.bunyipslib.Filter;
import org.murraybridgebunyips.bunyipslib.external.Mathf;
import org.murraybridgebunyips.bunyipslib.roadrunner.util.DashboardUtil;
import org.murraybridgebunyips.bunyipslib.vision.AprilTagPoseEstimator;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.data.AprilTagData;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.cellphone.components.CellphoneConfig;

import java.util.ArrayList;

/**
 * test for AprilTagPoseEstimator sanity checking
 */
@TeleOp
public class CellphoneAprilTagTest extends BunyipsOpMode {
    private final CellphoneConfig config = new CellphoneConfig();
    private AprilTag aprilTag;
    private final boolean updateHeading = true;
    private final Pose2d cameraRobotOffset = new Pose2d(24,0,0);
//    double R = 4;
//    double Q = 1.0e-5;
//    private final Filter.Kalman xf = new Filter.Kalman(R, Q);
//    private final Filter.Kalman yf = new Filter.Kalman(R, Q);
//    private final Filter.Kalman rf = new Filter.Kalman(R, Q);
    private Localizer l = new Localizer() {
    private Pose2d poseEstimate = new Pose2d();
        @NonNull
        @Override
        public Pose2d getPoseEstimate() {
            return poseEstimate;
        }

        @Override
        public void setPoseEstimate(@NonNull Pose2d pose2d) {
            poseEstimate = pose2d;
        }

        @Nullable
        @Override
        public Pose2d getPoseVelocity() {
            return null;
        }

        @Override
        public void update() {
            poseEstimate = poseEstimate.plus(new Pose2d(0, 0, timer.deltaTime().in(Second)));
            poseEstimate = new Pose2d(poseEstimate.vec(), Mathf.inputModulus(poseEstimate.getHeading(), 0, 2 * Math.PI));
        }
    };
    private final ArrayList<Pose2d> estimates = new ArrayList<>();
//    private Pose2d performedCorrection = new Pose2d();
//    private Pose2d previousOffset = new Pose2d();

    @Override
    protected void onInit() {
        config.init();
        Vision vision = new Vision(config.cameraB);
        // add fake angled tag
        AprilTagMetadata meta = new AprilTagMetadata(14, "test", 1.5, new VectorF(0, 0, 0), DistanceUnit.INCH,
                new QuaternionMaker(0, 0, 0).make());
        aprilTag = new AprilTag((b) -> b.setTagLibrary(new AprilTagLibrary.Builder().setAllowOverwrite(true).addTags(AprilTagGameDatabase.getCurrentGameTagLibrary()).addTag(meta).build()));
        vision.init(aprilTag).start(aprilTag);
        vision.startPreview();
        AprilTagPoseEstimator.enable(aprilTag, l).setHeadingEstimate(false);
    }

    @Override
    protected void activeLoop() {
        telemetry.add("pose: %", l.getPoseEstimate());
        telemetry.dashboardFieldOverlay().setStroke("#FF0000");
        telemetry.dashboardFieldOverlay().strokeCircle(l.getPoseEstimate().getX(), l.getPoseEstimate()  .getY(), 1)
                .strokeCircle(0,0,24);
        DashboardUtil.drawRobot(telemetry.dashboardFieldOverlay(), l.getPoseEstimate());
        l.update();

//        poseEstimate = poseEstimate.plus(new Pose2d(0, 0, timer.deltaTime().in(Seconds)));
//        ArrayList<AprilTagData> data = aprilTag.getData();
//        if (data.isEmpty()) {
//            return;
//        }
//
//        estimates.clear();
//        for (int i = 0; i < data.size(); i++) {
//            AprilTagData aprilTag = data.get(i);
//            if (!aprilTag.getMetadata().isPresent() || !aprilTag.getFtcPose().isPresent()) {
//                // No luck with this ID
//                continue;
//            }
//
//            AprilTagMetadata metadata = aprilTag.getMetadata().get();
//            AprilTagPoseFtc camPose = aprilTag.getFtcPose().get();
//
//            VectorF tagPos = metadata.fieldPosition;
//            Orientation tagOri = metadata.fieldOrientation.toOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
//
//            double tagX = tagPos.get(0);
//            double tagY = tagPos.get(1);
//            double tagRotation = metadata.distanceUnit.toInches(tagOri.thirdAngle);
//            // 2D transformation matrix
//            // x' = x * cos(t) - y * sin(t)
//            // y' = x * sin(t) + y * cos(t)
//            // where t=0 yields (-y, x) for a 90 degree default rotation to accommodate for the 90 degree offset
//            // between RoadRunner pose and the FTC Global Coordinate system.
//            double t = tagRotation - Math.toRadians(camPose.yaw);
//            double relativeX = camPose.x * Math.cos(t) - camPose.y * Math.sin(t);
//            double relativeY = camPose.x * Math.sin(t) + camPose.y * Math.cos(t);
//            // Displacement vector
//            Vector2d pos = new Vector2d(
//                    tagX - relativeX,
//                    tagY - relativeY
//            );
//            // Offset as defined by the user to account for the camera not representing true position
//            pos = pos.minus(cameraRobotOffset.vec().rotated(tagRotation + Math.PI / 2).rotated(-Math.toRadians(camPose.yaw)));
//
//            // Only set the heading if the user wants it, which we can do fairly simply if they want that too
//            double heading = 0;
//            if (updateHeading) {
//                // Rotate 90 degrees (pi/2 rads) to match unit circle proportions due to a rotation mismatch as corrected
//                // in the vector calculation above
//                heading = Math.PI / 2.0 + tagRotation - Math.toRadians(camPose.yaw) - cameraRobotOffset.getHeading();
//            }
//            Pose2d atPoseEstimate = new Pose2d(pos.getX(), pos.getY(), Mathf.inputModulus(heading, -Math.PI, Math.PI));
//
////            Pose2d kfPose = new Pose2d(
////                    xf.calculate(uncorrected.getX(), atPoseEstimate.getX()),
////                    yf.calculate(uncorrected.getY(), atPoseEstimate.getY()),
////                    updateHeading ? rf.calculate(
////                            Mathf.inputModulus(uncorrected.getHeading(), -Math.PI, Math.PI),
////                            Mathf.inputModulus(atPoseEstimate.getHeading(), -Math.PI, Math.PI)
////                    ) : poseEstimate.getHeading()
////            );
//
//            // Avoid spamming the logs by logging the events that are over an inch away from the current estimate
////            if (poseEstimate.vec().distTo(kfPose.vec()) >= 1)
////                Dbg.logd(getClass(), "Updated pose based on AprilTag ID#%, %,%->%", aprilTag.getId(), poseEstimate, atPoseEstimate, kfPose);
//
//            // Use an unmodified pose as the one we actually calculate otherwise we'll oscillate around the target
//            // since the Kalman filter shouldn't be fed it's own data
//
//            estimates.add(atPoseEstimate);
//        }
//        Pose2d avg = estimates.stream()
//                .reduce(new Pose2d(), Pose2d::plus)
//                .div(estimates.size());
//
////        performedCorrection = avg.minus(poseEstimate);
//
//        poseEstimate = avg;
    }

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
            return new Quaternion((float) w, (float) x, (float) y, (float) z, 0);
        }
    }
}
