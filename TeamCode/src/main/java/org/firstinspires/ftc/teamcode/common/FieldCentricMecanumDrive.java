package org.firstinspires.ftc.teamcode.common;

import static java.lang.Math.abs;

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
        if (startingDirection == RelativeVector.CLOCKWISE || startingDirection == RelativeVector.ANTICLOCKWISE) {
            throw new IllegalArgumentException("Cannot use rotational quantities as a starting direction");
        }
        // Current vector will be the robot's starting vector, must offset the IMU to align straight
        // Since the controls are the same for driving on each two quadrants, we can use absolute values
        imu.setOffset(startingDirection.getAngle());
        imu.setOffset(abs(imu.getOffset()));
    }

    // Override the setSpeedUsingController method to include the IMU heading in the calculation
    // Field-centric should never have to deal with setSpeedXYR itself since autonomous won't need
    // to use it, so it's safe to only implement half of the methods.
    @Override
    public void setSpeedUsingController(double x, double y, double r) {
        // TODO: Validate as vectors have changed
        x = -x;
        imu.tick();
        double heading = imu.getRawHeading() + 90;
        double sin = Math.sin(Math.toRadians(heading));
        double cos = Math.cos(Math.toRadians(heading));
        // Transform the x and y values to be relative to the field
        // This is done by calculating the current heading to the field then rotating the x
        // and y vectors to be relative to the field, then updating the motor powers as normal
        super.speedX = x * cos + y * sin;
        super.speedY = x * sin - y * cos;
        super.speedR = r;
    }
}
