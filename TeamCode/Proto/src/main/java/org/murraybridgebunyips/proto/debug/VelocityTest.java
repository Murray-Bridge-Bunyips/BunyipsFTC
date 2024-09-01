package org.murraybridgebunyips.proto.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Dbg;
import org.murraybridgebunyips.bunyipslib.EncoderTicks;
import org.murraybridgebunyips.bunyipslib.Motor;
import org.murraybridgebunyips.proto.Proto;

/**
 * BunyipsLib velocity test
 */
@Config
@TeleOp
public class VelocityTest extends BunyipsOpMode {
    /**
     * Velocity target (deg/s)
     */
    public static double DEG_PER_SEC = 0;

    private final Proto robot = new Proto();
    private int tpr;
    private Motor motor;

    @Override
    protected void onInit() {
        robot.init();
        // TODO: assign motor to test
        motor = robot.backLeft;
        MotorConfigurationType mConf = motor.getMotorType();
        tpr = (int) mConf.getTicksPerRev();
        Dbg.log("[Motor Intrinsics] TPR:%, MAX_TPS:%, MAX_RPM_BUFF:%", tpr, mConf.getAchieveableMaxTicksPerSecond(), mConf.getAchieveableMaxRPMFraction());
    }

    @Override
    protected void activeLoop() {
        motor.setVelocity(DEG_PER_SEC, AngleUnit.DEGREES);
        telemetry.addData("angVel",
                EncoderTicks
                        .toAngle((int) Math.round(motor.getVelocity()), tpr, 1)
                        .in(Degrees)
        );
        telemetry.addData("angVelTarget", DEG_PER_SEC);
        telemetry.addData("tickVel", motor.getVelocity());
        telemetry.addData("tickVelTarget", EncoderTicks.fromAngle(Degrees.of(DEG_PER_SEC), tpr, 1));
    }
}
