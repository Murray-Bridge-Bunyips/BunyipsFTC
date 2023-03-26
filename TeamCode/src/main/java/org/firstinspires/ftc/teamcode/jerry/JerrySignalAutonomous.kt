package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.CameraOp.CamMode
import org.firstinspires.ftc.teamcode.common.Deadwheels
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryPrecisionDriveTask
import java.util.ArrayDeque

/**
 * Basic Signal read and park OpMode. Uses camera to read the signal and then drives to the correct square.
 */
@Autonomous(name = "<JERRY> POWERPLAY Auto Signal Read & Park")
class JerrySignalAutonomous : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var cam: CameraOp? = null
    private var drive: JerryDrive? = null
    private var imu: IMUOp? = null
    private var pos: Deadwheels? = null
    private var tagtask: GetAprilTagTask? = null
    private val tasks = ArrayDeque<TaskImpl>()
    override fun onInit() {
        // Configuration of camera and drive components
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        cam = CameraOp(this, config?.webcam, config!!.monitorID, CamMode.OPENCV)
        drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        pos = Deadwheels(this, config?.fl!!, config?.fr!!)
        imu = IMUOp(this, config?.imu)

        // Use PrecisionDrive to move rightwards for 1.5 seconds
        // PrecisionDrive will take into account what components we are using and what it can do to achieve this goal.
        tasks.add(
            JerryPrecisionDriveTask(
                this,
                4.0,
                drive,
                imu,
                pos,
                600.0,
                JerryPrecisionDriveTask.Directions.RIGHT,
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
        telemetry.addLine("ParkingPosition set to: $position")

        // Add movement tasks based on the signal position
        if (position == GetAprilTagTask.ParkingPosition.LEFT) {
            // Drive forward if the position of the signal is LEFT
            tasks.add(
                JerryPrecisionDriveTask(
                    this,
                    3.5,
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
                JerryPrecisionDriveTask(
                    this,
                    3.5,
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

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        val currentTask = tasks.peekFirst() ?: return
        currentTask.run()
        if (currentTask.isFinished()) {
            tasks.removeFirst()
        }
        if (tasks.isEmpty()) {
            drive?.deinit()
        }
    }
}