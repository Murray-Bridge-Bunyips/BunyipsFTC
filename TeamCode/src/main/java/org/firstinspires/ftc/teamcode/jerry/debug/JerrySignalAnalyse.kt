package org.firstinspires.ftc.teamcode.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig

/**
 * Debug OpMode to continually output what AprilTag position the robot is currently seeing.
 */
@TeleOp(name = "<JERRY> POWERPLAY Signal Debug")
class JerrySignalAnalyse : BunyipsOpMode() {
    private lateinit var config: JerryConfig
    private var cam: CameraOp? = null
    private var task: GetAprilTagTask? = null

    override fun onInit() {
        config = RobotConfig.new(config, hardwareMap, this::at) as JerryConfig
        cam = CameraOp(this, config.webcam, config.monitorID, CameraOp.CamMode.OPENCV)
        task = cam?.let { GetAprilTagTask(this, it) }
    }

    override fun activeLoop() {
        task?.run()
        addTelemetry("Currently seeing position: ${task?.position ?: "NONE"}")
    }
}