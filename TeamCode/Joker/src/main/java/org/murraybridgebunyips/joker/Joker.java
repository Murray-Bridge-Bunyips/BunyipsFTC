package org.murraybridgebunyips.joker;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.murraybridgebunyips.bunyipslib.RobotConfig;

public class Joker extends RobotConfig {
    /**
     * Expansion 1: front_left
     */
    public DcMotor frontLeft;
    /**
     * Expansion 2: front_right
     */
    public DcMotor frontRight;
    /**
     * Expansion 0: back_left
     */
    public DcMotor backLeft;
    /**
     * Expansion 3: back_right
     */
    public DcMotor backRight;
    /**
     * Control Hub 0: intakeMotor
     */
    public DcMotor intakeMotor;
    /**
     * Control Hub 1: liftMotor
     */
    public DcMotor liftMotor;

    /**
     * Control Hub 0: outtakeAlign
     */
    public Servo outtakeAlign;
    /**
     * Control Hub 1: outtakeGrip
     */
    public Servo outtakeGrip;
    /**
     * Control Hub 2: intakeGrip
     */
    public Servo intakeGrip;
    /**
     * Control Hub 3: lights
     */
    public RevBlinkinLedDriver lights;

    /**
     * Control Hub 0-1 (1 used): liftLimiter
     */
    public TouchSensor liftBotStop;
    /**
     * Control Hub 2-3 (3 used): intakeInStop
     */
    public TouchSensor intakeInStop;
    /**
     * Control Hub 4-5 (5 used): intakeOutStop
     */
    public TouchSensor intakeOutStop;
    /**
     * Control Hub 6-7 (7 used): handoverPoint
     */
    public TouchSensor handoverPoint;

    public static int INTAKE_GRIP_OPEN_POSITION = 1;
    public static int INTAKE_GRIP_CLOSED_POSITION = 0;

    public static int OUTTAKE_GRIP_OPEN_POSITION = 1;
    public static int OUTTAKE_GRIP_CLOSED_POSITION = 0;

    public static int OUTTAKE_ALIGN_IN_POSITION = 1;
    public static int OUTTAKE_ALIGN_OUT_POSITION = 0;

    private boolean intakeGripClosed = false;
    private boolean outtakeFacingOut = false;

    @Override
    protected void onRuntime() {
        frontLeft = getHardware("front_left", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        frontRight = getHardware("front_right", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        backLeft = getHardware("back_left", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        backRight = getHardware("back_right", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        intakeMotor = getHardware("intakeMotor", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        liftMotor = getHardware("liftMotor", DcMotor.class);
        outtakeAlign = getHardware("outtakeAlign", Servo.class);
        outtakeGrip = getHardware("outtakeGrip", Servo.class);
        intakeGrip = getHardware("intakeGrip", Servo.class);
        lights = getHardware("lights", RevBlinkinLedDriver.class);
        liftBotStop = getHardware("liftLimiter", TouchSensor.class);
        intakeInStop = getHardware("intakeInStop", TouchSensor.class);
        intakeOutStop = getHardware("intakeOutStop", TouchSensor.class);
        handoverPoint = getHardware("handoverPoint", TouchSensor.class);
    }

    public void toggleGrips() {
        if (intakeGripClosed) {
            intakeGrip.setPosition(INTAKE_GRIP_CLOSED_POSITION);
            outtakeGrip.setPosition(OUTTAKE_GRIP_OPEN_POSITION);
            intakeGripClosed = false;
        }
        else {
            intakeGrip.setPosition(INTAKE_GRIP_OPEN_POSITION);
            outtakeGrip.setPosition(OUTTAKE_GRIP_CLOSED_POSITION);
            intakeGripClosed = true;
        }
    }

    public void toggleOuttake() {
        if (outtakeFacingOut) {
            outtakeAlign.setPosition(OUTTAKE_ALIGN_IN_POSITION);
            outtakeFacingOut = false;
        }
        else {
            outtakeAlign.setPosition(OUTTAKE_ALIGN_OUT_POSITION);
            outtakeFacingOut = true;
        }
    }
}