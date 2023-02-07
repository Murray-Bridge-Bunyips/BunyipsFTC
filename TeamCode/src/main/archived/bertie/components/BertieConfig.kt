package org.firstinspires.ftc.archived.bertie.components

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.common.RobotConfig

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
class BertieConfig : RobotConfig() {
    var backLeft: DcMotorEx? = null
    var backRight: DcMotorEx? = null
    var frontLeft: DcMotorEx? = null
    var frontRight: DcMotorEx? = null
    var armMotor: DcMotorEx? = null
    var spinIntake: CRServo? = null
    var carouselLeft: CRServo? = null
    var carouselRight: CRServo? = null
    override fun init(hardwareMap: HardwareMap?, telemetry: Telemetry) {
        setTelemetry(telemetry)
        backLeft = getHardware("Back Left", DcMotorEx::class.java) as? DcMotorEx
        backRight = getHardware("Back Right", DcMotorEx::class.java) as? DcMotorEx
        frontLeft = getHardware("Front Left", DcMotorEx::class.java) as? DcMotorEx
        frontRight = getHardware("Front Right", DcMotorEx::class.java) as? DcMotorEx
        armMotor = getHardware("Arm Motor", DcMotorEx::class.java) as? DcMotorEx
        carouselLeft = getHardware("Carousel Left", CRServo::class.java) as? CRServo
        carouselRight = getHardware("Carousel Right", CRServo::class.java) as? CRServo
        spinIntake = getHardware("Spin Intake", CRServo::class.java) as? CRServo
        frontRight?.direction = DcMotorSimple.Direction.REVERSE
        backRight?.direction = DcMotorSimple.Direction.REVERSE
        spinIntake?.direction = DcMotorSimple.Direction.REVERSE
        armMotor?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
    }

    companion object {
        fun newConfig(hardwareMap: HardwareMap?, telemetry: Telemetry): BertieConfig {
            val config = BertieConfig()
            config.init(hardwareMap, telemetry)
            return config
        }
    }
}