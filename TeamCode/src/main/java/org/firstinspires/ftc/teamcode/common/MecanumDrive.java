package org.firstinspires.ftc.teamcode.common;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

/**
 * Base class for a drive system that uses Mecanum wheels.
 * Includes all the common math done across all Mecanum drive systems.
 * @author Lucas Bubner, 2023
 */
public abstract class MecanumDrive extends BunyipsComponent {

    private final DcMotor frontLeft;
    private final DcMotor backLeft;
    private final DcMotor frontRight;
    private final DcMotor backRight;

    // Axial translation speeds
    Double speedX;
    Double speedY;
    Double speedR;

    // Store and declare prioritisation when given instruction to calculate motor powers
    private Priority priority = Priority.NORMALISED;

    public MecanumDrive(@NonNull BunyipsOpMode opMode, DcMotor frontLeft, DcMotor backLeft, DcMotor frontRight, DcMotor backRight) {
        super(opMode);
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
    }

    // Setters for the prioritisation of the drive system
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void swapPriority() {
        this.setPriority(priority == Priority.NORMALISED ? Priority.ROTATIONAL : Priority.NORMALISED);
    }

    /**
     * Set a speed at which the Mecanum drive assembly should move.
     * @param x The speed at which the robot should move in the x direction.
     *          Positive is right, negative is left.
     *          Range: -1.0 to 1.0
     * @param y The speed at which the robot should move in the -y direction.
     *          Positive is backward, negative is forward.
     *          Range: -1.0 to 1.0
     * @param r The speed at which the robot will rotate.
     *          Positive is clockwise, negative is anti-clockwise.
     *          Range: -1.0 to 1.0
     */
    public void setSpeedXYR(double x, double y, double r) {
        // X and Y have been swapped, and X has been inverted
        // This rotates input vectors by 90 degrees clockwise and wil account for gamepad input.
        this.speedX = Range.clip(y, -1.0, 1.0);
        this.speedY = Range.clip(-x, -1.0, 1.0);
        this.speedR = Range.clip(r, -1.0, 1.0);
    }

    /**
     * Set a polar speed at which the Mecanum drive assembly should move.
     *
     * @param speed             speed at which the motors will operate
     * @param direction_degrees direction at which the motors will move toward
     * @param speedR            rotation speed - positive: clockwise
     */
    public void setSpeedPolarR(double speed, double direction_degrees, double speedR) {
        double radians = Math.toRadians(direction_degrees);
        this.speedX = Range.clip(speed * Math.cos(radians), -1.0, 1.0);
        this.speedY = Range.clip(speed * Math.sin(radians), -1.0, 1.0);
        this.speedR = Range.clip(speedR, -1.0, 1.0);
    }

    /**
     * Update and reflect the speed of the drive system to the actual motors, and
     * calculate the motor powers based on these variables.
     */
    public void update() {
        if (priority == Priority.ROTATIONAL) {
            rotationalUpdate();
        }

        // Calculate motor powers
        double frontLeftPower = speedX + speedY - speedR;
        double frontRightPower = speedX - speedY + speedR;
        double backLeftPower = speedX - speedY - speedR;
        double backRightPower = speedX + speedY + speedR;

        double maxPower = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
        // If the maximum number is greater than 1.0, then normalise by that number
        if (maxPower > 1.0) {
            frontLeftPower = frontLeftPower / maxPower;
            frontRightPower = frontRightPower / maxPower;
            backLeftPower = backLeftPower / maxPower;
            backRightPower = backRightPower / maxPower;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        getOpMode().addTelemetry(String.format(Locale.getDefault(), "Mecanum Drive: Forward: %.2f, Strafe: %.2f, Rotate: %.2f", speedX, speedY, speedR), false);
    }

    /**
     * Immediately stop the drive system.
     */
    public void deinit() {
        this.speedX = 0.0;
        this.speedY = 0.0;
        this.speedR = 0.0;
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
        update();
    }

    /**
     * Set the drive system to brake.
     */
    public void setToBrake() {
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Set the drive system to float.
     */
    public void setToFloat() {
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    private void rotationalUpdate() {
        // Calculate translational speeds
        double[] translationValues = {
                speedX + speedY,
                speedX - speedY,
                speedX - speedY,
                speedX + speedY
        };

        double[] rotationValues = {
                -speedR,
                speedR,
                -speedR,
                speedR
        };

        double scaleFactor = 1.0;
        double tmpScale = 1.0;

        // Solve this equation backwards
        // MotorX = TranslationX * scaleFactor + RotationX
        // to find scaleFactor that ensures -1 <= MotorX <= 1 and 0 < scaleFactor <= 1
        for (int i = 0; i < 4; i++) {
            if (Math.abs(translationValues[i] + rotationValues[i]) > 1) {
                tmpScale = (1 - rotationValues[i]) / translationValues[i];
            } else if (translationValues[i] + rotationValues[i] < -1) {
                tmpScale = (rotationValues[i] - 1) / translationValues[i];
            }
            if (tmpScale < scaleFactor) {
                scaleFactor = tmpScale;
            }
        }

        double frontLeftPower = translationValues[0] * scaleFactor + rotationValues[0];
        double frontRightPower = translationValues[1] * scaleFactor + rotationValues[1];
        double backLeftPower = translationValues[2] * scaleFactor + rotationValues[2];
        double backRightPower = translationValues[3] * scaleFactor + rotationValues[3];

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

    /**
     * Set motor speeds based on a RobotVector or RelativeVector.
     */
    public <T> void setVector(T vector) {
        if (vector instanceof RobotVector) {
            RobotVector robotVector = (RobotVector) vector;
            this.speedX = robotVector.getX();
            this.speedY = robotVector.getY();
            this.speedR = robotVector.getR();
        } else if (vector instanceof RelativeVector) {
            RelativeVector relativeVector = (RelativeVector) vector;
            this.speedX = relativeVector.getVector().getX();
            this.speedY = relativeVector.getVector().getY();
            this.speedR = relativeVector.getVector().getR();
        }
    }

    enum Priority {
        NORMALISED, ROTATIONAL
    }
}

