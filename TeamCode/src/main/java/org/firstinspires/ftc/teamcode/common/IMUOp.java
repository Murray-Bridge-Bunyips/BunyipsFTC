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
    private double previousHeading = 0, heading = 0, capture = 0;
    private boolean precisionDrive = false;

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
     * Start PrecisionDrive IMU alignment algorithm
     */
    public void startPrecisionDrive() {
        imu.startAccelerationIntegration(new Position(), new Velocity(), 50);
        capture = this.getHeading();
        precisionDrive = true;
    }

    /**
     * Query motor alignment speed for left side motors through PrecisionDrive
     */
     public double getLeftPrecisionSpeed(double original_speed, int tolerance) {
        double current = this.getHeading();
        if (current > capture - tolerance) {
            return original_speed - 0.1;
        }
        return original_speed;
     } 

     /**
     * Query motor alignment speed for right side motors through PrecisionDrive
     */
     public double getRightPrecisionSpeed(double original_speed, int tolerance) {
        double current = this.getHeading();
        if (current < capture + tolerance) {
            return original_speed - 0.1;
        }
        return original_speed;
     }
 }