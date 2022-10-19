package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.bosch.BNO055IMU;

/**
 * IMUOperation custom common class for internal BNO055IMUs
 * @author Lucas Bubner - FTC 15215 Captain; Oct 2022 - Murray Bridge Bunyips
 */
 public class IMUOp extends BunyipsComponent {

    private final BNO055IMU imu;

    public IMUOp(BunyipsOpMode opMode, BNO055IMU imu) {
        super(opMode);
        this.imu = imu;
    }

    // TODO
 }