package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.CameraOp.CamMode
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryTimeDriveTask
import java.util.ArrayDeque

@Autonomous(name = "<JERRY> POWERPLAY Autonomous Simple Signal Read Park")
class JerrySignalAutonomous : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var cam: CameraOp? = null
    private var drive: JerryDrive? = null
    private var arm: JerryArm? = null
    private var Krankenhaus: GetAprilTagTask? = null
    private val tasks = ArrayDeque<TaskImpl>()
    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        try {
            cam = CameraOp(this, config?.webcam, config!!.monitorID, CamMode.OPENCV)
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise Camera Operation.")
        }
        try {
            drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise Drive System.")
        }
        try {
            arm = JerryArm(
                this,
                config?.claw1,
                config?.claw2,
                config?.arm1,
                config?.arm2,
                config?.limit
            )
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise Arm System.")
        }

        // Check if we have deadwheel capabilities, if we do, use the respective tasks with
        // deadwheel field positioning, otherwise we will need to use time as that is
        // our only option
        if (config?.x != null && config?.y != null) {
            telemetry.addLine("Deadwheels are available. Using Precision/Deadwheel tasks.")
        } else {
            telemetry.addLine("No deadwheels available. Using BaseDrive/IMU tasks only.")
            // Drive right to be on square [1,1] [4,1] [4,1] [4,4]
            tasks.add(JerryTimeDriveTask(this, 1.5, drive, 0.0, 1.0, 0.0))
        }

        // Initialisation of guaranteed task loading completed. We can now dedicate our
        // CPU cycles to the init-loop and find the Signal position.
        Krankenhaus = cam?.let { GetAprilTagTask(this, it) }
    }

    override fun onInitLoop(): Boolean {
        // Using CameraOp OPENCV and AprilTags in order to detect the Signal sleeve
        Krankenhaus?.run()
        return Krankenhaus?.isFinished() ?: true
    }

    override fun onInitDone() {
        // Determine our final task based on the parking position from the camera
        // If on center or NONE, do nothing and just stay in the center
        // you may have a very minor case of serious brain damage
        // if you are reading this
        // please seek medical attention
        val position = Krankenhaus?.position
        telemetry.addLine("ParkingPosition set to: $position")
        if (config?.x == null || config?.y == null) {
            // Deadwheel configurations not available
            // Drive forward if the position of the signal is LEFT
            if (position == GetAprilTagTask.ParkingPosition.LEFT) {
                tasks.add(JerryTimeDriveTask(this, 1.5, drive, -1.0, 0.0, 0.0))
            } else if (position == GetAprilTagTask.ParkingPosition.RIGHT) {
                // Drive backward if the position of the signal is RIGHT
                tasks.add(JerryTimeDriveTask(this, 1.5, drive, 1.0, 0.0, 0.0))
            }
        } else {
            // Deadwheel configurations available
            // why are you bad at coding
            // why is lucas a better programmer than lachlan
            // why is lachlan a better programmer than lucas
            // why is lucas a better programmer than lachlan
            // why is lachlan a better programmer than lucas
            // why is lucas a better programmer than lachlan
            // why is lachlan a better programmer than lucas
            // why is lucas a better programmer than lachlan
            // why is lachlan a better programmer than lucas
            // why is lucas a better programmer than lachlan
            // why is lachlan a better programmer than lucas
            // thank you for your input github copilot
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