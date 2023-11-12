package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;

/**
 * Mecanum Drive for Wheatley
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyMecanumDrive extends MecanumDrive {

    public WheatleyMecanumDrive(@NonNull BunyipsOpMode opMode, DcMotor frontLeft, DcMotor backLeft, DcMotor frontRight, DcMotor backRight) {
        super(opMode, frontLeft, backLeft, frontRight, backRight);
    }
}
