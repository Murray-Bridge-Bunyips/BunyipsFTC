package au.edu.sa.mbhs.studentrobotics.ftc15215.proto

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.TwoWheelLocalizer
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive
import com.acmerobotics.roadrunner.ftc.LazyImu
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotorSimple

/**
 * FTC 15215 INTO THE DEEP 2024-2025 robot configuration
 *
 * @author Lucas Bubner, 2024
 */
class Proto : RobotConfig() {
    /**
     * Non-subsystem hardware instances.
     */
    val hw: Hardware = Hardware()

    /**
     * RoadRunner Mecanum Drive with Two-Wheel Localization.
     */
    lateinit var drive: MecanumDrive

    override fun onRuntime() {
        // Base is from GLaDOS
        hw.imu = getLazyImu(
            orientationOnRobot = RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
            )
        )
        hw.fl = getHardware("fl", Motor::class.java) { fl ->
            fl.direction = DcMotorSimple.Direction.FORWARD
        }
        hw.bl = getHardware("bl", Motor::class.java) { bl ->
            bl.direction = DcMotorSimple.Direction.REVERSE
        }
        hw.br = getHardware("br", Motor::class.java) { br ->
            br.direction = DcMotorSimple.Direction.REVERSE
        }
        hw.fr = getHardware("fr", Motor::class.java) { fr ->
            fr.direction = DcMotorSimple.Direction.REVERSE
        }

        // REV Through Bore Encoders
        hw.pe = getHardware("pe", RawEncoder::class.java) { p ->
            p.direction = DcMotorSimple.Direction.REVERSE
        }
        hw.ppe = getHardware("ppe", RawEncoder::class.java) { pp ->
            pp.direction = DcMotorSimple.Direction.FORWARD
        }

        // RoadRunner drivebase configuration
        val dm = DriveModel.Builder()
            .setInPerTick(0.0)
            .setLateralInPerTick(0.0)
            .setTrackWidthTicks(0.0)
            .build()
        val mp = MotionProfile.Builder()
            // TODO: constraints and tuning
            .setKs(0.0)
            .setKv(0.0)
            .setKa(0.0)
            .build()
        val mg = MecanumGains.Builder()
            .setAxialGain(0.0)
            .setLateralGain(0.0)
            .setHeadingGain(0.0)
            .setAxialVelGain(0.0)
            .setLateralVelGain(0.0)
            .setHeadingVelGain(0.0)
            .build()
        val twl = TwoWheelLocalizer.Params.Builder()
            .setParYTicks(0.0)
            .setPerpXTicks(0.0)
            .build()

        drive = MecanumDrive(dm, mp, mg, hw.fr!!, hw.fl!!, hw.br!!, hw.bl!!, hw.imu!!, hardwareMap.voltageSensor)
            .withLocalizer(TwoWheelLocalizer(dm, twl, hw.pe!!, hw.ppe!!, hw.imu!!.get()))
    }

    /**
     * Raw hardware instances.
     */
    class Hardware {
        /**
         * Expansion 0: Front Left "fl"
         */
        var fl: Motor? = null

        /**
         * Expansion 1: Front Right "fr"
         */
        var fr: Motor? = null

        /**
         * Expansion 2: Back Right "br"
         */
        var br: Motor? = null

        /**
         * Expansion 3: Back Left "bl"
         */
        var bl: Motor? = null

        /**
         * Control 3: Parallel Encoder "pe"
         */
        var pe: RawEncoder? = null

        /**
         * Control 2: Perpendicular Encoder "ppe"
         */
        var ppe: RawEncoder? = null

        /**
         * Internally mounted on I2C C0 "imu"
         */
        var imu: LazyImu? = null
    }
}