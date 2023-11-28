package org.firstinspires.ftc.teamcode.jerry.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.ButtonHashmap
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.Odometer
import org.firstinspires.ftc.teamcode.common.OpenCVCam
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.common.RelativeVector
import org.firstinspires.ftc.teamcode.common.StandardMecanumDrive
import org.firstinspires.ftc.teamcode.common.tasks.GetParkingPositionTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryVectorDriveTask
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
@Disabled
// Experimental Vector autonomous. This code was scrapped as it served little advantage over setSpeedXYR.
// It is also more buggy and overall less reliable.
class JerryConeParkAutonomous : BunyipsOpMode() {
    private var config = JerryConfig()
    private var cam: OpenCVCam? = null
    private var drive: StandardMecanumDrive? = null
    private var imu: IMUOp? = null
    private var x: Odometer? = null
    private var y: Odometer? = null
    private var tagtask: GetParkingPositionTask? = null
    private val tasks = ArrayDeque<TaskImpl>()

    private var originVector: RelativeVector = RelativeVector.FORWARD

    override fun onInit() {
        // Configuration of camera and drive components
        config = RobotConfig.newConfig(this, config, hardwareMap) as JerryConfig
        cam = OpenCVCam(this, config.webcam, config.monitorID)
        if (config.affirm(config.driveMotors))
            drive = StandardMecanumDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)

        if (config.affirm(config.fl))
            x = Odometer(this, config.fl!!, config.xDiameter, config.xTicksPerRev)

        if (config.affirm(config.fr))
            y = Odometer(this, config.fr!!, config.yDiameter, config.yTicksPerRev)

        if (config.affirm(config.imu))
            imu = IMUOp(this, config.imu!!)

        // Queue tasks for moving the cone forward or backwards based on the start position
        originVector = ButtonHashmap.map(this, RelativeVector.FORWARD, RelativeVector.BACKWARD)
        tasks.add(
            JerryVectorDriveTask(
                this,
                3.5,
                drive,
                imu,
                x,
                y,
                400.0,
                originVector,
                0.5
            )
        )

        // Find signal position
        tagtask = cam?.let { GetParkingPositionTask(this, it) }
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

        // Determine route back to parking position based on the origin vector
        originVector.vector.flip()
        // If we're as far away from the parking spot, we need to move 2x as far
        val itsMarathonTime = position == GetParkingPositionTask.ParkingPosition.LEFT && originVector == RelativeVector.BACKWARD
                                || position == GetParkingPositionTask.ParkingPosition.RIGHT && originVector == RelativeVector.FORWARD

        // Always move back to the origin if needed
        if (position == GetParkingPositionTask.ParkingPosition.CENTER || itsMarathonTime) {
            tasks.add(
                JerryVectorDriveTask(
                    this,
                    if (itsMarathonTime) 7.0 else 3.5,
                    drive,
                    imu,
                    x,
                    y,
                    if (itsMarathonTime) 800.0 else 400.0,
                    originVector,
                    if (itsMarathonTime) 0.75 else 0.5
                )
            )
        }

        // Move into the parking position
        tasks.add(
            JerryVectorDriveTask(
                this,
                3.5,
                drive,
                imu,
                x,
                y,
                400.0,
                RelativeVector.RIGHT,
                0.5
            )
        )
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