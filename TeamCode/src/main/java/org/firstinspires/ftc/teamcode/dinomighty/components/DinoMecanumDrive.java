package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.example.examplerobot.components.ExampleMecanumDrive;

import java.util.Locale;

/**
 * Mecanum drive for DinoMighty
 */

public class DinoMecanumDrive extends BunyipsComponent {

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    // Stores the motor power needed into variables
    private Double speedX;
    private Double speedY;
    private Double speedR;

    public DinoMecanumDrive(@NonNull BunyipsOpMode opMode, DcMotor frontLeft, DcMotor backLeft, DcMotor frontRight, DcMotor backRight) {
        super(opMode);

        // Assign constructor parameters to the DinoMecanumDrive instance
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
    }

    enum Priority {
        NORMALISED, ROTATIONAL
    }

    // Store and declare prioritisation when given instruction to calculate motor powers
    private ExampleMecanumDrive.Priority priority = ExampleMecanumDrive.Priority.NORMALISED;

    // Setters for the prioritisation of the drive system
    public void setPriority(ExampleMecanumDrive.Priority priority) {
        this.priority = priority;
    }

    public void swapPriority() {
        this.setPriority(priority == ExampleMecanumDrive.Priority.NORMALISED ? ExampleMecanumDrive.Priority.ROTATIONAL : ExampleMecanumDrive.Priority.NORMALISED);
    }

    /**
     * Set a speed at which the Mecanum drive assembly should move.
     * @param x The speed at which the robot should move in the x direction.
     *          Positive is right, negative is left.
     *          Range: -1.0 to 1.0
     * @param y The speed at which the robot should move in the y direction.
     *          Positive is backward, negative is forward.
     *          Range: -1.0 to 1.0
     */
    public void setSpeedXYR(double x, double y, double r) {
        // X and Y have been swapped, and X has been inverted
        // This is to calibrate the controller movement to the robot
        this.speedX = Range.clip(y, -1.0, 1.0);
        this.speedY = Range.clip(-x, -1.0, 1.0);
        this.speedR = Range.clip(r, -1.0, 1.0);
    }

    /**
     * Set a polar speed at which the Mecanum drive assembly should move.
     * @param speed speed at which the motors will operate
     * @param direction_degrees direction at which the motors will move toward
     * @param speedR rotation speed - positive: anti-clockwise
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
        if (priority == ExampleMecanumDrive.Priority.ROTATIONAL) {
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

        getOpMode().addTelemetry(String.format(Locale.getDefault(),"Mecanum Drive: Forward: %.2f, Strafe: %.2f, Rotate: %.2f", speedX, speedY, speedR), false);
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

}
