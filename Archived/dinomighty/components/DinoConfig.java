package org.firstinspires.ftc.teamcode.dinomighty.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.RobotConfig;

/**
 * DinoMighty robot configuration and hardware declarations.
 */

public class DinoConfig extends RobotConfig {

    //    front_servo = hardwareMap.get(Servo.class, "front_servo");
    //    back_servo = hardwareMap.get(Servo.class, "back_servo")
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
    public Servo backServo;

    @Override
    protected void init() {

        frontLeft = (DcMotor) getHardware("left_front", DcMotor.class);
        backLeft = (DcMotor) getHardware("left_rear", DcMotor.class);
        frontRight = (DcMotor) getHardware("right_front", DcMotor.class);
        backRight = (DcMotor) getHardware("right_rear", DcMotor.class);
        arm = (DcMotor) getHardware("arm", DcMotor.class);

        frontServo = (Servo) getHardware("front_servo", Servo.class);
        backServo = (Servo) getHardware("back_servo", Servo.class);

        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Motor directions configured to work with current config
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }

}
