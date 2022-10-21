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
    }

    public void update() {
        int[] gX = new int[3];
        int[] gY = new int[3];
        int[] gZ = new int[3];
        int[] gXr = new int[3];
        int[] gYr = new int[3];
        int[] gZr = new int[3];

        if (cam.vuforiaEnabled && cam.targetVisible) {
            gX[0] = (int) cam.getX();
            gY[0] = (int) cam.getY();
            gZ[0] = (int) cam.getZ();
            gXr[0] = (int) cam.getPitch();
            gYr[0] = (int) cam.getRoll();
            gZr[0] = (int) cam.getHeading();
        }

        gXr[1] = (int) imu.getPitch();
        gYr[1] = (int) imu.getRoll();
        gZr[1] = (int) imu.getHeading();
    }

}
