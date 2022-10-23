package org.firstinspires.ftc.teamcode.common;


public class FieldPositioning extends BunyipsComponent {

    private final CameraOp cam;
    private final Deadwheel x, y;
    private final IMUOp imu;

    private int pX, pY, pZ, rX, rY, rZ;

    public FieldPositioning(BunyipsOpMode opMode, CameraOp cam, Deadwheel x, Deadwheel y, IMUOp imu) {
        super(opMode);
        this.cam = cam;
        this.x = x;
        this.y = y;
        this.imu = imu;
        pX = 0;
        pY = 0;
    }

    public void update() {
        int[] gXr = new int[2];
        int[] gYr = new int[2];
        int[] gZr = new int[2];

        if (cam.vuforiaEnabled && cam.targetVisible) {
            pX = (int) cam.getX();
            pY = (int) cam.getY();
            pZ = (int) cam.getZ();
            gXr[0] = (int) cam.getPitch();
            gYr[0] = (int) cam.getRoll();
            gZr[0] = (int) cam.getHeading();
        }

        gXr[1] = (int) imu.getPitch();
        gYr[1] = (int) imu.getRoll();
        gZr[1] = (int) imu.getHeading();

        // Prioritise Vuforia readings over IMU, unless Vuforia is not online/no target is seen
        rX = cam.targetVisible ? gXr[0] : gXr[1];
        rY = cam.targetVisible ? gYr[0] : gYr[1];
        rZ = cam.targetVisible ? gZr[0] : gZr[1];

        pX += x.getEncoderReading();
        pY += y.getEncoderReading();
    }
}
