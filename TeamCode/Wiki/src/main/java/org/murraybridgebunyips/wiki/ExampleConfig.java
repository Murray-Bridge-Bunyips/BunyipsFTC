package org.murraybridgebunyips.wheatley.components;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.murraybridgebunyips.bunyipslib.RobotConfig;

/**
 * An example config, with recommended documentation practices.
 *
 * @author Your Name Here, YYYY
 */
public class ExampleConfig extends RobotConfig {
    /**
     * USB: Webcam "webcam"
     */
    public WebcamName webcam;

    /**
     * Control 0: front_left
     * This is the port it's plugged into and the name of the motor it's saved under in the driver hub.
     * The variable name and the name saved in the driver hub can be different.
     */
    public DcMotorEx front_left;

    /**
     * Control 1: front_right
     */
    public DcMotorEx front_right;

    /**
     * Control 2: back_left
     */
    public DcMotorEx back_left;

    /**
     * Control 3: back_right
     */
    public DcMotorEx back_right;

    /**
     * Expansion 0: arm
     */
    public DcMotorEx arm;

    /**
     * Control Servo 0: left_claw
     */
    public Servo left_claw;

    /**
     * Control Servo 1: right_claw
     */
    public Servo right_claw;

    @Override
    protected void onRuntime() {
        // This is where all the variables we just declared are assigned to actual physical components.

        // The string in here should be the name that the component is assigned in the driver hub.
        front_left = getHardware("front_left", DcMotorEx.class, (d) -> {
            // This is where you change settings based on your needs for each component
            // For example, this team needs their front left wheel to have a reversed direction
            // These will differ based on your team's needs and your robot's build
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });
        front_right = getHardware("front_right", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });
        back_left = getHardware("back_left", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });
        back_right = getHardware("back_right", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        arm = getHardware("arm", DcMotorEx.class, (d) -> {
            // Doing this prevents the arm from moving while there is no power being supplied.
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        // Defining servos.
        left_claw = getHardware("left_claw", Servo.class);
        // Depending on your hardware, you may need to set the range at which the servo goes manually.
        right_claw = getHardware("right_claw", Servo.class, (d) -> d.scaleRange(0.2, 1.0));
    }
}