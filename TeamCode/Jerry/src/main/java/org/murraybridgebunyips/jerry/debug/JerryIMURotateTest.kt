package org.murraybridgebunyips.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode
import org.murraybridgebunyips.bunyipslib.subsystems.IMUOp
import org.murraybridgebunyips.bunyipslib.OpModeSelection
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds
import org.murraybridgebunyips.jerry.components.JerryConfig
import org.murraybridgebunyips.jerry.tasks.JerryIMURotationTask

/**
 * A test & debugging OpMode for testing faulty IMU rotation.
 * @author Lachlan Paul, 2023
 */

@Suppress("KDocMissingDocumentation")
@Disabled
@Autonomous(name = "IMU Rotate Test")
class JerryIMURotateTest : AutonomousBunyipsOpMode() {
    private var config = JerryConfig()
    private var imu: IMUOp? = null
    private var drive: CartesianMecanumDrive? = null

    override fun onInitialise() {
        config.init()
        imu = IMUOp(config.imu!!)
        drive = CartesianMecanumDrive(
            config.fl!!,
            config.fr!!,
            config.bl!!,
            config.br!!
        )
    }

    override fun onReady(selectedOpMode: OpModeSelection?) {
        addTask(JerryIMURotationTask(Seconds.of(15.0), imu!!, drive!!, -360.0, 0.5))
    }
}