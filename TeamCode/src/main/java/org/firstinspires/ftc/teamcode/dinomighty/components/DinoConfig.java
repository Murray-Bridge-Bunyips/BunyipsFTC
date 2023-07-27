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
    public DcMotor leftFront;
    public DcMotor leftBack;
    public DcMotor rightFront;
    public DcMotor rightBack;
    public DcMotor arm;

    public Servo frontServo;
    public Servo gripper;

    @Override
    protected void init() {

        leftFront = (DcMotor) getHardware("leftFront", DcMotor.class);
        leftBack = (DcMotor) getHardware("leftBack", DcMotor.class);
        rightFront = (DcMotor) getHardware("rightFront", DcMotor.class);
        rightBack = (DcMotor) getHardware("rightBack", DcMotor.class);
        arm = (DcMotor) getHardware("arm", DcMotor.class);

        frontServo = (Servo) getHardware("frontServo", Servo.class);
        gripper = (Servo) getHardware("gripper", Servo.class);
    }

}
