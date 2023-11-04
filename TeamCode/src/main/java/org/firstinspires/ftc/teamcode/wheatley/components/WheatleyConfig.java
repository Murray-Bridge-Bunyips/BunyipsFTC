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
    public DcMotor fl;

    // Expansion 0: bl
    public DcMotor bl;

    // Expansion 2: fr
    public DcMotor /*Are you*/ fr /*Or jk*/;

    // Expansion 3: br
    public DcMotor br;

    // Control 0: ra
    public DcMotor ra;

    // Control Servo 0: ls

    public DcMotor ls;

    // Control Servo 1: rs
    public DcMotor rs;

    // USB device "webcam"
    public WebcamName webcam;

    @Override
    protected void init() {

        // Motor directions configured to work with current config
        fl = (DcMotor) getHardware("fl", DcMotor.class);
        bl = (DcMotor) getHardware("bl", DcMotor.class);
        fr = (DcMotor) getHardware("fr", DcMotor.class);
        br = (DcMotor) getHardware("br", DcMotor.class);
        ls = (DcMotor) getHardware("ls", DcMotor.class);
        rs = (DcMotor) getHardware("rs", DcMotor.class);
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        ra = (DcMotor) getHardware("ra", DcMotor.class);
    }
}
