package org.murraybridgebunyips.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.IMUOp
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask
import org.murraybridgebunyips.jerry.components.JerryConfig
import org.murraybridgebunyips.jerry.tasks.JerryIMURotationTask
import java.util.ArrayDeque

/**
 * A test & debugging OpMode for testing faulty IMU rotation.
 * @author Lachlan Paul, 2023
 */

@Disabled
@Autonomous(name = "IMU Rotate Test")
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
            config.fl!!,
            config.fr!!,
            config.bl!!,
            config.br!!
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