package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Collections;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleTankDrive;

public class IDK extends RobotConfig {
    public SimpleTankDrive drive;
    public DcMotor RightDriveBase;
    public DcMotor LeftDriveBase;

    @Override
    protected void onRuntime() {
        RightDriveBase = getHardware("Right", DcMotor.class);
        RightDriveBase.setDirection(DcMotorSimple.Direction.REVERSE);
        RightDriveBase.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LeftDriveBase = getHardware("Left", DcMotor.class);
        LeftDriveBase.setDirection(DcMotorSimple.Direction.FORWARD);
        LeftDriveBase.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive = new SimpleTankDrive(Collections.singletonList(LeftDriveBase), Collections.singletonList(RightDriveBase));
    }
}
