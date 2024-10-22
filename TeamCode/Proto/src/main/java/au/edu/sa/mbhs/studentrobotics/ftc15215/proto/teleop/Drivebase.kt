package au.edu.sa.mbhs.studentrobotics.ftc15215.proto.teleop

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import au.edu.sa.mbhs.studentrobotics.ftc15215.proto.Proto

/**
 * Drivebase and localizer only TeleOp with auto-lock.
 */
@TeleOp(name = "Drivebase Control and Localizer Auto-Lock")
class Drivebase : CommandBasedBunyipsOpMode() {
    private val robot = Proto()

    override fun onInitialise() {
        robot.init()
    }

    override fun assignCommands() {
        robot.drive.setDefaultTask(HolonomicDriveTask(gamepad1, robot.drive))
    }
}
