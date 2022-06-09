package org.firstinspires.ftc.teamcode;
 
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

// Uses Extended DcMotor class

@SuppressWarnings("unused")
@TeleOp(name = "Motors - Max Velocity")
public class ConceptMotorVelocity extends LinearOpMode {
    double backLeftcurrentVelocity;
    double backLeftmaxVelocity = 0.0;
    double frontLeftcurrentVelocity;
    double frontLeftmaxVelocity = 0.0;
    double backRightcurrentVelocity;
    double backRightmaxVelocity = 0.0;
    double frontRightcurrentVelocity;
    double frontRightmaxVelocity = 0.0;
    
    @SuppressWarnings("unused")
    @Override
    public void runOpMode() {
        
        // Map hardware
        DcMotorEx armMotor = hardwareMap.get(DcMotorEx.class, "Arm Motor"); // Not using
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "Front Right");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "Back Right");
        CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake"); // Not using
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "Front Left");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "Back Left");
        CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right"); // Not using
        CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left"); // Not using

        telemetry.addData("WARNING"," ALL MOVEMENT MOTORS WILL ENGAGE AT FULL SPEED WHEN STARTED!!");
        telemetry.addData("!!!", " SET ROBOT ON STAND BEFORE STARTING, AND USE A CHARGED BATTERY.");
        telemetry.update();
        waitForStart();

        // Start all motors
        backLeft.setPower(1); 
        frontLeft.setPower(1);
        backRight.setPower(1);
        frontRight.setPower(1);

        while (opModeIsActive()) {
            backLeftcurrentVelocity = backLeft.getVelocity();
            backRightcurrentVelocity = backRight.getVelocity();
            frontLeftcurrentVelocity = frontLeft.getVelocity();
            frontRightcurrentVelocity = frontRight.getVelocity();
        
            if (backLeftcurrentVelocity > backLeftmaxVelocity) {
                backLeftmaxVelocity = backLeftcurrentVelocity;
            }

            if (backRightcurrentVelocity > backRightmaxVelocity) {
                backRightmaxVelocity = backRightcurrentVelocity;
            }

            if (frontLeftcurrentVelocity > frontLeftmaxVelocity) {
                frontLeftmaxVelocity = frontLeftcurrentVelocity;
            }

            if (frontRightcurrentVelocity > frontRightmaxVelocity) {
                frontRightmaxVelocity = frontRightcurrentVelocity;
            }
            
            telemetry.addData("backLeftcurrentVelocity", backLeftcurrentVelocity);
            telemetry.addData("backLeftmaxVelocity", backLeftmaxVelocity);
            telemetry.addData("backRightcurrentVelocity", backRightcurrentVelocity);
            telemetry.addData("backRightmaxVelocity", backRightmaxVelocity);
            telemetry.addData("frontLeftcurrentVelocity", frontLeftcurrentVelocity);
            telemetry.addData("frontLeftmaxVelocity", frontLeftmaxVelocity);
            telemetry.addData("frontRightcurrentVelocity", backLeftcurrentVelocity);
            telemetry.addData("frontRightmaxVelocity", frontRightmaxVelocity);
            telemetry.update();
        }
        backLeft.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
    }
}
