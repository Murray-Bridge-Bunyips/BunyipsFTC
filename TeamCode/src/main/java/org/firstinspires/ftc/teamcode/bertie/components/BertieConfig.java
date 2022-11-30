package org.firstinspires.ftc.teamcode.bertie.components;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
public class BertieConfig extends RobotConfig {

    public DcMotorEx backLeft;
    public DcMotorEx backRight;
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx armMotor;
    public CRServo spinIntake;
    public CRServo carouselLeft;
    public CRServo carouselRight;

    public static BertieConfig newConfig(HardwareMap hardwareMap, Telemetry telemetry) {
        BertieConfig config = new BertieConfig();
        config.init(hardwareMap, telemetry);
        return config;
    }

    @Override
    protected void init(HardwareMap hardwareMap, Telemetry telemetry) {
        setTelemetry(telemetry);

        backLeft = (DcMotorEx) getHardwareOn("Back Left", hardwareMap.dcMotor);
        backRight = (DcMotorEx) getHardwareOn("Back Right", hardwareMap.dcMotor);
        frontLeft = (DcMotorEx) getHardwareOn("Front Left", hardwareMap.dcMotor);
        frontRight = (DcMotorEx) getHardwareOn("Front Right", hardwareMap.dcMotor);
        armMotor = (DcMotorEx) getHardwareOn("Arm Motor", hardwareMap.dcMotor);
        carouselLeft = (CRServo) getHardwareOn("Carousel Left", hardwareMap.crservo);
        carouselRight = (CRServo) getHardwareOn("Carousel Right", hardwareMap.crservo);
        spinIntake = (CRServo) getHardwareOn("Spin Intake", hardwareMap.crservo);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }
}
