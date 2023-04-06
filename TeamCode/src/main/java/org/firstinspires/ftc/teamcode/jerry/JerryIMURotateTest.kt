package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryIMURotationTask

/**
 * A test OpMode for testing faulty IMU rotation.
 * @author Lachlan Paul, 2023
 */
@Autonomous(name = "<Jerry> IMURotateTest")
class JerryIMURotateTest : BunyipsOpMode() {
    private val imu: IMUOp? = null
    private val drive: JerryDrive? = null
    private val angle: Double? = null
    override fun onInit() {

    }

    override fun activeLoop() {

    }
}