package org.firstinspires.ftc.teamcode.jerry.config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

import java.util.Locale;

public class JerryDrive extends BunyipsComponent {

    private BunyipsOpMode opmode;
    final private DcMotorEx bl;
    final private DcMotorEx br;
    final private DcMotorEx fl;
    final private DcMotorEx fr;

    private double speedX = 0.0;
    private double speedY = 0.0;
    private double speedR = 0.0;


    // Drive mode functionality to change between normal and rotational modes
    public enum MecanumDriveMode {
        NORMALIZED, ROTATION_PRIORITY_NORMALIZED
    }

    private MecanumDriveMode driveMode = MecanumDriveMode.NORMALIZED;
    public void setDriveMode(MecanumDriveMode mode) {
        driveMode = mode;
    }

    public JerryDrive(BunyipsOpMode opMode,
                      DcMotorEx bl, DcMotorEx br,
                      DcMotorEx fl, DcMotorEx fr) {
        // Encoders are not controlled by JerryDrive, as they are not connected
        super(opMode);
        this.bl = bl;
        this.br = br;
        this.fl = fl;
        this.fr = fr;

        // Make sure all the motors actually exist
        // The OpMode will catch this error if a motor is for some reason not connected
        assert bl != null && br != null && fl != null && fr != null;
    }

    public void setToFloat() {
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setToBrake() {
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Call to update motor speeds through the selected drivemode.
     * Rotation Priority will calculate rotation speed before translation speed, while normalised
     * will do the opposite, calculating
     */
    public void update() {
        if (driveMode == MecanumDriveMode.ROTATION_PRIORITY_NORMALIZED) rotationalPriority();

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

        fl.setPower(frontLeftPower);
        fr.setPower(frontRightPower);
        bl.setPower(backLeftPower);
        br.setPower(backRightPower);

        getOpMode().telemetry.addLine(String.format(Locale.getDefault(),"Mecanum Drive: Forward: %.2f, Strafe: %.2f, Rotate: %.2f", speedX, speedY, speedR));
    }

    /**
     * This method is automatically called when required from the driveMode.
     * Calculate rotational speed first, and use remaining headway for translation.
     */
    private void rotationalPriority() {
        // Calculate motor powers
        double[] translationValues = {
                speedX + speedY,
                speedX - speedY,
                speedX - speedY,
                speedX + speedY};

        double[] rotationValues = {
                -speedR,
                speedR,
                -speedR,
                speedR};

        double scaleFactor = 1.0;
        double tmpScale = 1.0;

        // Solve this equation backwards:
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

        fl.setPower(frontLeftPower);
        fr.setPower(frontRightPower);
        bl.setPower(backLeftPower);
        br.setPower(backRightPower);
    }

    /**
     * Set all motor speeds to zero
     */
    public void deinit() {
        this.setSpeedXYR(0, 0, 0);
        this.update();
    }

    /**
     * @param speedX relative east-west speed - positive: left
     * @param speedY relative north-south speed - positive: north
     * @param speedR rotation speed - positive: anti-clockwise
     */
    public void setSpeedXYR(double speedX, double speedY, double speedR) {
        // X and Y have been swapped, and X has been inverted
        // This is to calibrate the controller movement to the robot
        this.speedX = clipMotorPower(-speedY);
        this.speedY = clipMotorPower(speedX);
        this.speedR = clipMotorPower(-speedR);
    }

    /**
     * @param speed speed at which the motors will operate
     * @param direction_degrees direction at which the motors will move toward
     * @param speedR rotation speed - positive: anti-clockwise
     */
    public void setSpeedPolarR(double speed, double direction_degrees, double speedR) {
        double radians = Math.toRadians(direction_degrees);
        this.speedX = clipMotorPower(speed * Math.cos(radians));
        this.speedY = clipMotorPower(speed * Math.sin(radians));
        this.speedR = clipMotorPower(speedR);
    }

    private double clipMotorPower(double p) {
        return Range.clip(p, -1.0, 1.0);
    }

}
