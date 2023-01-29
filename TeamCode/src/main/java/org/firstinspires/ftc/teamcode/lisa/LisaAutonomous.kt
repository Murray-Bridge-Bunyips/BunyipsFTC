package org.firstinspires.ftc.teamcode.lisa

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.lisa.components.LisaConfig
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive
import org.firstinspires.ftc.teamcode.lisa.tasks.LisaPrecisionDriveTask
import java.util.ArrayDeque

@Autonomous(name = "<LISA> Autonomous Testing")
class LisaAutonomous : BunyipsOpMode() {
    private var config: LisaConfig? = null
    private var drive: LisaDrive? = null
    private val tasks = ArrayDeque<TaskImpl>()
    override fun onInit() {
        config = LisaConfig.newConfig(hardwareMap, telemetry)
        try {
            drive = LisaDrive(
                this,
                config!!.left, config!!.right
            )
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise drive.")
        }
        drive!!.setToBrake()
        tasks.add(MessageTask(this, 3.0, "Activating drive tasks in three seconds."))
        // tasks.add(new LisaIMUTask(this, 4, drive, 0.2, config.imu, 90));
        tasks.add(
            LisaPrecisionDriveTask(
                this,
                drive,
                config!!.imu,
                100.0,
                100000.0,
                0.25,
                3.0,
                0.1
            )
        )
        // tasks.add(new LisaEncoderDriveTask(this, 5, drive, 10, 10, 0.3, 0.3));
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        val currentTask = tasks.peekFirst() ?: return
        currentTask.run()
        if (currentTask.isFinished()) {
            tasks.removeFirst()
        }
        if (tasks.isEmpty()) {
            drive!!.setPower(0.0, 0.0)
            drive!!.update()
        }
    }
}