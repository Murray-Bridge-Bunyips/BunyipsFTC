package org.murraybridgebunyips.jerry.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode
import org.murraybridgebunyips.bunyipslib.IMUOp
import org.murraybridgebunyips.bunyipslib.NullSafety
import org.murraybridgebunyips.bunyipslib.OpModeSelection
import org.murraybridgebunyips.bunyipslib.Direction
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask
import org.murraybridgebunyips.jerry.components.JerryConfig
import org.murraybridgebunyips.jerry.tasks.JerryPrecisionDriveTask

/**
 * Basic autonomous that guarantees the robot will park in a corner or centre.
 * Incredibly simple, uses only time drive.
 */
@Suppress("KDocMissingDocumentation")
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
        config.init()
        if (NullSafety.assertNotNull(config.driveMotors))
            drive = CartesianMecanumDrive(
                config.fl!!,
                config.fr!!,
                config.bl!!,
                config.br!!
            )

        if (NullSafety.assertNotNull(config.imu))
            imu = IMUOp(config.imu!!)
    }

    override fun setOpModes(): MutableList<OpModeSelection> {
        return listOf(
            OpModeSelection(Direction.LEFT),
            OpModeSelection(Direction.RIGHT)
        ).toMutableList()
    }

    override fun setInitTask(): RobotTask? {
        return null
    }

    override fun onQueueReady(selectedOpMode: OpModeSelection?) {
        if (selectedOpMode == null) return
        addTask(
            JerryPrecisionDriveTask(
                1.5,
                drive,
                imu,
                selectedOpMode.obj as Direction,
                1.0
            )
        )
    }
}