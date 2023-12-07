package org.murraybridgebunyips.bunyipslib.personalitycore.submodules;

import static org.murraybridgebunyips.bunyipslib.Text.round;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsComponent;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;

/**
 * Horizontal CRServo motion for the GLaDOS/Wheatley robot
 * @author Lucas Bubner, 2023
 */
public class PersonalityCoreClawMover extends BunyipsComponent {
    private final CRServo servo;
    private double power;
    private double currentTimeout;
    private ElapsedTime timer;

    public PersonalityCoreClawMover(@NonNull BunyipsOpMode opMode, CRServo servo) {
        super(opMode);
        this.servo = servo;
    }

    public void actuateUsingController(double y) {
        if (currentTimeout != 0) return;
        this.power = Range.clip(-y, -1.0, 1.0);
    }

    public void actuateUsingDpad(boolean up, boolean down) {
        if (!up && !down) {
            this.power = 0;
            return;
        }
        this.power = up ? 1 : -1;
    }

    public void setPower(double power) {
        if (currentTimeout != 0) return;
        this.power = Range.clip(power, -1.0, 1.0);
    }

    public void runFor(double seconds, double power) {
        if (currentTimeout != 0) return;
        this.power = power;
        this.currentTimeout = seconds;
        timer.reset();
    }

    public void update() {
        servo.setPower(power);
        getOpMode().addTelemetry("Claw Horizontal: % power", round(servo.getPower(), 1));
        if (currentTimeout != 0) {
            getOpMode().addTelemetry(" - running for % seconds", currentTimeout);
            if (timer.seconds() >= currentTimeout) {
                currentTimeout = 0;
                power = 0;
            }
        }
    }
}
