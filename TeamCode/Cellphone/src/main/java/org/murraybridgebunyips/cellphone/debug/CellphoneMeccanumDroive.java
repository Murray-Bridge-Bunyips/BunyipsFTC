package org.murraybridgebunyips.cellphone.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.DriveConstants;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.trajectorysequence.TrajectorySequence;
import org.murraybridgebunyips.bunyipslib.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.murraybridgebunyips.bunyipslib.roadrunner.trajectorysequence.TrajectorySequenceRunner;
import org.murraybridgebunyips.bunyipslib.roadrunner.util.DashboardUtil;
import org.murraybridgebunyips.bunyipslib.vision.AprilTagPoseEstimator;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.cellphone.components.CellphoneConfig;

import java.util.Collections;
import java.util.List;

/**
 * Fake mecanum drive (meccanum droive)
 */
@TeleOp
public class CellphoneMeccanumDroive extends BunyipsOpMode {
    private final CellphoneConfig config = new CellphoneConfig();
    private Pose2d pose = new Pose2d();

    private class DummyDrive implements RoadRunnerDrive {

        @Override
        public TrajectorySequenceRunner getTrajectorySequenceRunner() {
            return null;
        }

        @Override
        public void stop() {

        }

        @Override
        public void waitForIdle() {

        }

        @Override
        public DriveConstants getConstants() {
            return null;
        }

        @Override
        public TrajectoryBuilder trajectoryBuilder(Pose2d startPose) {
            return null;
        }

        @Override
        public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, boolean reversed) {
            return null;
        }

        @Override
        public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, double startHeading) {
            return null;
        }

        @Override
        public TrajectorySequenceBuilder<?> trajectorySequenceBuilder(Pose2d startPose) {
            return null;
        }

        @Override
        public void turnAsync(double angle) {

        }

        @Override
        public void turn(double angle) {

        }

        @Override
        public void followTrajectoryAsync(Trajectory trajectory) {

        }

        @Override
        public void followTrajectory(Trajectory trajectory) {

        }

        @Override
        public void followTrajectorySequenceAsync(TrajectorySequence trajectorySequence) {

        }

        @Override
        public void followTrajectorySequence(TrajectorySequence trajectorySequence) {

        }

        @Override
        public Pose2d getLastError() {
            return null;
        }

        @Override
        public void update() {

        }

        @Override
        public boolean isBusy() {
            return false;
        }

        @Override
        public void cancelTrajectory() {

        }

        @Override
        public void setMode(DcMotor.RunMode runMode) {

        }

        @Override
        public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {

        }

        @Override
        public void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {

        }

        @Override
        public void setWeightedDrivePower(Pose2d drivePower) {

        }

        @Override
        public void setRotationPriorityWeightedDrivePower(Pose2d drivePowerRotationPriority) {

        }

        @Override
        public List<Double> getWheelPositions() {
            return Collections.emptyList();
        }

        @Override
        public List<Double> getWheelVelocities() {
            return Collections.emptyList();
        }

        @Override
        public double[] getMotorPowers() {
            return new double[0];
        }

        @Override
        public void setMotorPowers(double... powers) {

        }

        @Override
        public double getRawExternalHeading() {
            return 0;
        }

        @Override
        public Double getExternalHeadingVelocity() {
            return 0.0;
        }

        @Override
        public Localizer getLocalizer() {
            return null;
        }

        @Override
        public void setLocalizer(Localizer localizer) {

        }

        @Override
        public double getExternalHeading() {
            return 0;
        }

        @Override
        public void setExternalHeading(double value) {

        }

        @Override
        public Pose2d getPoseEstimate() {
            return pose;
        }

        @Override
        public void setPoseEstimate(Pose2d value) {
            pose = value;
        }

        @Nullable
        @Override
        public Pose2d getPoseVelocity() {
            return null;
        }

        @Override
        public void updatePoseEstimate() {

        }

        @Override
        public void setDriveSignal(DriveSignal driveSignal) {

        }

        @Override
        public void setDrivePower(Pose2d drivePower) {

        }
    }

    @Override
    protected void onInit() {
        config.init();
        Vision vision = new Vision(config.cameraB);
        AprilTag at = new AprilTag();
        vision.init(at);
        vision.start(at);
        vision.startPreview();
        DummyDrive dummyDrive = new DummyDrive();
        onActiveLoop(new AprilTagPoseEstimator(at, dummyDrive).setCameraOffset(new Pose2d(9, 0, 0)));
    }

    @Override
    protected void activeLoop() {
        // field centric because i cant be bothered slapping a rot matrix here
        pose = new Pose2d(
                pose.getX() - gamepad1.lsy * 30 * timer.deltaTime().in(Seconds),
                pose.getY() - gamepad1.lsx * 30 * timer.deltaTime().in(Seconds),
                pose.getHeading() - gamepad1.rsx * 1.57 * timer.deltaTime().in(Seconds)
        );

        telemetry.addData("x", pose.getX());
        telemetry.addData("y", pose.getY());
        telemetry.addData("r", pose.getHeading());

        DashboardUtil.drawRobot(telemetry.dashboardFieldOverlay(), pose);
    }
}
