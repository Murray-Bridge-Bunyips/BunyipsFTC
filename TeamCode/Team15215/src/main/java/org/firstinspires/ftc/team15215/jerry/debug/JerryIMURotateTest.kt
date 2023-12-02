package org.firstinspires.ftc.team15215.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.team15215.jerry.components.JerryConfig
import org.firstinspires.ftc.team15215.jerry.tasks.JerryIMURotationTask
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.ftc.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.ftc.bunyipslib.IMUOp
import org.murraybridgebunyips.ftc.bunyipslib.tasks.AutoTask
import java.util.ArrayDeque

/**
 * A test & debugging OpMode for testing faulty IMU rotation.
 * @author Lachlan Paul, 2023
 */

@Disabled
@Autonomous(name = "JERRY: IMU Rotate Test", group = "JERRY")
class JerryIMURotateTest : BunyipsOpMode() {
    private var config = JerryConfig()
    private var imu: IMUOp? = null
    private var drive: CartesianMecanumDrive? = null
    private val tasks = ArrayDeque<AutoTask>()

    override fun onInit() {
        config.init(this)
        imu = IMUOp(this, config.imu!!)
        drive = CartesianMecanumDrive(
            this,
            config.bl!!,
            config.br!!,
            config.fl!!,
            config.fr!!
        )

        tasks.add(JerryIMURotationTask(this, 15.0, imu!!, drive!!, -360.0, 0.5))
    }

    override fun activeLoop() {
        val currentTask = tasks.peekFirst() ?: return
        currentTask.run()
        if (currentTask.isFinished()) {
            tasks.removeFirst()
        }
        if (tasks.isEmpty()) {
            drive?.stop()
        }
    }
}