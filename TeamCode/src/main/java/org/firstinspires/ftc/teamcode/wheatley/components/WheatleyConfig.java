package org.firstinspires.ftc.teamcode.wheatley.components;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.MecanumCoefficients;

/**
 * Wheatley robot configuration and hardware declarations
 *
 * @author Lucas Bubner, 2023
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
    public DcMotorEx fl;

    // Expansion 0: bl
    public DcMotorEx bl;

    // Expansion 2: fr
    public DcMotorEx /*Are you*/ fr /*Or jk*/;

    // Expansion 3: br
    public DcMotorEx br;

    // Control 0: ra
    // Rotation Arm: Arm's rotation motor
    public DcMotorEx ra;

    // Control 1: susMotor
    // Suspender Extension: Suspender extension motor
    public DcMotorEx susMotor;

    // Control Servo 1: ls
    // Left Servo: Left Claw
    public Servo ls;

    // Control Servo 0: rs
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

    public DriveConstants driveConstants;
    public MecanumCoefficients mecanumCoefficients;

    @Override
    protected void configureHardware() {

        // Motor directions configured to work with current config
        fl = (DcMotorEx) getHardware("fl", DcMotorEx.class);
        bl = (DcMotorEx) getHardware("bl", DcMotorEx.class);
        fr = (DcMotorEx) getHardware("fr", DcMotorEx.class);
        br = (DcMotorEx) getHardware("br", DcMotorEx.class);
        ls = (Servo) getHardware("ls", Servo.class);
        rs = (Servo) getHardware("rs", Servo.class);
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        ra = (DcMotorEx) getHardware("ra", DcMotorEx.class);
        susMotor = (DcMotorEx) getHardware("susMotor", DcMotorEx.class);
        susServo = (Servo) getHardware("susServo", Servo.class);
        pl = (Servo) getHardware("pl", Servo.class);
        imu = (IMU) getHardware("imu", IMU.class);

        if (fr != null)
            fr.setDirection(DcMotorSimple.Direction.FORWARD);

        if (fl != null)
            fl.setDirection(DcMotorSimple.Direction.REVERSE);

        if (br != null)
            br.setDirection(DcMotorSimple.Direction.REVERSE);

        if (bl != null)
            bl.setDirection(DcMotorSimple.Direction.REVERSE);

        if (ra != null)
            ra.setDirection(DcMotorSimple.Direction.REVERSE);

        if (imu == null) {
            // huh
            throw new RuntimeException("IMU is null?");
        }

        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                        )
                )
        );

        // TODO: Tune
        driveConstants = new DriveConstants.Builder()
                .setTicksPerRev(537.6)
                .setMaxRPM(312.5)
                .setRunUsingEncoder(false)
                .setWheelRadius(1.4763)
                .setGearRatio(1)
                .setTrackWidth(18)
                // ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.85
                .setMaxVel(41.065033847087705)
                .setMaxAccel(41.065033847087705)
                .setMaxAngVel(Math.toRadians(130.71406249999998))
                .setMaxAngAccel(Math.toRadians(130.71406249999998))
                .build();

        mecanumCoefficients = new MecanumCoefficients.Builder()
                .build();
    }
}
