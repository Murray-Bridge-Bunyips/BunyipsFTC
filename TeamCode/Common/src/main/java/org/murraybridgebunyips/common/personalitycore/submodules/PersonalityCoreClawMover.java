package org.murraybridgebunyips.common.personalitycore.submodules;

import static org.murraybridgebunyips.bunyipslib.Text.round;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;

/**
 * Horizontal CRServo motion for the GLaDOS/Wheatley robot
 *
 * @author Lucas Bubner, 2023
 */
public class PersonalityCoreClawMover extends BunyipsSubsystem {
    private final CRServo servo;
    private double power;
    private double currentTimeout;
    private final ElapsedTime timer = new ElapsedTime();

    public PersonalityCoreClawMover(CRServo servo) {
        this.servo = servo;
    }

    public PersonalityCoreClawMover actuateUsingController(double y) {
        if (currentTimeout != 0) return this;
        power = Range.clip(-y, -1.0, 1.0);
        return this;
    }

    public PersonalityCoreClawMover actuateUsingDpad(boolean up, boolean down) {
        if (!up && !down) {
            power = 0;
            return this;
        }
        power = up ? 1 : -1;
        return this;
    }

    public PersonalityCoreClawMover setPower(double power) {
        if (currentTimeout != 0) return this;
        this.power = Range.clip(power, -1.0, 1.0);
        return this;
    }

    public PersonalityCoreClawMover runFor(double seconds, double power) {
        if (currentTimeout != 0) return this;
        this.power = power;
        currentTimeout = seconds;
        timer.reset();
        return this;
    }

    @Override
    public void update() {
        servo.setPower(power);
        opMode.addTelemetry("Claw Horizontal: % power", round(servo.getPower(), 1));
        if (currentTimeout != 0) {
            opMode.addTelemetry(" - running for % seconds", currentTimeout);
            if (timer.seconds() >= currentTimeout) {
                currentTimeout = 0;
                power = 0;
            }
        }
    }
}
