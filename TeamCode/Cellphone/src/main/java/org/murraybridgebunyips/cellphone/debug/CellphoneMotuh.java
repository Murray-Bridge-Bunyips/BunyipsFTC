package org.murraybridgebunyips.cellphone.debug;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.external.pid.PController;
import org.murraybridgebunyips.cellphone.components.CellphoneConfig;

/**
 * Fake motor (motuh)
 */
@TeleOp
@Config
public class CellphoneMotuh extends BunyipsOpMode {
    /** vroom setpoint */
    public static int SETPOINT = 0;
    /** vroom amount */
    public static double kP = 0.01;
    /** motuh direction innit */
    public static DcMotorSimple.Direction DIRECTION = DcMotorSimple.Direction.FORWARD;
    /** the un-guhening */
    public static double ACC_LP_GAIN = 0.95;

    private final CellphoneConfig phone = new CellphoneConfig();

    @Override
    protected void onInit() {
        phone.init();
        phone.dummy.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        phone.dummy.setRunToPositionController(new PController(kP));
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
