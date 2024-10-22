package au.edu.sa.mbhs.studentrobotics.ftc15215.proto.debug

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import au.edu.sa.mbhs.studentrobotics.ftc15215.proto.Proto

/**
 * No-op to init hardware and print timer status.
 */
@TeleOp(name = "No-op", group = "a")
class Noop : BunyipsOpMode() {
    private val robot = Proto()

    override fun onInit() {
        robot.init()
    }

    override fun activeLoop() {
        telemetry.add(timer)
    }
}
