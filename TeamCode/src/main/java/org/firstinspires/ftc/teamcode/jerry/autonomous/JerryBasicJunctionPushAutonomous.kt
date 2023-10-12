package org.firstinspires.ftc.teamcode.jerry.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.Odometer
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.common.UserSelection
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryPrecisionDriveTask
import java.util.ArrayDeque

/**
 * Basic autonomous that guarantees the robot will park in a corner or centre.
 * Incredibly simple, uses only time drive.
 */
@Autonomous(
    name = "JERRY: PowerPlay Left-Right Junction Park",
    group = "JERRY",
    preselectTeleOp = "JERRY: TeleOp"
)
class JerryBasicJunctionPushAutonomous : BunyipsOpMode() {
    private var config = JerryConfig()
    private var drive: JerryDrive? = null
    private var imu: IMUOp? = null
    private var x: Odometer? = null
    private var y: Odometer? = null
    private val tasks = ArrayDeque<AutoTask>()
    private val selection = UserSelection(this, {}, "Drive Left", "Drive Right")

    override fun onInit() {
        config = RobotConfig.newConfig(this, config, hardwareMap) as JerryConfig
        if (config.assertDevices(config.driveMotors))
            drive = JerryDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)

        if (config.assertDevices(config.fl))
            x = Odometer(this, config.fl!!, config.xDiameter, config.xTicksPerRev)

        if (config.assertDevices(config.fr))
            y = Odometer(this, config.fr!!, config.yDiameter, config.yTicksPerRev)

        if (config.assertDevices(config.imu))
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
                        x,
                        y,
                        600.0,
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
                        x,
                        y,
                        600.0,
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