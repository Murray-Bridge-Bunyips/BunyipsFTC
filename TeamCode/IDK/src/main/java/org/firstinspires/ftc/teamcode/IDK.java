package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Collections;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleTankDrive;

public class IDK extends RobotConfig {
    public SimpleTankDrive drive;
    public Switch Rotator;
    public Switch claw;
    public HoldableActuator lift;

    @Override
    protected void onRuntime() {
        DcMotor rightDrive = getHardware("right", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        DcMotor leftDrive = getHardware("left", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        drive = new SimpleTankDrive(Collections.singletonList(leftDrive), Collections.singletonList(rightDrive))
                .withName("Drive");

        Servo rotatorServo = getHardware("rotator", Servo.class);
        Servo clawServo = getHardware("claw", Servo.class);
        clawServo.scaleRange(0, 0.5);
        DcMotor liftmotor = getHardware("lift",DcMotor.class);
        liftmotor.setDirection(DcMotorSimple.Direction.REVERSE);

        Rotator = new Switch(rotatorServo).withName("Rotator");
        claw = new Switch(clawServo).withName("Claw");
        lift = new HoldableActuator(liftmotor).withName("Lift");
    }
}
