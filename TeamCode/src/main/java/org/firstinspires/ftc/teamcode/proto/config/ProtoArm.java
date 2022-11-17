package org.firstinspires.ftc.teamcode.proto.config;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class ProtoArm extends BunyipsComponent {

    private static final int[] LIFT_POSITIONS = {0, 100, 500, 1000, 1300};

    private int liftIndex = 0;
    private double liftPower;
    public CRServo claw1;
    public CRServo claw2;
//    public DcMotorEx arm1;
//    public DcMotorEx arm2;

//    public ProtoArm(BunyipsOpMode opMode, CRServo claw1, CRServo claw2, DcMotorEx arm1, DcMotorEx arm2) {
    public ProtoArm(BunyipsOpMode opMode, CRServo claw1, CRServo claw2) {
        super(opMode);
//        this.arm1 = arm1;
//        this.arm2 = arm2;
        this.claw1 = claw1;
        this.claw2 = claw2;
        liftPower = 0;

        // Ensure motors exist onboard
//        assert claw1 != null && claw2 != null && arm1 != null && arm2 != null;

        claw1.setDirection(CRServo.Direction.REVERSE);
        claw2.setDirection(CRServo.Direction.REVERSE);

//        arm1.setDirection(DcMotorEx.Direction.FORWARD);
//        arm2.setDirection(DcMotorEx.Direction.FORWARD);
//        arm1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
//        arm2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
//        arm1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        arm2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        arm1.setTargetPosition(0);
//        arm2.setTargetPosition(0);
    }

    /**
     * Set claw motor speed
     * @param speed desired speed (-1, 0, 1)
     */
    public void clawRun(double speed) {
        claw1.setPower(speed);
        claw2.setPower(speed);
        getOpMode().telemetry.addLine("Claws are running at power: " + claw1.getPower() + " : " + claw2.getPower());
    }

    /**
     * Lift arm motor up by one index of the LIFT_POSITIONS index
     */
//    public void liftUp() {
//        liftIndex++;
//        if (liftIndex > LIFT_POSITIONS.length - 1) {
//            liftIndex = LIFT_POSITIONS.length - 1;
//        }
//        arm1.setTargetPosition(LIFT_POSITIONS[liftIndex]);
//        arm2.setTargetPosition(LIFT_POSITIONS[liftIndex]);
//    }


    /**
     * Lift arm motor down by one index of the LIFT_POSITIONS index
     */
//    public void liftDown() {
//        liftIndex--;
//        if (liftIndex < 0) {
//            liftIndex = 0;
//        }
//        arm1.setTargetPosition(LIFT_POSITIONS[liftIndex]);
//        arm2.setTargetPosition(LIFT_POSITIONS[liftIndex]);
//    }


    /**
     * Set arm motor to index 0 of the LIFT_POSITIONS index (lowest position)
     */
//    public void liftReset() {
//        liftIndex = 0;
//        arm1.setTargetPosition(LIFT_POSITIONS[0]);
//        arm2.setTargetPosition(LIFT_POSITIONS[0]);
//    }


    /**
     * Set arm power for positional movements
     * @param power desired power
     */
    public void liftSetPower(double power) {
        liftPower = power;
    }


    /**
     * Call to update desired arm motor speeds
     */
    @SuppressLint("DefaultLocale")
    public void update() {
//        getOpMode().telemetry.addLine(String.format("Arm1 Position: %d", arm1.getCurrentPosition()));
//        getOpMode().telemetry.addLine(String.format("Arm2 Position: %d", arm2.getCurrentPosition()));
//        arm1.setPower(liftPower);
//        arm2.setPower(liftPower);
//        arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

}
