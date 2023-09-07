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

    public FieldCentricMecanumDrive(@NonNull BunyipsOpMode opMode, DcMotor frontLeft, DcMotor backLeft, DcMotor frontRight, DcMotor backRight, IMUOp imu, RelativeVector startingDirection) {
        super(opMode, frontLeft, backLeft, frontRight, backRight);
        this.imu = imu;
        // Current vector will be the robot's starting vector, must offset the IMU to align straight
        imu.setOffset(startingDirection.getAngle());
    }

    // Override the setSpeedXYR method to include the IMU heading in the calculation
    @Override
    public void setSpeedXYR(double x, double y, double r) {
        x = -x;
        imu.tick();
        double heading = imu.getRawHeading();
        double sin = Math.sin(Math.toRadians(heading));
        double cos = Math.cos(Math.toRadians(heading));
        // Transform the x and y values to be relative to the field
        // This is done by calculating the current heading to the field then rotating the x
        // and y vectors to be relative to the field, then updating the motor powers as normal
        super.speedX = x * cos + y * sin;
        super.speedY = x * sin - y * cos;
        super.speedR = r;
    }

    public void resetHeading() {
        imu.resetHeading();
    }
}
