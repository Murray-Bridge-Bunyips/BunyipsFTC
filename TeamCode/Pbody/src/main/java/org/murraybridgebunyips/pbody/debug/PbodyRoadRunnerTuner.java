package org.murraybridgebunyips.pbody.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.tuning.AutomaticFeedforwardTuner;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.tuning.ManualFeedforwardTuner;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.tuning.StraightTest;
import org.murraybridgebunyips.pbody.components.PbodyConfig;

@TeleOp(name="rr tuning")
public class PbodyRoadRunnerTuner extends StraightTest {
    private final PbodyConfig config = new PbodyConfig();

    @Override
    public void runOpMode() {
        config.init(this);
        drive = new MecanumRoadRunnerDrive(null, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
        super.runOpMode();
    }
}
