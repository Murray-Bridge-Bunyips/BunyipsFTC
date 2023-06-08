package org.firstinspires.ftc.teamcode.jerry.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.CameraOp.CamMode
import org.firstinspires.ftc.teamcode.common.Deadwheels
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryIMURotationTask
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryPrecisionDriveTask
import java.util.ArrayDeque

/**
 * Duplicate of JerrySignalAutonomous.
 * Modified by Lachlan to rely on rotating the robot instead, and to use the IMU to rotate.
 * This was to combat a mechanical problem having to do with losing two motor functions.
 */
@Autonomous(name = "<JERRY> POWERPLAY Auto Signal Read & Park ROTATION")
@Disabled
class JerrySignalAutonomousRotate : BunyipsOpMode() {
    private var config = JerryConfig()
    private var cam: CameraOp? = null
    private var drive: JerryDrive? = null
    private var imu: IMUOp? = null
    private var pos: Deadwheels? = null
    private var tagtask: GetAprilTagTask? = null
    private val tasks = ArrayDeque<TaskImpl>()

    override fun onInit() {
        // Configuration of camera and drive components
        config = RobotConfig.new(config, hardwareMap, ::at) as JerryConfig
        cam = CameraOp(this, config.webcam, config.monitorID, CamMode.OPENCV)
        if (config.assert(config.driveMotors))
            drive = JerryDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)

        if (config.assert(config.fl, config.fr))
            pos = Deadwheels(this, config.fl!!, config.fr!!)

        if (config.assert(config.imu))
            imu = IMUOp(this, config.imu!!)

        // Use PrecisionDrive to move rightwards for 1.5 seconds
        // PrecisionDrive will take into account what components we are using and what it can do to achieve this goal.
        tasks.add(
            JerryIMURotationTask(
                this,
                4.0,
                imu,
                drive,
                90.0,
                1.0
            )
        )
        tasks.add(
            JerryPrecisionDriveTask(
                this,
                5.0,
                drive,
                imu,
                pos,
                400.0,
                JerryPrecisionDriveTask.Directions.FORWARD,
                0.5
            )
        )

        // Initialisation of guaranteed task loading completed. We can now dedicate our
        // CPU cycles to the init-loop and find the Signal position.
        tagtask = cam?.let { GetAprilTagTask(this, it) }
    }

    override fun onInitLoop(): Boolean {
        // Using CameraOp OPENCV and AprilTags in order to detect the Signal sleeve
        tagtask?.run()
        return tagtask?.isFinished() ?: true
    }

    override fun onInitDone() {
        // Determine our final task based on the parking position from the camera
        // If on center or NONE, do nothing and just stay in the center
        val position = tagtask?.position
        addTelemetry("ParkingPosition set to: $position")

        // Add movement tasks based on the signal position
        if (position == GetAprilTagTask.ParkingPosition.LEFT) {
            // Drive forward if the position of the signal is LEFT
            tasks.add(
                JerryIMURotationTask(
                    this,
                    6.0,
                    imu,
                    drive,
                    0.0,
                    1.0
                )
            )
            tasks.add(
                JerryPrecisionDriveTask(
                    this,
                    5.0,
                    drive,
                    imu,
                    pos,
                    400.0,
                    JerryPrecisionDriveTask.Directions.FORWARD,
                    0.5
                )
            )
        } else if (position == GetAprilTagTask.ParkingPosition.RIGHT) {
            // Drive backward if the position of the signal is RIGHT
            tasks.add(
                JerryIMURotationTask(
                    this,
                    6.0,
                    imu,
                    drive,
                    0.0,
                    1.0
                )
            )
            tasks.add(
                JerryPrecisionDriveTask(
                    this,
                    5.0,
                    drive,
                    imu,
                    pos,
                    400.0,
                    JerryPrecisionDriveTask.Directions.BACKWARD,
                    0.5
                )
            )
        }
    }

    override fun activeLoop() {
        val currentTask = tasks.peekFirst()
        if (currentTask == null) {
            finish()
            return
        }
        currentTask.run()
        if (currentTask.isFinished()) {
            tasks.removeFirst()
        }
        if (tasks.isEmpty()) {
            drive?.deinit()
        }
    }
}