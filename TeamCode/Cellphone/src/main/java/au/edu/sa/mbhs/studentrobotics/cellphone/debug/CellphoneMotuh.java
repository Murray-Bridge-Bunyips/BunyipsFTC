package au.edu.sa.mbhs.studentrobotics.cellphone.debug;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.TrapezoidProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.ProfiledPIDController;
import au.edu.sa.mbhs.studentrobotics.cellphone.components.CellphoneConfig;

/**
 * Fake motor (motuh)
 */
@TeleOp
@Config
public class CellphoneMotuh extends BunyipsOpMode {
    /**
     * vroom setpoint
     */
    public static int SETPOINT = 0;
    /**
     * vroom amount
     */
    public static double kP = 0.01;
    /**
     * motuh direction innit
     */
    public static DcMotorSimple.Direction DIRECTION = DcMotorSimple.Direction.FORWARD;
    /**
     * the un-guhening
     */
    public static double ACC_LP_GAIN = 0.95;
    /**
     * vroom
     */
    public static double mVelo = 15;
    /**
     * vroom vroom
     */
    public static double mAccel = 8;

    private final CellphoneConfig phone = new CellphoneConfig();

    @Override
    protected void onInit() {
        phone.init();
        phone.dummy.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        phone.dummy.setRunToPositionController(new ProfiledPIDController(kP, 0, 0, new TrapezoidProfile.Constraints(mVelo, mAccel)));
        phone.dummy.getEncoder().setAccelLowPassGain(ACC_LP_GAIN);
        t.setMsTransmissionInterval(10);
    }

    @Override
    protected void activeLoop() {
        phone.dummy.setDirection(DIRECTION);
        phone.dummy.setTargetPosition(SETPOINT);
        phone.dummy.setPower(1);

        t.addData("power", phone.dummy.getPower());
        t.addData("setpoint", SETPOINT);
        t.addData("pos", phone.dummy.getCurrentPosition());
        t.addData("velo", phone.dummy.getVelocity());
        t.addData("accel", phone.dummy.getAcceleration());
    }
}
