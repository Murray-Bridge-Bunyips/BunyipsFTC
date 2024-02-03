package org.murraybridgebunyips.bunyipslib.roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Dbg;
import org.murraybridgebunyips.bunyipslib.EmergencyStop;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;

import java.util.ArrayList;

/**
 * PathRecorder - a macro-like RoadRunner trajectory generator from user input
 * <p>
 * To use this class, you must be using a RoadRunner drive that can provide pose estimates,
 * and proper starting pose data. This class will record user input and generate a trajectory
 * from that input. This trajectory can then be used to generate a RoadRunner trajectory.
 * <p>
 * You will need to make a new OpMode that extends this class.
 * @author Lucas Bubner, 2024
 */
public abstract class PathRecorder extends BunyipsOpMode {

    /**
     * Initialise your drive and config here.
     */
    protected abstract void configureRobot();

    /**
     * Set the drive that will be used to record the path.
     * @return the drive that will be used to record the path, will be called after configureRobot()
     */
    protected abstract RoadRunnerDrive setDrive();

    /**
     * Set the starting pose of the robot.
     * @return the starting pose of the robot, this should be in the inches coordinate system used by RoadRunner/FtcDashboard/RRPathGen
     */
    protected abstract Pose2d setStartPose();

    /**
     * Set the duration at which Pose snapshots will be taken (a path will be generated from these snapshots).
     * @return the duration at which Pose snapshots will be taken, in milliseconds
     */
    protected abstract int setSnapshotDuration();

    protected RoadRunnerDrive drive;
    protected int snapshotDuration;
    private ArrayList<Pose2d> path;
    private Pose2d startPose;
    private Pose2d currentPose;
    private ElapsedTime timer;

    @Override
    protected final void onInit() {
        configureRobot();
        if (drive == null)
            drive = setDrive();
        if (drive == null)
            throw new EmergencyStop("Drive not set in PathRecorder");
        if (startPose == null)
            startPose = setStartPose();
        if (startPose == null)
            throw new EmergencyStop("Start pose not set in PathRecorder");
        drive.setPoseEstimate(startPose);
        if (snapshotDuration == 0)
            snapshotDuration = Math.abs(setSnapshotDuration());
        if (snapshotDuration == 0)
            throw new EmergencyStop("Snapshot duration must be not zero");
    }

    @Override
    protected final boolean onInitLoop() {
        // no-op
        return true;
    }

    @Override
    protected final void onInitDone() {
        // no-op
    }

    @Override
    protected final void onStart() {
        timer.reset();
    }

    @Override
    protected final void activeLoop() {
        addTelemetry("PoseRecorder is recording...");
        addTelemetry("Current pose: %", currentPose.toString());
        addTelemetry("Snapshot count: %", path.size());
        addTelemetry("Snapshot duration: %ms", snapshotDuration);
        addTelemetry("Press B to stop recording");
        if (gamepad1.b) {
            drive.stop();
            path.add(currentPose);
            clearTelemetry();
            addTelemetry("Processing data...");
            pushTelemetry();
            processData();
            finish();
        }
        drive.setWeightedDrivePower(
                Controller.makeRobotPose(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x)
        );
        currentPose = drive.getPoseEstimate();
        if (timer.milliseconds() >= snapshotDuration) {
            path.add(currentPose);
            Dbg.log(getClass(), "Snapshotting pose % -> %", path.size(), currentPose.toString());
            timer.reset();
        }
        drive.update();
    }

    private void processData() {
        Dbg.log("Processing data of % poses...", path.size());
        StringBuilder sb = new StringBuilder();
        sb.append("addNewTrajectory(new Pose2d(")
                .append(startPose.getX())
                .append(", ")
                .append(startPose.getY())
                .append(", ")
                .append(startPose.getHeading())
                .append("))")
                .append("\n");
        for (Pose2d pose : path) {
            sb.append(".lineToLinearHeading(new Pose2d(")
                    .append(pose.getX())
                    .append(", ")
                    .append(pose.getY())
                    .append(", ")
                    .append(pose.getHeading())
                    .append("));\n");
        }
        sb.append(".build();");
        clearTelemetry();
        addRetainedTelemetry(sb.toString());
        Dbg.log(sb.toString());
    }

    @Override
    protected final void onStop() {
        // no-op
    }
}
