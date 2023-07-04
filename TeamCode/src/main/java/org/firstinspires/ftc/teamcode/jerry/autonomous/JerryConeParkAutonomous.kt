package org.firstinspires.ftc.teamcode.jerry.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.OpenCVCam
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import java.util.ArrayDeque

/**
 * Signal read, movement of a preloaded cone, and park autonomous for the PowerPlay season.
 * @author Lucas Bubner, 2023
 */
@Autonomous(
    name = "JERRY: PowerPlay Cone Placement & Signal Park",
    group = "JERRY",
    preselectTeleOp = "JERRY: TeleOp"
)
class JerryConeParkAutonomous : BunyipsOpMode() {
    private var config = JerryConfig()
    private var cam: OpenCVCam? = null
    private var drive: JerryDrive? = null
    private var tagtask: GetAprilTagTask? = null
    private val tasks = ArrayDeque<TaskImpl>()

    override fun onInit() {
        // Configuration of camera and drive components
        config = RobotConfig.new(config, hardwareMap, ::telem) as JerryConfig
        cam = OpenCVCam(this, config.webcam, config.monitorID)
        if (config.assert(config.driveMotors))
            drive = JerryDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)

        // Initialisation of guaranteed task loading completed. We can now dedicate our
        // CPU cycles to the init-loop and find the Signal position.
        tagtask = cam?.let { GetAprilTagTask(this, it) }

        // TODO: Add movement tasks that will run before Signal position movements
    }

    override fun onInitLoop(): Boolean {
        // Using OpenCV and AprilTags in order to detect the Signal sleeve
        tagtask?.run()
        return tagtask?.isFinished() ?: true
    }

    override fun onInitDone() {
        // Determine our final task based on the parking position from the camera
        // If on center or NONE, do nothing and just stay in the center
        val position = tagtask?.position
        addTelemetry("ParkingPosition set to: $position")

        // TODO: Add movement tasks based on the signal position
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