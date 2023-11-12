package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

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
 * @author Lucas Bubner, 2023
 */
public class WheatleyManagementRail extends BunyipsComponent {
    private final DcMotor extension;
    // Frustration, is getting bigger,
    // bang, bang, bang,
    private final Servo /*pull my devil*/ trigger;
    private int suspenderTarget;
    private double triggerTarget;

    private static final int MAX_TICKS = 3000;
    private static final double PWR = 1.0;

    private static final double ARMED = 1.0;
    private static final double OPEN = 0.0;

    public WheatleyManagementRail(@NonNull BunyipsOpMode opMode, DcMotor extension, Servo trigger) {
        super(opMode);
        this.extension = extension;
        this.trigger = trigger;

        triggerTarget = ARMED;
        update();

        // Assumes extension arm is set at zero
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setTargetPosition(0);
        extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // will not dispatch a motor update as it is in RUN_TO_POSITION
        extension.setPower(PWR);
    }

    /**
     * Release the Suspender
     */
    public void release() {
        triggerTarget = OPEN;
    }

    /**
     * Return trigger to stowed
     */
    public void reset() {
        triggerTarget = ARMED;
    }

    public void hookArm(double gamepadPosition) {
        if (trigger.getPosition() == OPEN) {
            suspenderTarget -= gamepadPosition * 10;
        }
        suspenderTarget = Range.clip(suspenderTarget, 0, MAX_TICKS);
    }

    public void update() {
        extension.setTargetPosition(suspenderTarget);
        trigger.setPosition(triggerTarget);
        getOpMode().addTelemetry("Suspender: %, % ticks", trigger.getPosition() == OPEN ? "OPEN" : "ARMED", extension.getCurrentPosition());
    }
}
