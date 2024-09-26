package org.murraybridgebunyips.joker;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.murraybridgebunyips.bunyipslib.RobotConfig;

public class Joker extends RobotConfig {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    @Override
    protected void onRuntime() {
        frontLeft = getHardware("front_left", DcMotor.class);
        frontRight = getHardware("front_right", DcMotor.class);
        backLeft = getHardware("back_left", DcMotor.class);
        backRight = getHardware("back_right", DcMotor.class);
    }
}