package org.firstinspires.ftc.teamcode.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.Odometer
import org.firstinspires.ftc.teamcode.common.RelativeVector
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryVectorDriveTask
import java.util.ArrayDeque

@Autonomous(name="VectorTest")
class JerryVectorTest : BunyipsOpMode() {
    private var config = JerryConfig()
    private var drive: StandardMecanumDrive? = null
    private var imu: IMUOp? = null
    private var x: Odometer? = null
    private var y: Odometer? = null
    private val tasks = ArrayDeque<TaskImpl>()

    override fun onInit() {
        config = RobotConfig.newConfig(this, config, hardwareMap) as JerryConfig
        if (config.affirm(config.driveMotors))
            drive = StandardMecanumDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)

        if (config.affirm(config.fl))
            x = Odometer(this, config.fl!!, config.xDiameter, config.xTicksPerRev)

        if (config.affirm(config.fr))
            y = Odometer(this, config.fr!!, config.yDiameter, config.yTicksPerRev)

        if (config.affirm(config.imu))
            imu = IMUOp(this, config.imu!!)

        log(RelativeVector.FORWARD.vector.toString())

        tasks.add(
            JerryVectorDriveTask(
                this,
                3.5,
                drive,
                imu,
                x,
                y,
                400.0,
                RelativeVector.FORWARD,
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