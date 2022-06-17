package org.firstinspires.ftc.teamcode;
 
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

// Uses Extended DcMotor class

@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Autonomous(name = "Test - Drivetrain PIDF")
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

        // Set all motors to forward drive, although we wouldn't do this to move forward
        frontRight.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);

        telemetry.addData("TEST READY", "Initialise to begin test (20s)");
        telemetry.addData("WARNING"," MOVEMENT MOTORS WILL ENGAGE AT FULL SPEED WHEN STARTED!!");
        telemetry.addData("!!!", " SET ROBOT ON STABLE STAND BEFORE STARTING, AND USE A FULLY CHARGED BATTERY.");
        telemetry.update();

        waitForStart();
        
        // Back Left
        runtime.reset();
        backLeft.setPower(1);
        while (runtime.seconds() < 5) {
            backLeftcurrentVelocity = backLeft.getVelocity();
            if (backLeftcurrentVelocity > backLeftmaxVelocity) {
                backLeftmaxVelocity = backLeftcurrentVelocity; }
            telemetry.addData("TEST IN PROGRESS", "1/4, Back Left Motor");
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("backLeftcurrentVelocity", backLeftcurrentVelocity);
            telemetry.addData("backLeftmaxVelocity", backLeftmaxVelocity);
            telemetry.addData("backLeftReportedVelocity", backLeft.getVelocity());
            telemetry.update();
        } finalBackLeftMVeloc = backLeftmaxVelocity;
        backLeft.setPower(0);

        // Back Right
        backRight.setPower(1);
        while (runtime.seconds() < 10) {
            backRightcurrentVelocity = backRight.getVelocity();
            if (backRightcurrentVelocity > backRightmaxVelocity) {
                backRightmaxVelocity = backRightcurrentVelocity; }
            telemetry.addData("TEST IN PROGRESS", "2/4, Back Right Motor");
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("backRightcurrentVelocity", backRightcurrentVelocity);
            telemetry.addData("backRightmaxVelocity", backRightmaxVelocity);
            telemetry.addData("backRightReportedVelocity", backRight.getVelocity());
            telemetry.update();
        } finalBackRightMVeloc = backRightmaxVelocity;
        backRight.setPower(0);

        // Front Left
        frontLeft.setPower(1);
        while (runtime.seconds() < 15) {
            frontLeftcurrentVelocity = frontLeft.getVelocity();
            if (frontLeftcurrentVelocity > frontLeftmaxVelocity) {
                frontLeftmaxVelocity = frontLeftcurrentVelocity; }
            telemetry.addData("TEST IN PROGRESS", "3/4, Front Left Motor");
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("frontLeftcurrentVelocity", frontLeftcurrentVelocity);
            telemetry.addData("frontLeftmaxVelocity", frontLeftmaxVelocity);
            telemetry.addData("frontLeftReportedVelocity", frontLeft.getVelocity());
            telemetry.update();
        } finalFrontLeftMVeloc = frontLeftmaxVelocity;
        frontLeft.setPower(0);

        // Front Right
        frontRight.setPower(1);
        while (runtime.seconds() < 20) {
            frontRightcurrentVelocity = frontRight.getVelocity();
            if (frontRightcurrentVelocity > frontRightmaxVelocity) {
                frontRightmaxVelocity = frontRightcurrentVelocity; }
            telemetry.addData("TEST IN PROGRESS", "4/4, Front Right Motor");
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("frontcurrentVelocity", frontRightcurrentVelocity);
            telemetry.addData("frontmaxVelocity", frontRightmaxVelocity);
            telemetry.addData("frontRightReportedVelocity", frontRight.getVelocity());
            telemetry.update();
        } finalFrontRightMVeloc = frontRightmaxVelocity;
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
