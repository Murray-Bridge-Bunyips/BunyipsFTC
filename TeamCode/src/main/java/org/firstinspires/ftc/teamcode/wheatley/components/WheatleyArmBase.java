package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Component that has common methods used by both the rigging and claw arms
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyArmBase extends BunyipsComponent {

    private Double armPower;

    public WheatleyArmBase(@NonNull BunyipsOpMode opMode, Double armPower) {
        super(opMode);
        this.armPower = armPower;
    }

    public void armLift(double gamepadPosition) {
        armPower = gamepadPosition;
    }
}
