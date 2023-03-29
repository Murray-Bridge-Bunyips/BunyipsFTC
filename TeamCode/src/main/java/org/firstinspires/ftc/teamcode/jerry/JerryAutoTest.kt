package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryIMURotationTask
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryTimeDriveTask
import java.util.ArrayDeque

/**
 * Testing opmode for Jerry, used to test various components and tasks.
 * @author Lachlan Paul, 2023
 */
@Autonomous(name = "<JERRY> Patriarch of the Bunyips Family")
@Disabled
class JerryAutoTest : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var drive: JerryDrive? = null
    private var Krankenhaus: GetAprilTagTask? = null
    private var arm: JerryArm? = null
    private var cam: CameraOp? = null
    private var imu: IMUOp? = null
    private val tasks = ArrayDeque<TaskImpl>()

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        drive?.setToBrake()

        imu = IMUOp(this, config?.imu)
        tasks.add(MessageTask(this, 1.0, "well here we are again, it's always such a pleasure, remember when you tried to kill me twice"))

        arm = JerryArm(
            this,
            config?.claw1,
            config?.claw2,
            config?.arm1,
            config?.arm2,
            config?.limit
        )

        cam = CameraOp(
            this,
            config?.webcam, config!!.monitorID, CameraOp.CamMode.OPENCV
        )

        Krankenhaus = cam?.let { GetAprilTagTask(this, it) }
    }

    override fun onInitLoop(): Boolean {
        // i have been informed I COULD change the name to something more convenient
        // but this
        // this is funnier
        Krankenhaus?.run()
        return Krankenhaus?.isFinished() ?: true
    }

    override fun onInitDone() {
        val pos = Krankenhaus?.position
        when (pos) {
            GetAprilTagTask.ParkingPosition.LEFT -> {
                // Draw a square
                // Forward
                tasks.add(JerryTimeDriveTask(this, 3.0, drive!!, 0.0, 0.5, 0.0))
                // Right
                tasks.add(JerryTimeDriveTask(this, 3.0, drive!!, 0.5, 0.0, 0.0))
                // South
                tasks.add(JerryTimeDriveTask(this, 3.0, drive!!, 0.0, -0.5, 0.0))
                // West
                tasks.add(JerryTimeDriveTask(this, 3.0, drive!!, -0.5, 0.0, 0.0))
                // i do not care about consistency
            }
            GetAprilTagTask.ParkingPosition.RIGHT -> {
                // Spin 360 degrees right
                tasks.add(JerryIMURotationTask(this, 10.0, imu!!, drive!!, 360.0, 0.5))
            }
            GetAprilTagTask.ParkingPosition.CENTER -> {
                // Insult lucas in the console
                tasks.add(MessageTask( this, 10.0, "lucas is unaware of the several criminals stalking his house at this moment"))
            }
            else -> {
                tasks.add(MessageTask( this, 7.0,"well here we are again(I don't know where that is)"))
            }
        }
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