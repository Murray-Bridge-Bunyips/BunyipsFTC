package org.firstinspires.ftc.teamcode.lisa;

public class LisaIMUTask extends BaseTask implements Task {

    private final LisaDrive drive;
    private final double leftSpeed;
    private final double rightSpeed;

    public LisaIMUTask(BunyipsOpMode opMode, double time, LisaDrive drive, double speed, boolean ccw, BNO055IMU imu, double angle) {
        super(opMode, time);
        this.drive = drive;
        this.speed = speed;
        this.imu = imu;
        this.ccw = ccw;
        this.angle = (float) angle;
    }

    @Override
    public void init() {
        super.init();
        imu.startAccelerationIntegration(new Position(), new Velocity(), 50);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        leftSpeed = (ccw ? speed : -speed);
        rightSpeed - (ccw ? -speed : speed);
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
        if (currentAngles.firstAngle > (angles.firstAngle + angle)) {
            drive.setPower(0.0, 0.0);
            drive.update();
            isFinished = true;
        }
    }
}