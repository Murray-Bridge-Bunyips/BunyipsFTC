package org.murraybridgebunyips.jerry.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode
import org.murraybridgebunyips.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.IMUOp
import org.murraybridgebunyips.bunyipslib.NullSafety
import org.murraybridgebunyips.bunyipslib.OpModeSelection
import org.murraybridgebunyips.bunyipslib.tasks.Command
import org.murraybridgebunyips.jerry.components.JerryConfig
import org.murraybridgebunyips.jerry.tasks.JerryPrecisionDriveTask

/**
 * Basic autonomous that guarantees the robot will park in a corner or centre.
 * Incredibly simple, uses only time drive.
 */
@Autonomous(
    name = "PowerPlay Left-Right Junction Park",
    group = "JERRY",
    preselectTeleOp = "TeleOp"
)
class JerryBasicJunctionPushAutonomous : AutonomousBunyipsOpMode() {
    private var config = JerryConfig()
    private var drive: CartesianMecanumDrive? = null
    private var imu: IMUOp? = null

    override fun onInitialisation() {
        config.init(this)
        if (NullSafety.assertNotNull(config.driveMotors))
            drive = CartesianMecanumDrive(
                this,
                config.fl!!,
                config.fr!!,
                config.bl!!,
                config.br!!
            )

        if (NullSafety.assertNotNull(config.imu))
            imu = IMUOp(this, config.imu!!)
    }

    override fun setOpModes(): MutableList<OpModeSelection> {
        return listOf(
            OpModeSelection(Direction.DRIVE_LEFT),
            OpModeSelection(Direction.DRIVE_RIGHT)
        ).toMutableList()
    }

    override fun setInitTask(): Command? {
        return null
    }

    override fun onQueueReady(selectedOpMode: OpModeSelection?) {
        if (selectedOpMode == null) return
        when (selectedOpMode.obj as Direction) {
            Direction.DRIVE_LEFT ->
                addTask(
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
                addTask(
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

    private enum class Direction {
        DRIVE_LEFT, DRIVE_RIGHT
    }
}