package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Wheatley's suspender class.
 * Making me feel like Suspender was kind of pointless.
 *
 * <p></p>
 * "Okay, listen, let me lay something on you here. It's pretty heavy.
 * They told me NEVER NEVER EVER to disengage myself from my Management Rail. Or I would DIE.
 * But we're out of options here.
 * So... get ready to catch me, alright,
 * on the off chance that I'm not dead the moment I pop off this thing."
 *
 * @author Lachlan Paul, 2023
 */
public class WheatleyManagementRail extends BunyipsComponent {
    private final DcMotor extension;
    private int suspenderPos;
    private double suspenderPower;
    private Boolean stowed = true;

    // Frustration, is getting bigger,
    // bang, bang, bang,
    private final Servo /*pull my devil*/ trigger;
    public WheatleyManagementRail(@NonNull BunyipsOpMode opMode, DcMotor extension, Servo trigger) {
        super(opMode);
        this.extension = extension;
        this.trigger = trigger;

        // Default position when holding Suspender
        trigger.setPosition(90.0);
    }

    /**
     * Release the Suspender
     */
    public void release() {
        trigger.setPosition(0.0);
        stowed = false;
    }

    public void hookArm(double gamepadPosition) {
        if (!stowed) {
            suspenderPower = gamepadPosition;
        }
    }

    public void update() {
        extension.setPower(suspenderPower);

        getOpMode().addTelemetry("Suspender Arm is stowed:" + stowed);
        getOpMode().addTelemetry("Suspender Arm Position:" + extension.getCurrentPosition());
    }
}
