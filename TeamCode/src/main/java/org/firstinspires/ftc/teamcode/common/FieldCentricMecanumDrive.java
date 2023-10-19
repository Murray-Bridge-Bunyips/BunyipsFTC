package org.firstinspires.ftc.teamcode.common;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Variant of the MecanumDrive that uses field centric control.
 *
 * @author Lucas Bubner, 2023
 * @see MecanumDrive
 */
public abstract class FieldCentricMecanumDrive extends MecanumDrive {
    private final IMUOp imu;

    protected FieldCentricMecanumDrive(@NonNull BunyipsOpMode opMode, DcMotor frontLeft, DcMotor backLeft, DcMotor frontRight, DcMotor backRight, IMUOp imu, RelativeVector startingDirection) {
        super(opMode, frontLeft, backLeft, frontRight, backRight);
        this.imu = imu;
        if (startingDirection == RelativeVector.CLOCKWISE || startingDirection == RelativeVector.ANTICLOCKWISE) {
            throw new IllegalArgumentException("Cannot use rotational quantities as a starting direction");
        }
        // Current vector will be the robot's starting vector, must offset the IMU to align straight
        imu.setOffset(startingDirection.getAngle());
    }

    // Override the setSpeedUsingController method to include the IMU heading in the calculation
    // Field-centric should never have to deal with setSpeedXYR itself since autonomous won't need
    // to use it, so it's safe to only implement half of the methods.
    @Override
    public void setSpeedUsingController(double x, double y, double r) {
        imu.tick();

        // Account for the rotated vector of the gamepad
        double heading = imu.getRawHeading() + 90;
        x = -x;

        double sin = Math.sin(Math.toRadians(heading));
        double cos = Math.cos(Math.toRadians(heading));

        // Transform the x and y values to be relative to the field
        // This is done by calculating the current heading to the field then rotating the x
        // and y vectors to be relative to the field, then updating the motor powers as normal
        speedX = x * cos + y * sin;
        speedY = x * sin - y * cos;
        speedR = r;
    }
}
