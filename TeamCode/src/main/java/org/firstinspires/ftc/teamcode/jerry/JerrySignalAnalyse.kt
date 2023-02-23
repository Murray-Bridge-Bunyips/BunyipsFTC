package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig

@Autonomous(name = "<JERRY> POWERPLAY Signal Test")
class JerrySignalAnalyse : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var cam: CameraOp? = null
    private var task: GetAprilTagTask? = null

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        try {
            cam = CameraOp(this, config?.webcam, config!!.monitorID, CameraOp.CamMode.OPENCV)
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise Camera Operation.")
        }
        task = cam?.let { GetAprilTagTask(this, it) }
    }

    override fun activeLoop() {
        task?.run()
        telemetry.addLine("Currently seeing position: ${task?.position ?: "NONE"}")
        telemetry.update()
    }
}