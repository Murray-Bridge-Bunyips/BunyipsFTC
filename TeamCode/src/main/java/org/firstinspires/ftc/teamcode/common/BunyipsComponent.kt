package org.firstinspires.ftc.teamcode.common;

/**
 * Base class for components used in BunyipsOpModes
 */
public class BunyipsComponent {

    private final BunyipsOpMode opMode;

    public BunyipsComponent(BunyipsOpMode opMode) {
        this.opMode = opMode;
    }

    protected BunyipsOpMode getOpMode() {
        return opMode;
    }
}
