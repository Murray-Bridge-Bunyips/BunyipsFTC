package org.firstinspires.ftc.teamcode;
 
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

// Uses Extended DcMotor class

@SuppressWarnings("unused")
@TeleOp(name = "Test - Drivetrain PIDF")
public class TestMotorVelocity extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    double backLeftcurrentVelocity;
    double backLeftmaxVelocity = 0.0;
    double frontLeftcurrentVelocity;
    double frontLeftmaxVelocity = 0.0;
    double backRightcurrentVelocity;
    double backRightmaxVelocity = 0.0;
    double frontRightcurrentVelocity;
    double frontRightmaxVelocity = 0.0;
    double finalBackLeftMVeloc;
    double finalBackRightMVeloc;
    double finalFrontLeftMVeloc;
    double finalFrontRightMVeloc;
    
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

        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);

        telemetry.addData("WARNING"," MOVEMENT MOTORS WILL ENGAGE AT FULL SPEED WHEN STARTED!!");
        telemetry.addData("!!!", " SET ROBOT ON STAND BEFORE STARTING, AND USE A CHARGED BATTERY.");
        telemetry.update();
        waitForStart();
        
        // Start back left motor
        runtime.reset();
        backLeft.setPower(1);
        while (runtime.seconds() < 4) {
            backLeftcurrentVelocity = backLeft.getVelocity();
           /* backRightcurrentVelocity = backRight.getVelocity();
            frontLeftcurrentVelocity = frontLeft.getVelocity();
            frontRightcurrentVelocity = frontRight.getVelocity(); */
            if (backLeftcurrentVelocity > backLeftmaxVelocity) {
                backLeftmaxVelocity = backLeftcurrentVelocity;
            }

         /*   if (backRightcurrentVelocity > backRightmaxVelocity) {
                backRightmaxVelocity = backRightcurrentVelocity;
            }

            if (frontLeftcurrentVelocity > frontLeftmaxVelocity) {
                frontLeftmaxVelocity = frontLeftcurrentVelocity;
            }

            if (frontRightcurrentVelocity > frontRightmaxVelocity) {
                frontRightmaxVelocity = frontRightcurrentVelocity;
            } */
            
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("backLeftcurrentVelocity", backLeftcurrentVelocity);
            telemetry.addData("backLeftmaxVelocity", backLeftmaxVelocity);
           /* telemetry.addData("backRightcurrentVelocity", backRightcurrentVelocity);
            telemetry.addData("backRightmaxVelocity", backRightmaxVelocity);
            telemetry.addData("frontLeftcurrentVelocity", frontLeftcurrentVelocity);
            telemetry.addData("frontLeftmaxVelocity", frontLeftmaxVelocity);
            telemetry.addData("frontRightcurrentVelocity", frontRightcurrentVelocity);
            telemetry.addData("frontRightmaxVelocity", frontRightmaxVelocity);
            telemetry.update(); */
        }

        finalBackLeftMVeloc = backLeftmaxVelocity;
        finalBackRightMVeloc = backRightmaxVelocity;
        finalFrontLeftMVeloc = frontLeftmaxVelocity;
        finalFrontRightMVeloc = frontRightmaxVelocity;

        backLeft.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);

        telemetry.addData("TEST COMPLETE", "PIDF CALCULATED.");
        telemetry.addData("Front Left Max Velocity", finalFrontLeftMVeloc);
        telemetry.addData("PIDF Front Left", "(" + 0.1*(32767/finalFrontLeftMVeloc) + ", " + 0.1*(0.1*(32767/finalFrontLeftMVeloc)) + ", " + "0" + ", " + (32767/finalFrontLeftMVeloc) + ")");
        telemetry.addData("Front Right Max Velocity", finalFrontRightMVeloc);
        telemetry.addData("PIDF Front Right", "(" + 0.1*(32767/finalFrontRightMVeloc) + ", " + 0.1*(0.1*(32767/finalFrontRightMVeloc)) + ", " + "0" + ", " + (32767/finalFrontRightMVeloc) + ")");
        telemetry.addData("Back Left Max Velocity", finalBackLeftMVeloc);
        telemetry.addData("PIDF Back Left", "(" + 0.1*(32767/finalBackLeftMVeloc) + ", " + 0.1*(0.1*(32767/finalBackLeftMVeloc)) + ", " + "0" + ", " + (32767/finalBackLeftMVeloc) + ")");
        telemetry.addData("Back Right Max Velocity", finalBackRightMVeloc);
        telemetry.addData("PIDF Back Right", "(" + 0.1*(32767/finalBackRightMVeloc) + ", " + 0.1*(0.1*(32767/finalBackRightMVeloc)) + ", " + "0" + ", " + (32767/finalBackRightMVeloc) + ")");
        telemetry.update();
        sleep(60000);
    }
}
