package org.murraybridgebunyips.bunyipslib.drive;


import com.acmerobotics.roadrunner.geometry.Pose2d;

public interface Drivebase {
    /**
     * For continuity, keep setSpeedUsingController for setting drive speeds.
     * This was used in older versions of the BunyipsFTC repository, but will remain for compatibility.
     * Should internally run setWeightedDrivePower() and converts the controller input to a robot Pose2d.
     *
     * @param x gamepad.left_stick_x or similar
     * @param y gamepad.left_stick_y or similar
     * @param r gamepad.right_stick_x or similar
     */
    void setSpeedUsingController(double x, double y, double r);

    /**
     * Set speed based on a Robot Pose (x=forward, y=left, r=counter-clockwise rotation)
     */
    void setPower(Pose2d pose);
}
