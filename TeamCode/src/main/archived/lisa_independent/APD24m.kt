package org.firstinspires.ftc.archived.lisa_independent

// ----------------------------------------------
// Code written by Lucas Bubner through Blocks
// Murray Bridge Bunyips - 15215
// ----------------------------------------------

/*
    Lucas's first innovative OpMode in Blocks.
    Archived and stored away for information purposes only, tidied up to look a bit nicer.
 */

//@Disabled
//@Autonomous(name = "Advanced PrecisionDrive - 24M STRAIGHT")
//class APD24m : LinearOpMode() {
//    // Declare hardware
//    private var imu: BNO055IMU? = null
//    private var LeftMotor: DcMotor? = null
//    private var RightMotor: DcMotor? = null
//    private var ForwardVisionSystem_DistanceSensor: DistanceSensor? = null
//
//    // Declare variables
//    var fwsAlert = false
//    var yawAngle = 0f
//    var leftPower = 0.0
//    var rightPower = 0.0
//    var leftMotorCurrentPosition = 0.0
//    var rightMotorCurrentPosition = 0.0
//    var elapsedTime: ElapsedTime? = null
//
//    // IMU calibration check method
//    private fun IMU_Calibrated(): Boolean {
//        telemetry.addData("IMU calibration status", imu?.calibrationStatus)
//        telemetry.addData("Gyro calibration", if (imu?.isGyroCalibrated) "True" else "False")
//        telemetry.addData("System status", imu?.systemStatus.toString())
//        return imu?.isGyroCalibrated
//    }
//
//    // If pitch over 30 degrees, run fwsalert
//    private fun areMotorsOverexerting(): Boolean {
//        return imu?.getAngularOrientation(
//            AxesReference.INTRINSIC,
//            AxesOrder.XYZ,
//            AngleUnit.DEGREES
//        ).secondAngle > 30
//    }
//
//    override fun runOpMode() {
//        val imuParameters: BNO055IMU.Parameters
//        imu = hardwareMap.get(BNO055IMU::class.java, "imu")
//        LeftMotor = hardwareMap.get(DcMotor::class.java, "Left Motor")
//        RightMotor = hardwareMap.get(DcMotor::class.java, "Right Motor")
//        ForwardVisionSystem_DistanceSensor =
//            hardwareMap.get(DistanceSensor::class.java, "Forward Vision System")
//        LeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
//        RightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
//        // Neutralise the FWS stop system
//        fwsAlert = false
//        // Reverse direction of both motors for one-direction travel
//        LeftMotor.setDirection(DcMotor.Direction.REVERSE)
//        RightMotor.setDirection(DcMotor.Direction.REVERSE)
//        // Reset encoders for distance calc
//        LeftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER)
//        RightMotor.setMode(RunMode.STOP_AND_RESET_ENCODER)
//        // IMU configuration
//        imuParameters = BNO055IMU.Parameters()
//        imuParameters.mode = BNO055IMU.SensorMode.IMU
//        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
//        imuParameters.loggingEnabled = true
//        imuParameters.accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
//        // Init IMU sequence
//        imu.initialize(imuParameters)
//        telemetry.addData("Status", "IMU successfully initialised")
//        telemetry.update()
//        // IMU calibration check
//        sleep(1000)
//        while (!IMU_Calibrated()) {
//            telemetry.addData(
//                "If calibration ",
//                "doesn't complete after 3 seconds, move through 90 degree pitch, roll and yaw motions until calibration is complete."
//            )
//            telemetry.update()
//            // Loop until IMU is calibrated
//            sleep(1000)
//        }
//        // Calibration complete - await for Driver
//        telemetry.addData("IMU", "Ready.")
//        telemetry.addData(">", "Ready to initialise code.")
//        telemetry.update()
//        waitForStart()
//        if (opModeIsActive()) {
//            elapsedTime = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
//            // Call to main variable functions
//            MoveDesiredDistanceWithPrecisionDriveAlgorithm(2400, 0.0, 5.0, 0.9)
//            // Execution complete.
//            leftPower = 0.0
//            rightPower = 0.0
//            LeftMotor.setPower(0.0)
//            RightMotor.setPower(0.0)
//            telemetry.addData("Code execution:", "All actions completed.")
//            telemetry.update()
//            sleep(3000)
//        }
//    }
//
//    /**
//     * Math function to calculate the distance travelled by the wheels.
//     *
//     * avg of motor position divided by pulses per revolution =
//     * roughly how many revolutions have been done by the robot,
//     * where plugging in the wheel circumference and using the formula
//     *
//     * diameter of wheel in inches * pi = circumference
//     * circumference * revolutions = inches travelled
//     *
//     * inches * 2.54 = cm
//     *
//     * We get the translated distance travelled by the encoders in centimetres.
//     * After motor changes, return value has been additively inversed due to different encoders.
//     */
//    private val translatedDistance: Double
//        private get() {
//            leftMotorCurrentPosition = LeftMotor?.currentPosition.toDouble()
//            rightMotorCurrentPosition = RightMotor?.currentPosition.toDouble()
//            return -(3.34646 * Math.PI * ((leftMotorCurrentPosition + rightMotorCurrentPosition) / 2) / 288 * 2.54)
//        }
//
//    /**
//     * Initialises the Forward Vision System Collision Avoidance System.
//     * Collision avoidance is only active during a forward drive.
//     * The avoidance system will activate once below the alert_threshold,
//     * begin backwards thrust to a clearance of [x]cm, and return
//     * the motor control to the following function in runOpMode.
//     * Code written by Lucas Bubner.
//     */
//    private fun FWSCollisionAvoidanceCheck() {
//        val fwsDist: Double
//        val captureCurrentDistance: Double
//        fwsDist = ForwardVisionSystem_DistanceSensor?.getDistance(DistanceUnit.CM)
//        if (fwsDist < 7 || areMotorsOverexerting()) {
//            // Activating FWS procedure
//            // Broadcast to code to stop operations
//            fwsAlert = true
//            // Take over motor control to reverse x distance
//            LeftMotor?.mode = RunMode.RUN_USING_ENCODER
//            RightMotor?.mode = RunMode.RUN_USING_ENCODER
//            LeftMotor?.power = 0.0
//            RightMotor?.power = 0.0
//            sleep(200)
//            captureCurrentDistance = translatedDistance
//            LeftMotor?.power = -1.0
//            RightMotor?.power = -1.0
//            // Reverse distance is 7cm
//            while (translatedDistance > captureCurrentDistance - 7) {
//                // Allow robot to move backwards until reverse_dist
//                telemetry.addData("Debug", "fws_alert currently active")
//                telemetry.update()
//            }
//            LeftMotor?.power = 0.0
//            RightMotor?.power = 0.0
//            sleep(200)
//            // Reset FWS and allow code to continue
//            fwsAlert = false
//        }
//    }
//
//    /**
//     * Utilise the Angle Adjustment Algorithm in order to turn the
//     * robot to within accuracies, with three-stop correction.
//     */
//    private fun TurnUsingAngleAdjustmentAlgorithm(desiredAngle: Int, tolerance: Int) {
//        val turnSwitch: Boolean
//        var i: Double
//        val desiredAngleCompensated: Int
//        LeftMotor?.mode = RunMode.STOP_AND_RESET_ENCODER
//        RightMotor?.mode = RunMode.STOP_AND_RESET_ENCODER
//        LeftMotor?.mode = RunMode.RUN_USING_ENCODER
//        RightMotor?.mode = RunMode.RUN_USING_ENCODER
//        // Check direction of turn and latch codes
//        if (desiredAngle >= 0) {
//            turnSwitch = true
//            desiredAngleCompensated = desiredAngle - 15
//        } else {
//            turnSwitch = false
//            desiredAngleCompensated = desiredAngle + 15
//        }
//        // Repeat code four times to ensure accuracy
//        // Unless IMU reports within <t> degrees of accuracy
//        i = 1.0
//        while (i <= 3) {
//            if (turnSwitch) {
//                // Turn left
//                LeftMotor?.power = if (i >= 3) -0.25 else -1.5 + i / 2
//                RightMotor?.power = if (i >= 3) 0.25 else 1.5 - i / 2
//                while (yawAngle < if (i >= 2) desiredAngle else desiredAngleCompensated) {
//                    yawAngle = imu?.getAngularOrientation(
//                        AxesReference.INTRINSIC,
//                        AxesOrder.XYZ,
//                        AngleUnit.DEGREES
//                    ).thirdAngle
//                    // Update yaw while robot turns
//                    telemetry.addData("Debug Yaw angle:", yawAngle)
//                    telemetry.update()
//                }
//            } else {
//                // Turn right
//                LeftMotor?.power = if (i >= 3) 0.25 else 1.5 - i / 2
//                RightMotor?.power = if (i >= 3) -0.25 else -1.5 + i / 2
//                while (yawAngle > if (i >= 2) desiredAngle else desiredAngleCompensated) {
//                    // Update Yaw-Angle variable with current yaw.
//                    yawAngle = imu?.getAngularOrientation(
//                        AxesReference.INTRINSIC,
//                        AxesOrder.XYZ,
//                        AngleUnit.DEGREES
//                    ).thirdAngle
//                    // Report yaw orientation to Driver Station.
//                    telemetry.addData("Debug Yaw angle:", yawAngle)
//                    telemetry.update()
//                }
//            }
//            LeftMotor?.power = 0.0
//            RightMotor?.power = 0.0
//            // Allow robot to remove momentum
//            sleep(150)
//            // Check if within tolerance
//            yawAngle = imu?.getAngularOrientation(
//                AxesReference.INTRINSIC,
//                AxesOrder.XYZ,
//                AngleUnit.DEGREES
//            ).thirdAngle
//            if (!(yawAngle < desiredAngle + tolerance || yawAngle > desiredAngle - tolerance)) {
//                // Restart from i loop with lower amplitude until aligned
//                break
//            }
//            i++
//        }
//    }
//
//    /**
//     * Corrects yaw within +-1 degrees of accuracy over x distance.
//     * Uses time limit backup - if set to null, the code will ignore.
//     * Utilises the self-designed PrecisionDrive algorithm.
//     */
//    private fun MoveDesiredDistanceWithPrecisionDriveAlgorithm(
//        desiredDistance_cm: Int,
//        timeLimit: Double,
//        precisionAngle_drg: Double,
//        correctionAmplitude: Double
//    ) {
//        val originalDesiredAngle: Float
//        // Reset encoders and run
//        LeftMotor?.mode = RunMode.STOP_AND_RESET_ENCODER
//        RightMotor?.mode = RunMode.STOP_AND_RESET_ENCODER
//        LeftMotor?.mode = RunMode.RUN_USING_ENCODER
//        RightMotor?.mode = RunMode.RUN_USING_ENCODER
//        originalDesiredAngle = imu?.getAngularOrientation(
//            AxesReference.INTRINSIC,
//            AxesOrder.XYZ,
//            AngleUnit.DEGREES
//        ).thirdAngle
//        // Utilises auto-latching for forward or backward cmd
//        while (!(if ((if (desiredDistance_cm > 0) translatedDistance else -translatedDistance) >= if (desiredDistance_cm > 0) desiredDistance_cm else -desiredDistance_cm || timeLimit != 0.0) elapsedTime?.milliseconds() >= timeLimit * Math.pow(
//                10.0,
//                3.0
//            ) || isStopRequested else isStopRequested) || if (desiredDistance_cm > 0) fwsAlert else isStopRequested
//        ) {
//            if (fwsAlert) {
//                break
//            }
//            yawAngle = imu?.getAngularOrientation(
//                AxesReference.INTRINSIC,
//                AxesOrder.XYZ,
//                AngleUnit.DEGREES
//            ).thirdAngle
//            // Z coordination orientation angle
//            telemetry.addData("Debug Yaw angle:", yawAngle)
//            // If outside x degree(s) of desired yaw then correct
//            if (yawAngle < originalDesiredAngle - precisionAngle_drg) {
//                // Left correction if positive, vice versa
//                leftPower = if (desiredDistance_cm > 0) correctionAmplitude else -1
//                rightPower = if (desiredDistance_cm > 0) 1 else -correctionAmplitude
//            } else if (yawAngle > originalDesiredAngle + precisionAngle_drg) {
//                // Right correction if positive, vice versa
//                leftPower = if (desiredDistance_cm > 0) 1 else -correctionAmplitude
//                rightPower = if (desiredDistance_cm > 0) correctionAmplitude else -1
//            } else {
//                // No correction
//                leftPower = (if (desiredDistance_cm > 0) 1 else -1).toDouble()
//                rightPower = (if (desiredDistance_cm > 0) 1 else -1).toDouble()
//            }
//            // Report debug information
//            telemetry.addData("Debug Left Power:", leftPower)
//            telemetry.addData("Debug Right Power:", rightPower)
//            telemetry.addData(
//                "Debug translatedDistance:",
//                if (desiredDistance_cm > 0) translatedDistance else -translatedDistance
//            )
//            LeftMotor?.power = leftPower
//            RightMotor?.power = rightPower
//            telemetry.update()
//            // Loop correction algorithm and check FWS
//            if (desiredDistance_cm > 0) {
//                FWSCollisionAvoidanceCheck()
//            } else if (areMotorsOverexerting()) {
//                break
//            }
//            sleep(200)
//        }
//        leftPower = 0.0
//        rightPower = 0.0
//        LeftMotor?.power = leftPower
//        RightMotor?.power = rightPower
//        sleep(200)
//    }
//}