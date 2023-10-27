package org.firstinspires.ftc.teamcode.glados.components;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.PivotMotor;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

/**
 * FTC 15215 CENTERSTAGE 2023-2024 robot configuration
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSConfigCore extends RobotConfig {

    public WebcamName webcam;
    // Expansion 0: Front Left "fl"
    public DcMotorEx fl;
    // Expansion 1: Front Right "fr"
    public DcMotorEx fr;
    // Expansion 2: Back Right "br"
    public DcMotorEx br;
    // Expansion 3: Back Left "bl"
    public DcMotorEx bl;
    // Control 0: Suspender Actuator "sa"
    public DcMotorEx sa;
    // Control 1: Suspender Rotation "sr"
    public PivotMotor sr;

    public IMU imu;

    protected static final double SR_TICKS_PER_REV = 537.6;

    @Override
    protected void init() {
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        fl = (DcMotorEx) getHardware("fl", DcMotorEx.class);
        fr = (DcMotorEx) getHardware("fr", DcMotorEx.class);
        br = (DcMotorEx) getHardware("br", DcMotorEx.class);
        bl = (DcMotorEx) getHardware("bl", DcMotorEx.class);
        DcMotorEx SRmotor = (DcMotorEx) getHardware("sr", DcMotorEx.class);
        if (SRmotor != null) {
            sr = new PivotMotor(SRmotor, SR_TICKS_PER_REV);
        }
        sa = (DcMotorEx) getHardware("sa", DcMotorEx.class);
        imu = (IMU) getHardware("imu", IMU.class);

        if (fl != null) {
            // The forward left wheel goes the wrong way without us changing
            fl.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        // Explicitly set all other motors for easy debugging
        if (fr != null) {
            fr.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        if (br != null) {
            br.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        if (bl != null) {
            bl.setDirection(DcMotorSimple.Direction.FORWARD);
        }

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
