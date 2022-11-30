package org.firstinspires.ftc.teamcode.lisa.tasks;


import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.lisa.config.LisaDrive;

// PrecisionDrive Algorithm II, Lucas Bubner, 2022
public class LisaPrecisionDriveTask extends Task implements TaskImpl {
    private final LisaDrive drive;
    private final BNO055IMU imu;
    private final double speed;
    private final double distanceCM;
    private final float tolerance;
    private final float reduction;
    private Orientation captureAngle;

    public LisaPrecisionDriveTask(BunyipsOpMode opMode, LisaDrive drive, BNO055IMU imu, double time, double distanceCM, double speed, double tolerance, double reduction) {
        super(opMode, time);
        this.drive = drive;
        this.speed = speed;
        this.imu = imu;
        this.tolerance = (float) tolerance;
        this.reduction = (float) reduction;

        // UltraPlanetary HD Hex Motors revolution count
        // TODO: Check these formulas
        final double TICKS_PER_REVOLUTION = 28;
        final double WHEEL_DIAMETER_CM = 8.5;

        this.distanceCM = (distanceCM * 10) * TICKS_PER_REVOLUTION / ((WHEEL_DIAMETER_CM / 10) * Math.PI);
    }

    @Override
    public void init() {
        super.init();
        drive.setEncoder(true);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 50);
        captureAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        // Start driving with a distance goal
        drive.setTargetPosition(distanceCM, distanceCM);
        drive.setPower(speed, speed);
        drive.update();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || drive.targetPositionReached();
    }

    @Override
    public void run() {

        // Stop if we've finished the distance or time goal
        if (isFinished()) {
            drive.setPower(0.0, 0.0);
            drive.update();
            drive.setEncoder(false);
            imu.stopAccelerationIntegration();
            return;
        }

        // Update angles and see if we need to adjust them
        Orientation currentAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        // Account for if we're travelling backwards
        double reducedspeed = speed > 0 ? speed - reduction : speed + reduction;
        if (currentAngle.firstAngle > captureAngle.firstAngle + tolerance) {
            // Rotate CCW
            drive.setPower(speed, reducedspeed);
            drive.update();
        } else if (currentAngle.firstAngle < captureAngle.firstAngle - tolerance) {
            // Rotate CW
            drive.setPower(reducedspeed, speed);
            drive.update();
        } else {
            // Continue straight
            drive.setPower(speed, speed);
            drive.update();
        }

        // Telemetry data
        opMode.telemetry.addData("Original Angle", captureAngle.firstAngle);
        opMode.telemetry.addData("Current Angle", currentAngle.firstAngle);
    }
}
