package org.firstinspires.ftc.teamcode.bertie_independent;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Disabled // May soon be deprecated by 'bertie' package
@TeleOp(name = "<BERTIE-I> Autonomous Blue")
public class BertieAutonomousBlue extends LinearOpMode {
    @Override
    public void runOpMode() {

        // Map hardware
        DcMotorEx armMotor = hardwareMap.get(DcMotorEx.class, "Arm Motor");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "Front Right");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "Back Right");
        CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "Front Left");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "Back Left");
        CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
        CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");

        // TODO: Autonomous code for blue and red
    }
}
