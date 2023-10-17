package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Component for the movement for both of the parts of Wheatley's rigging arm
 * Rigging arm being the arm used to hang Wheatley during the endgame
 * We use rigging instead of hanging because we don't want to say "Hanging the robot"
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyRigArm extends WheatleyArmBase {

    public WheatleyRigArm(@NonNull BunyipsOpMode opMode, Double armPower) {
        super(opMode, armPower);
    }
}
