package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.While;

/**
 * Pixel Movement arm mechanism
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSPixelCore extends BunyipsComponent {
    private final Servo endEffector;
    private DcMotor motor;
    private final While resetLoop = new While(
            () -> motor.getCurrentPosition() > 0,
            () -> motor.setPower(-0.5),
            () -> motor.setPower(0.0),
            4
    );

    public GLaDOSPixelCore(@NonNull BunyipsOpMode opMode, DcMotor motor, Servo endEffector) {
        super(opMode);
        this.endEffector = endEffector;
        reset();
    }

    public double getPivotPosition() {
        return motor.getCurrentPosition();
    }

    public double getEndEffectorPosition() {
        return endEffector.getPosition();
    }

    public void setEndEffectorPosition(double position) {
        endEffector.setPosition(position);
    }

    public void reset() {

    }

    public void update() {
        // TODO: Implement once we get a physical arm to work with
    }


}
