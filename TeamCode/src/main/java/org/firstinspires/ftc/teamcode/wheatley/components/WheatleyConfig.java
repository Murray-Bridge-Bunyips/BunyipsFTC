package org.firstinspires.ftc.teamcode.wheatley.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

/**
 * Wheatley robot configuration and hardware declarations
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyConfig extends RobotConfig {

    // I'm not sure if this comment actually makes sense here but I'm keeping it here anyway
    //    front_servo = hardwareMap.get(Servo.class, "front_servo");
    //    back_servo = hardwareMap.get(Servo.class, "back_servo");
    //    gripper = hardwareMap.get(Servo.class, "gripper");
    //    right_front = hardwareMap.get(DcMotor.class, "right_front");
    //    right_rear = hardwareMap.get(DcMotor.class, "right_rear");
    //    arm = hardwareMap.get(DcMotor.class, "arm");
    //    left_front = hardwareMap.get(DcMotor.class, "left_front");
    //    left_rear = hardwareMap.get(DcMotor.class, "left_rear");

    // Declares all necessary motors

    // Expansion 1: fl
    public DcMotor frontLeft;

    // Expansion 0: bl
    public DcMotor backLeft;

    // Expansion 2: fr
    public DcMotor frontRight;

    // Expansion 3: br
    public DcMotor backRight;

    // Control 0: ra
    public DcMotor rotator;

    // Control Servo 0: ls

    public DcMotor leftServo;

    // Control Servo 1: rs
    public DcMotor rightServo;

    // USB device "webcam"
    public WebcamName webcam;

    @Override
    protected void init() {

        // Motor directions configured to work with current config
        frontLeft = (DcMotor) getHardware("fl", DcMotor.class);
        backLeft = (DcMotor) getHardware("bl", DcMotor.class);
        frontRight = (DcMotor) getHardware("fr", DcMotor.class);
        backRight = (DcMotor) getHardware("br", DcMotor.class);
        leftServo = (DcMotor) getHardware("ls", DcMotor.class);
        rightServo = (DcMotor) getHardware("rs", DcMotor.class);
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        rotator = (DcMotor) getHardware("ra", DcMotor.class);
    }
}
