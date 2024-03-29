package org.firstinspires.ftc.teamcode.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.NullSafety
import org.firstinspires.ftc.teamcode.common.OpenCVCam
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask
import org.firstinspires.ftc.teamcode.common.tasks.GetSignalTask
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryIMURotationTask
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryTimeDriveTask
import java.util.ArrayDeque

/**
 * Testing opmode for Jerry, used to test various components and tasks.
 * @author Lachlan Paul, 2023
 */

@Disabled
@Autonomous(name = "Patriarch of the Bunyips Family")
class JerryAutoTest : BunyipsOpMode() {
    private var config = JerryConfig()
    private var drive: StandardMecanumDrive? = null
    private var tagTask: GetSignalTask? = null
    private var arm: JerryArm? = null
    private var cam: OpenCVCam? = null
    private var imu: IMUOp? = null
    private val tasks = ArrayDeque<AutoTask>()

    override fun onInit() {
        config = RobotConfig.newConfig(this, config, hardwareMap) as JerryConfig
        if (NullSafety.assertNotNull(config.driveMotors)) {
            drive = StandardMecanumDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)
        }

        drive?.setToBrake()
        if (NullSafety.assertNotNull(config.imu)) {
            imu = IMUOp(this, config.imu!!)
        }
        tasks.add(
            MessageTask(
                this,
                1.0,
                "well here we are again, it's always such a pleasure, remember when you tried to kill me twice"
            )
        )
        if (NullSafety.assertNotNull(config.armComponents)) {
            arm = JerryArm(
                this,
                config.claw!!,
                config.arm1!!,
                config.arm2!!,
                config.limit!!
            )
        }

        cam = OpenCVCam(
            this,
            config.webcam, config.monitorID
        )

        tagTask = cam?.let { GetSignalTask(this, it) }
    }

    override fun onInitLoop(): Boolean {
        // i have been informed I COULD change the name to something more convenient
        // but this
        // this is funnier
        tagTask?.run()
        return tagTask?.isFinished() ?: true
    }

    override fun onInitDone() {
        when (tagTask?.position) {
            RelativePose2d.LEFT -> {
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

            RelativePose2d.RIGHT -> {
                // Spin 360 degrees right
                tasks.add(JerryIMURotationTask(this, 10.0, imu!!, drive!!, 360.0, 0.5))
            }

            RelativePose2d.CENTER -> {
                // Insult lucas in the console
                tasks.add(
                    MessageTask(
                        this,
                        10.0,
                        "lucas is unaware of the several criminals stalking his house at this moment"
                    )
                )
            }

            else -> {
                tasks.add(
                    MessageTask(
                        this,
                        7.0,
                        "well here we are again(I don't know where that is)"
                    )
                )
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
            drive?.stop()
        }
    }
}