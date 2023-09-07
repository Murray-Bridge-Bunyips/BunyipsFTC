package org.firstinspires.ftc.teamcode.wheatley.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

/**
 * Wheatley robot configuration and hardware declarations
 * <p>
 * NOTE: As of initial writing, we have nearly no idea what hardware specifications we will be using
 * This is almost all guessing and taking hints from new SDK features, mostly copied from DinoMighty
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyConfig extends RobotConfig {

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
    public WebcamName webCam;

    @Override
    protected void init() {

        // Motor directions configured to work with current config
        frontLeft = (DcMotor) getHardware("left_front", DcMotor.class);
        backLeft = (DcMotor) getHardware("left_rear", DcMotor.class);
        frontRight = (DcMotor) getHardware("right_front", DcMotor.class);
        backRight = (DcMotor) getHardware("right_rear", DcMotor.class);
        webCam = (WebcamName) getHardware("web_cam", WebcamName.class);
    }
}
