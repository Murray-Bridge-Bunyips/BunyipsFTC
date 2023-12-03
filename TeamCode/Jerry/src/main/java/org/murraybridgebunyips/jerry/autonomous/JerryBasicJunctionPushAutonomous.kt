package org.murraybridgebunyips.jerry.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.IMUOp
import org.murraybridgebunyips.bunyipslib.NullSafety
import org.murraybridgebunyips.bunyipslib.UserSelection
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask
import org.murraybridgebunyips.jerry.components.JerryConfig
import org.murraybridgebunyips.jerry.tasks.JerryPrecisionDriveTask
import java.util.ArrayDeque

/**
 * Basic autonomous that guarantees the robot will park in a corner or centre.
 * Incredibly simple, uses only time drive.
 */
@Autonomous(
    name = "PowerPlay Left-Right Junction Park",
    group = "JERRY",
    preselectTeleOp = "TeleOp"
)
class JerryBasicJunctionPushAutonomous : BunyipsOpMode() {
    private var config = JerryConfig()
    private var drive: CartesianMecanumDrive? = null
    private var imu: IMUOp? = null
    private val tasks = ArrayDeque<AutoTask>()
    private val selection = UserSelection(this, {}, "Drive Left", "Drive Right")

    override fun onInit() {
        config.init(this)
        if (NullSafety.assertNotNull(config.driveMotors))
            drive = CartesianMecanumDrive(
                this,
                config.bl!!,
                config.br!!,
                config.fl!!,
                config.fr!!
            )

        if (NullSafety.assertNotNull(config.imu))
            imu = IMUOp(this, config.imu!!)

        selection.start()
    }

    override fun onStart() {
        when (selection.result) {
            "Drive Left" ->
                tasks.add(
                    JerryPrecisionDriveTask(
                        this,
                        1.5,
                        drive,
                        imu,
                        JerryPrecisionDriveTask.Directions.LEFT,
                        1.0
                    )
                )

            else ->
                tasks.add(
                    JerryPrecisionDriveTask(
                        this,
                        1.5,
                        drive,
                        imu,
                        JerryPrecisionDriveTask.Directions.RIGHT,
                        1.0
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
            drive?.stop()
        }
    }
}