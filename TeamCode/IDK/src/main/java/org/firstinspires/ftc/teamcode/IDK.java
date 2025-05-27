package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Collections;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleTankDrive;

public class IDK extends RobotConfig {
    public SimpleTankDrive drive;
    public DcMotor RightDriveBase;
    public DcMotor LeftDriveBase;

    public DcMotor leftLift;
    public DcMotor rightLift;

    public HoldableActuator rotator;
    public Switch claw;

    @Override
    protected void onRuntime() {
        RightDriveBase = getHardware("Right", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        LeftDriveBase = getHardware("Left", DcMotor.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));
        drive = new SimpleTankDrive(Collections.singletonList(LeftDriveBase), Collections.singletonList(RightDriveBase))
                .withName("Drive");
        leftLift = getHardware("l_lift", DcMotor.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        rightLift = getHardware("r_lift", DcMotor.class);
        rotator = new HoldableActuator(getHardware("rotator", Motor.class, (d) -> d.setRunToPositionController(new PController(0.01))))
                .withUserSetpointControl((dt) -> 100 * dt)
                .withName("Rotator");
        claw = new Switch(getHardware("claw", Servo.class, (d) -> d.scaleRange(0, 0.5)))
                .withName("Claw");
    }
}
