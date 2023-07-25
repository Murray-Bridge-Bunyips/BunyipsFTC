package org.firstinspires.ftc.teamcode.dinomighty.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.RobotConfig;

/**
 * DinoMighty robot configuration and hardware declarations.
 */

public class DinoConfig extends RobotConfig {

    //    front_servo = hardwareMap.get(Servo.class, "front_servo");
    //    gripper = hardwareMap.get(Servo.class, "gripper");
    //    right_front = hardwareMap.get(DcMotor.class, "right_front");
    //    right_rear = hardwareMap.get(DcMotor.class, "right_rear");
    //    arm = hardwareMap.get(DcMotor.class, "arm");
    //    left_front = hardwareMap.get(DcMotor.class, "left_front");
    //    left_rear = hardwareMap.get(DcMotor.class, "left_rear");

    // Declares all necessary motors
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public DcMotor liftMotor;

    @Override
    protected void init() {

        leftMotor = (DcMotor) getHardware("leftMotor", DcMotor.class);
        rightMotor = (DcMotor) getHardware("rightMotor", DcMotor.class);
        liftMotor = (DcMotor) getHardware("liftMotor", DcMotor.class);
    }

}
