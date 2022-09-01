package org.firstinspires.ftc.teamcode.lisa;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.common.BaseTask;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Task;


// TODO: Check if negative values are needed for CW rotation, and change tolerance code
public class LisaIMUTask extends BaseTask implements Task {

    private final LisaDrive drive;
    private final BNO055IMU imu;
    private final double speed;
    private final boolean ccw;
    private final float angle;
    private Orientation angles;

    public LisaIMUTask(BunyipsOpMode opMode, double time, LisaDrive drive, double speed, boolean ccw, BNO055IMU imu, double angle) {
        super(opMode, time);
        this.drive = drive;
        this.speed = speed;
        this.imu = imu;
        this.ccw = ccw;
        this.angle = ccw ? (float) angle : (float) -angle;
    }

    @Override
    public void init() {
        super.init();
        imu.startAccelerationIntegration(new Position(), new Velocity(), 50);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double leftSpeed = (ccw ? speed : -speed);
        double rightSpeed = (ccw ? -speed : speed);
        drive.setPower(leftSpeed, rightSpeed);
        drive.update();
    }

    @Override
    public void run() {
        if (isFinished()) {
            drive.setPower(0.0, 0.0);
            drive.update();
            imu.stopAccelerationIntegration();
            return;
        }

        Orientation currentAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        opMode.telemetry.addData("Original Angle", angles.firstAngle);
        opMode.telemetry.addData("Current Angle", currentAngles.firstAngle);
        if (currentAngles.firstAngle > (angles.firstAngle + (ccw ? angle - 15 : angle + 15))) {
            drive.setPower(0.0, 0.0);
            drive.update();
            if (currentAngles.firstAngle > (angles.firstAngle + (ccw ? angle : -angle))) {
                drive.setPower(ccw ? 0.1 : -0.1, ccw ? -0.1 : 0.1);
                drive.update();
            } else {
                isFinished = true;
            }
        }
    }
}