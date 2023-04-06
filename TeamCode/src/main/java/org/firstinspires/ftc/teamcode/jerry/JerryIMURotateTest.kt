package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryIMURotationTask
import java.util.ArrayDeque

/**
 * A test & debugging OpMode for testing faulty IMU rotation.
 * @author Lachlan Paul, 2023
 */
@Autonomous(name = "<JERRY> IMURotateTest")
class JerryIMURotateTest : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var imu: IMUOp? = null
    private var drive: JerryDrive? = null
    private val tasks = ArrayDeque<TaskImpl>()

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        imu = IMUOp(this, config?.imu)
        drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)

        tasks.add(JerryIMURotationTask(this, 5.0, imu, drive, 90.0, 0.5))
    }

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