package org.firstinspires.ftc.teamcode.wheatley.components;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

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
    // Rotation Arm: Arm's rotation motor
    public DcMotor ra;

    // Control 1: susMotor
    // Suspender Extension: Suspender extension motor
    public DcMotor susMotor;

    // Control Servo 0: ls
    // Left Servo: Left Claw
    public Servo ls;

    // Control Servo 1: rs
    // Right Servo: Right Claw
    public Servo rs;

    // Control Servo 2: sus (why did they name it this)
    // Suspension Trigger
    public Servo susServo;

    // Control Servo 3: pl
    // Prolong: The thingo that launches the paper plane
    public Servo pl;

    // USB device "webcam"
    public WebcamName webcam;

    // Internally mounted on I2C C0 "imu"
    public IMU imu;

    @Override
    protected void init() {

        // Motor directions configured to work with current config
        fl = (DcMotor) getHardware("fl", DcMotor.class);
        bl = (DcMotor) getHardware("bl", DcMotor.class);
        fr = (DcMotor) getHardware("fr", DcMotor.class);
        br = (DcMotor) getHardware("br", DcMotor.class);
        ls = (Servo) getHardware("ls", Servo.class);
        rs = (Servo) getHardware("rs", Servo.class);
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        ra = (DcMotor) getHardware("ra", DcMotor.class);
        susMotor = (DcMotor) getHardware("susMotor", DcMotor.class);
        susServo = (Servo) getHardware("susServo", Servo.class);
        pl = (Servo) getHardware("pl", Servo.class);
        imu = (IMU) getHardware("imu", IMU.class);

        // This is because the fr motor was going the wrong way
        if (fr != null)
            fr.setDirection(DcMotorSimple.Direction.REVERSE);

        if (imu == null) {
            return;
        }

        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                        )
                )
        );
    }
}
