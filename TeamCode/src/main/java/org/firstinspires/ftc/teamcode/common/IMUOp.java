package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * IMUOperation custom common class for internal BNO055IMUs
 * @author Lucas Bubner - FTC 15215 Captain; Oct 2022 - Murray Bridge Bunyips
 */
 public class IMUOp extends BunyipsComponent {

    private final BNO055IMU imu;
    public volatile Orientation currentAngles;
    private double previousHeading = 0, heading = 0, capture = null;

    public IMUOp(BunyipsOpMode opMode, BNO055IMU imu) {
        super(opMode);
        this.imu = imu;
    }

    /**
     * Update the latest state in the IMU to current data
     */
    public void tick() {
        currentAngles = imu.getAngularOrientation(
                AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
    }

    /**
     * Get the current heading reading from the internal IMU, with support for 360 degree readings
     * Instead of using Euler readings, this will return a number within 0 to 360
     * @return Z value of Orientation axes in human-friendly reading range [0, 360]
     */
    public double getHeading() {
        double currentHeading = currentAngles.thirdAngle;
        double delta = currentHeading - previousHeading;

        // Detects if there is a sudden 180 turn which means we have turned more than the 180
        // degree threshold. Adds 360 to additively inverse the value and give us a proper delta
        if (delta < -180) {
            delta += 360;
        } else if (delta >= 180) {
            delta -= 360;
        }

        heading += delta;
        previousHeading = currentHeading;

        // Limit heading readings to only be in a (0, 360) index
        if (heading >= 360.0) {
            heading -= 360.0;
        } else if (heading < 0.0) {
            heading += 360.0;
        }

        return heading;
    }

    /**
     * Get the current roll reading from the internal IMU
     * @return Y value of Orientation axes
     */
    public double getRoll() {
        return currentAngles.secondAngle;
    }

    /**
     * Get the current pitch reading from the internal IMU
     * @return X value of Orientation axes
     */
    public double getPitch() {
        return currentAngles.firstAngle;
    }

    /**
     * Start PrecisionDrive IMU alignment algorithm and capture the original angle
     */
    public void startCapture() {
        capture = this.getHeading();
    }

    /**
     * Stop and reset PrecisionDrive IMU alignment algorithm
     */
     public void resetCapture() {
        capture = null;
     }

    /**
     * Query motor alignment speed for a motor through PrecisionDrive
     * @param original_speed supply the intended speed for the motor
     * @param tolerance supply the tolerance range (tol < x <  tol) before making a correction
     * @param isLeft state whether the returned speed of the motor is on the left side of the robot
     * @return queried speed based on parameters given, returns null if PrecisionDrive is not online
     */
     public double getPrecisionSpeed(double original_speed, int tolerance, boolean isLeft) {
        if (capture == null) { return null; }

        double current = this.getHeading();
        if (isLeft && original_speed > 0 ? current > capture - tolerance : current < capture + tolerance) {
            return Math.abs(original_speed) - 0.1;
        }
        return Math.abs(original_speed);
     } 
 }