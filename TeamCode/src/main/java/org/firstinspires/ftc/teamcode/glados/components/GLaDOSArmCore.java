package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Encoder;
import org.firstinspires.ftc.teamcode.common.Pivot;

public class GLaDOSArmCore extends BunyipsComponent {
    private DcMotor motor;
    private Pivot pivot;
    private Servo endEffector;

    public GLaDOSArmCore(@NonNull BunyipsOpMode opMode, DcMotor motor, Servo endEffector) {
        super(opMode);
        this.motor = motor;
        this.pivot = new Pivot(opMode, motor, 0);
        this.endEffector = endEffector;

        // TODO: Reset physical arm to 0.0 degrees

        pivot.reset();
        pivot.track();
    }

    public double getPivotPosition() {
        return motor.getCurrentPosition();
    }

    public double getPivotAngle() {
        return pivot.getDegrees(Encoder.Scope.RELATIVE);
    }

    public double getEndEffectorPosition() {
        return endEffector.getPosition();
    }

    public void setEndEffectorPosition(double position) {
        endEffector.setPosition(position);
    }

    public void update() {
        // TODO
    }


}
