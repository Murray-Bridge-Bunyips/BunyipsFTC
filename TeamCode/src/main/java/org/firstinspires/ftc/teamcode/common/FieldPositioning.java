package org.firstinspires.ftc.teamcode.common;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.Deadwheel;

/**
 * Implementation class to control and monitor field positioning data through all available means
 * of sensor input, including deadwheels, IMU, and camera readings.
 * @author Lucas Bubner; FTC 15215 Dec 2022
 */
public class FieldPositioning extends BunyipsComponent {
    
    private final Deadwheel x, y;
    private final CameraOp cam;
    private final IMUOp imu;

    public FieldPositioning(BunyipsOpMode opMode, Deadwheel x, Deadwheel y, CameraOp cam, IMUOp imu) {
        super(opMode);
        this.x = x;
        this.y = y;
        this.cam = cam;
        this.imu = imu;
    }
    
    /**
     * Update matricies and data with newest information
     */
    public void update() {}


}
