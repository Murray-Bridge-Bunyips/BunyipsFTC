package org.murraybridgebunyips.glados.debug;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.subsystems.IMUOp;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * test IMUOp (new)
 */
@Config
@TeleOp
public class GLaDOSTestIMU extends BunyipsOpMode {
    /** yaw domain for imu readings */
    public static IMUOp.YawDomain YAW_DOMAIN = IMUOp.YawDomain.SIGNED;

    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    private IMUOp imu;

    @Override
    protected void onInit() {
        config.init();
        imu = new IMUOp(config.imu);
    }

    @Override
    protected void activeLoop() {
        imu.setYawDomain(YAW_DOMAIN);
        imu.update();

        telemetry.add(imu.yaw);
    }
}
