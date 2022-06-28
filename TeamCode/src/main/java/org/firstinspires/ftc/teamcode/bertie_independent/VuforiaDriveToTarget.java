package org.firstinspires.ftc.teamcode.bertie_independent;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@SuppressWarnings("unused")
@TeleOp(name="<BERTIE-I> Vuforia Drive To Target")
// @Disabled
public class VuforiaDriveToTarget extends LinearOpMode {
    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)
                                         //  The GAIN constants set the relationship between the measured position error,
                                         //  and how much power is applied to the drive motors.  Drive = Error * Gain
                                         //  Make these values smaller for smoother control.
    final double SPEED_GAIN =   0.05 ;   //  Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double TURN_GAIN  =   0.02 ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MM_PER_INCH = 25.40 ;   //  Metric conversion

    double armPosition = 1600;

    private static final String VUFORIA_KEY =
            "AUAUEO7/////AAABmaBhSSJLMEMkmztY3FQ8jc8fX/wM6mSSQMqcLVW4LjbkWOU5wMH4tLQR7u90fyd93G/7JgfGU5nn2fHF41Q+oaUFe4zI58cr7KsONh689X8o8nr6+7BPN9gMrz08bOzj4+4JwxJ1m84iTPqCpImzYMHr60dtlKBSHN53sRL476JHa+HxZZB4kVq0BhpHlDo7WSGUb6wb5qdgGS3GGx62kiZVCfuWkGY0CZY+pdenCmkNXG2w0/gaeKC5gNw+8G4oGPmAKYiVtCkVJOvjKFncom2h82seL9QA9k96YKns4pQcJn5jdkCbbKNPULv3sqvuvWsjfFOpvzJ0Wh36MrcXlRCetR5oNWctERDjujSjf1o1";
    // Vuforia key from Lucas Bubner
    
    VuforiaLocalizer vuforia    = null;
    OpenGLMatrix targetPose     = null;
    String targetName           = "";

    @Override public void runOpMode()
    {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * To get an on-phone camera preview, use the code below.
         * If no camera preview is desired, use the parameter-less constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;

        // Connect to the camera we are to use.  This name must match what is set up in Robot Configuration
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the trackable objects from the Assets file, and give them meaningful names
        VuforiaTrackables targetsFreightFrenzy = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");
        targetsFreightFrenzy.get(0).setName("Blue Storage");
        targetsFreightFrenzy.get(1).setName("Blue Alliance Wall");
        targetsFreightFrenzy.get(2).setName("Red Storage");
        targetsFreightFrenzy.get(3).setName("Red Alliance Wall");

        // Start tracking targets in the background
        targetsFreightFrenzy.activate();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        DcMotor armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "Front Right");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "Back Right");
        CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "Back Left");
        CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
        CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        boolean targetFound;    // Set to true when a target is detected by Vuforia
        double  targetRange     = 0;        // Distance from camera to target in Inches
        double  targetBearing   = 0;        // Robot Heading, relative to target.  Positive degrees means target is to the right.
        double  drive;        // Desired forward power (-1 to +1)
        double  turn;        // Desired turning power (-1 to +1)

        while (opModeIsActive())
        {
            // Look for first visible target, and save its pose.
            targetFound = false;
            for (VuforiaTrackable trackable : targetsFreightFrenzy)
            {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible())
                {
                    targetPose = ((VuforiaTrackableDefaultListener)trackable.getListener()).getVuforiaCameraFromTarget();

                    // if we have a target, process the "pose" to determine the position of the target relative to the robot.
                    if (targetPose != null)
                    {
                        targetFound = true;
                        targetName  = trackable.getName();
                        VectorF trans = targetPose.getTranslation();

                        // Extract the X & Y components of the offset of the target relative to the robot
                        double targetX = trans.get(0) / MM_PER_INCH; // Image X axis
                        double targetY = trans.get(2) / MM_PER_INCH; // Image Z axis

                        // target range is based on distance from robot position to origin (right triangle).
                        targetRange = Math.hypot(targetX, targetY);

                        // target bearing is based on angle formed between the X axis and the target range line
                        targetBearing = Math.toDegrees(Math.asin(targetX / targetRange));

                        break;  // jump out of target tracking loop if we find a target.
                    }
                }
            }

            // Tell the driver what we see, and what to do.
            if (targetFound) {
                telemetry.addData(">","FOUND, TRAVELLING NOW. HOLD LEFT BUMPER TO STOP\n");
                telemetry.addData("Target", " %s", targetName);
                telemetry.addData("Range",  "%5.1f inches", targetRange);
                telemetry.addData("Bearing","%3.0f degrees", targetBearing);
            } else {
                telemetry.addData(">","Drive using joystick to find target\n");
            }

            // Drive to target Automatically if Left Bumper is not being pressed, AND we have found a target.
            if (!gamepad1.left_bumper && targetFound) {

                // Determine heading and range error so we can use them to control the robot automatically.
                double  rangeError   = (targetRange - DESIRED_DISTANCE);

                // Use the speed and turn "gains" to calculate how we want the robot to move.
                drive = rangeError * SPEED_GAIN;
                turn  = targetBearing * TURN_GAIN ;

                telemetry.addData("Auto","Drive %5.2f, Turn %5.2f", drive, turn);
            } else {

                // drive using manual POV Joystick mode.
                drive = -gamepad1.left_stick_y / 2;
                turn  =  gamepad1.right_stick_x / 2;
                telemetry.addData("Manual","Drive %5.2f, Turn %5.2f", drive, turn);
            }
            telemetry.update();

            // Calculate left and right wheel powers and send to them to the motors.
            double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            frontRight.setPower(rightPower);
            frontLeft.setPower(leftPower);
            backLeft.setPower(leftPower);
            backRight.setPower(rightPower);

      // Arm controller taken from BunyipsFreightFrenzyTeleOp.java
      armPosition = (int) (armPosition + -gamepad2.right_stick_y * 14);
      armMotor.setTargetPosition((int) armPosition < 1850 ? (int) armPosition : armMotor.getTargetPosition());
      telemetry.addData("ArmPosition", armPosition);
      telemetry.addData("TargetPosition", armMotor.getTargetPosition());
      telemetry.addData("CurrentPosition", armMotor.getCurrentPosition());
      armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      if (armMotor.getTargetPosition() > armMotor.getCurrentPosition() + 5 || armMotor.getTargetPosition() < armMotor.getCurrentPosition() - 5) {
        armMotor.setPower(armMotor.getTargetPosition() > armMotor.getCurrentPosition() ? 0.25 : -0.25);
      } else {
        armMotor.setPower(0);
      }

            sleep(10);
        }
    }
}
