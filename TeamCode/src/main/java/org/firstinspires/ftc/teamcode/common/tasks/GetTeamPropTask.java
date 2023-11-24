package org.firstinspires.ftc.teamcode.common.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Dbg;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.vision.VisionPortal;

/**
 * Task for detecting on which spike the Team Prop is placed on.
 * This is measured from the starting position, with the camera facing towards the Spike Marks.
 *
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
 */
public class GetTeamPropTask extends Task {
    private final Vision vision;

    public GetTeamPropTask(@NonNull BunyipsOpMode opMode, Vision vision) {
        super(opMode);
        this.vision = vision;
    }

    @Override
    public void init() {
        if (vision.visionPortalNull()) {
            if (vision.getStatus() == VisionPortal.CameraState.STREAMING) {
                // Camera is already running, notify of this
                Dbg.log("TeamPropTask is reinitialising Vision!");
            }

            if (vision.getStatus() == VisionPortal.CameraState.CAMERA_DEVICE_READY) {

            }
        } else {
            vision.init(); // I think this needs to have an argument?
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public boolean isTaskFinished() {
        return false;
    }
}
