package org.firstinspires.ftc.teamcode.common

/**
 * Custom odometer class to control and monitor field positioning data through all available means
 * of sensor input, including deadwheels, IMU, and camera readings.
 * This class aims to provide field-centric functionality, instead of absolute positioning.
 *
 * @author Lucas Bubner - FTC 15215 Captain; Feb-Mar 2023 - Murray Bridge Bunyips
 */
class FieldPositioning(
    opMode: BunyipsOpMode?,
    x: Deadwheel,
    y: Deadwheel,
    cam: CameraOp,
    imu: IMUOp,
    position: StartingPositions?,
    direction: StartingDirections?
) :
    BunyipsComponent(opMode) {

    // Grand running variable that stores the robot position
    var fieldPosition: Int = 0

    // Store current robot heading
    var heading: Double = 0.0

    // Stores encoder threshold between squares
    private val encoderThreshold = 1000

    /**
     * Legal starting positions for POWERPLAY 2022
     */
    enum class StartingPositions {
        RED_LEFT, RED_RIGHT, BLUE_LEFT, BLUE_RIGHT
    }

    /**
     * Starting position relative to the robot where it is placed at a specific location
     */
    enum class StartingDirections {
        FORWARD, BACKWARD, LEFTWARD, RIGHTWARD
    }

    private val x: Deadwheel
    private val y: Deadwheel
    private val cam: CameraOp
    private val imu: IMUOp

    /**
     * Initial orientation of the robot on the field.
     */
    init {
        this.x = x
        this.y = y
        this.cam = cam
        this.imu = imu
        when (position) {
            StartingPositions.RED_LEFT -> fieldPosition = Field[0][1]
            StartingPositions.RED_RIGHT -> fieldPosition = Field[0][4]
            StartingPositions.BLUE_LEFT -> fieldPosition = Field[5][1]
            StartingPositions.BLUE_RIGHT -> fieldPosition = Field[5][4]
            else -> {}
        }
        when (direction) {
            StartingDirections.FORWARD -> {}
            StartingDirections.BACKWARD ->
                // Facing 180 degrees the wrong way, must flip 180 degrees for 0 to be towards the opponents
                imu.offset = 180.0
            StartingDirections.LEFTWARD ->
                // Facing 90 degrees towards the left
                imu.offset = 270.0
            StartingDirections.RIGHTWARD ->
                // Facing 90 degrees towards the right
                imu.offset = 90.0
            else -> {}
        }
        // If the camera is available, initialise it into Vuforia mode as a default
        // The OpMode will change it accordingly and will not account it into the update loop
        if (cam.mode != CameraOp.CamMode.OPENCV)
            cam.swapModes()

    }

    /**
     * Update matrices and data with newest information
     */
    fun update() {
        // Calculate delta of deadwheel encoders using distance they have travelled
        // Each square is 23 and a half inches wide and tall
        val deltaX = x.travelledMM / 595.0
        val deltaY = y.travelledMM / 595.0

        // Use calculation to determine how many squares the array should be modified
        val xSquares = deltaX.toInt()
        val ySquares = deltaY.toInt()

        // Integrate Vuforia data to determine robot orientation
        if (cam.mode == CameraOp.CamMode.STANDARD) {
            // Get the robot's translation from the camera
            val x = cam.vuGetX()
            val y = cam.vuGetY()

            // Abandon ship if we don't have x or y data
            if (x == null || y == null) return

            // ** TRANSLATE DATA INTO FIELD POSITION **
            // The (0, 0) Vuforia location is located in the middle of the field,
            // in the intersection between 15, 16, 21, and 22
            // The Y value faces towards the blue alliance, the X value faces 90 degrees to the right
            // if the observer was facing blue alliance from the centre
            // Z axis is not used as it represents vertical alignment.
            if (x > 0 && y > 0) {
                // Robot is in the top left quadrant of the Field map
            } else if (x > 0 && y < 0) {
                // Robot is in the top right quadrant of the Field map
            } else if (x < 0 && y < 0) {
                // Robot is in the bottom right quadrant of the Field map
            } else if (x < 0 && y > 0) {
                // Robot is in the bottom left quadrant of the Field map
            }
        }

        // Update field position
        fieldPosition += xSquares + ySquares * Field[0].size

        // Update heading with IMU if possible, otherwise try to use Vuforia
        val imuHeading = imu.heading
        val vuHeading = cam.vuGetHeading()

        // Check if Vuforia and IMU are out of sync and report this to the Driver Station
        if (imuHeading != null && vuHeading != null) {
            if (((imuHeading - vuHeading) > 10) || ((imuHeading - vuHeading) < -10)) {
                // Report factor of offset
                opMode!!.telemetry.addData("Heading differential", imuHeading - vuHeading)
            }
        }
    }

    /**
     * Get X orientation of the robot on the field
     */
    fun getX(): Int {
        return (fieldPosition - 1) % Field[0].size
    }

    /**
     * Get Y orientation of the robot on the field
     */
    fun getY(): Int {
        return (fieldPosition - 1) / Field[0].size
    }

    companion object {
        /**
         * A 2D array of the standard FTC field, from the perspective of blue-left and red-right
         */
        private val Field = arrayOf(
            intArrayOf(1,  2,  3,  4,  5,  6),
            intArrayOf(7,  8,  9,  10, 11, 12),
            intArrayOf(13, 14, 15, 16, 17, 18),
            intArrayOf(19, 20, 21, 22, 23, 24),
            intArrayOf(25, 26, 27, 28, 29, 30),
            intArrayOf(31, 32, 33, 34, 35, 36)
        )
    }
}