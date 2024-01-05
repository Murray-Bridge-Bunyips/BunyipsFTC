package org.murraybridgebunyips.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode
import org.murraybridgebunyips.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.IMUOp
import org.murraybridgebunyips.bunyipslib.OpModeSelection
import org.murraybridgebunyips.bunyipslib.tasks.RobotTask
import org.murraybridgebunyips.jerry.components.JerryConfig
import org.murraybridgebunyips.jerry.tasks.JerryIMURotationTask

/**
 * A test & debugging OpMode for testing faulty IMU rotation.
 * @author Lachlan Paul, 2023
 */

@Disabled
@Autonomous(name = "IMU Rotate Test")
class JerryIMURotateTest : AutonomousBunyipsOpMode() {
    private var config = JerryConfig()
    private var imu: IMUOp? = null
    private var drive: CartesianMecanumDrive? = null

    override fun onInitialisation() {
        config.init(this)
        imu = IMUOp(this, config.imu!!)
        drive = CartesianMecanumDrive(
            this,
            config.fl!!,
            config.fr!!,
            config.bl!!,
            config.br!!
        )
    }

    override fun setOpModes(): MutableList<OpModeSelection>? {
        return null
    }

    override fun setInitTask(): RobotTask? {
        return null
    }

    override fun onQueueReady(selectedOpMode: OpModeSelection?) {
        addTask(JerryIMURotationTask(this, 15.0, imu!!, drive!!, -360.0, 0.5))
    }
}