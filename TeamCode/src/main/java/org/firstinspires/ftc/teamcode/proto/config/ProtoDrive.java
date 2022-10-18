package org.firstinspires.ftc.teamcode.proto.config;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;

import java.util.Locale;

public class ProtoDrive extends BunyipsComponent {

    private BunyipsOpMode opmode;
    final private DcMotorEx bl;
    final private DcMotorEx br;
    final private DcMotorEx fl;
    final private DcMotorEx fr;

    private double speedX = 0.0;
    private double speedY = 0.0;
    private double speedR = 0.0;


    public ProtoDrive(BunyipsOpMode opMode,
                      DcMotorEx bl, DcMotorEx br,
                      DcMotorEx fl, DcMotorEx fr) {
        super(opMode);
        this.bl = bl;
        this.br = br;
        this.fl = fl;
        this.fr = fr;
    }

    /**
     * Call to update motor speeds
     */
    public void update() {
        // Calculate motor powers
        double frontLeftPower = speedX + speedY - speedR;
        double frontRightPower = speedX - speedY + speedR;
        double backLeftPower = speedX - speedY - speedR;
        double backRightPower = speedX + speedY + speedR;

        double maxPower = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
        // If the maximum number is greater than 1.0, then normalise by that number
        if (maxPower > 1.0) {
            frontLeftPower = frontLeftPower / maxPower;
            frontRightPower = frontRightPower / maxPower;
            backLeftPower = backLeftPower / maxPower;
            backRightPower = backRightPower / maxPower;
        }

        fl.setPower(frontLeftPower);
        fr.setPower(frontRightPower);
        bl.setPower(backLeftPower);
        br.setPower(backRightPower);

        getOpMode().telemetry.addLine(String.format(Locale.getDefault(),"Mecanum Drive: Forward: %.2f, Strafe: %.2f, Rotate: %.2f", speedX, speedY, speedR));
    }

    /**
     * Set all motor speeds to zero
     */
    public void deinit() {
        this.setSpeedXYR(0, 0, 0);
        this.update();
    }

    /**
     * @param speedX relative north-south speed - positive: north
     * @param speedY relative east-west speed - positive: east
     * @param speedR rotation speed - positive: clockwise
     */
    public void setSpeedXYR(double speedX, double speedY, double speedR) {
        this.speedX = clipMotorPower(speedX);
        this.speedY = clipMotorPower(speedY);
        this.speedR = clipMotorPower(speedR);
    }

    private double clipMotorPower(double p) {
        return Range.clip(p, -1.0, 1.0);
    }

}
