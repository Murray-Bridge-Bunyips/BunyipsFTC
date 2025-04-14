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
        LeftDriveBase = getHardware("Left", DcMotor.class);
        RightDriveBase.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LeftDriveBase.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftDriveBase.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive = new SimpleTankDrive(Collections.singletonList(LeftDriveBase), Collections.singletonList(RightDriveBase));
    }
}
