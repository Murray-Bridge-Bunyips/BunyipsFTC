package org.firstinspires.ftc.teamcode.dinomighty.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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
    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;
    public DcMotor arm;

    public Servo frontServo;
    public Servo gripper;

    @Override
    protected void init() {

        frontLeft = (DcMotor) getHardware("left_front", DcMotor.class);
        backLeft = (DcMotor) getHardware("left_rear", DcMotor.class);
        frontRight = (DcMotor) getHardware("right_front", DcMotor.class);
        backRight = (DcMotor) getHardware("right_rear", DcMotor.class);
        arm = (DcMotor) getHardware("arm", DcMotor.class);

        frontServo = (Servo) getHardware("front_servo", Servo.class);
        gripper = (Servo) getHardware("gripper", Servo.class);

        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }

}
