package org.firstinspires.ftc.teamcode.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryIMURotationTask
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
    private var drive: JerryDrive? = null
    private val tasks = ArrayDeque<AutoTask>()

    override fun onInit() {
        config.init(this, hardwareMap)
        imu = IMUOp(this, config.imu!!)
        drive = JerryDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)

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