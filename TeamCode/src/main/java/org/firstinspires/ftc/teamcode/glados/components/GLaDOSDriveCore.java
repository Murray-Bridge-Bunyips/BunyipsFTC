package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.FieldCentricMecanumDrive;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.RelativeVector;

public class GLaDOSDriveCore extends FieldCentricMecanumDrive {
    public GLaDOSDriveCore(@NonNull BunyipsOpMode opMode, DcMotor frontLeft, DcMotor backLeft, DcMotor frontRight, DcMotor backRight, IMUOp imu, RelativeVector startingDirection) {
        super(opMode, frontLeft, backLeft, frontRight, backRight, imu, startingDirection);
    }
}
