package org.firstinspires.ftc.teamcode.teleop

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask
import org.firstinspires.ftc.teamcode.Proto
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

/**
 * Drivebase and localizer only TeleOp with auto-lock.
 */
@TeleOp(name = "Drivebase Control")
@Disabled
class Drivebase : CommandBasedBunyipsOpMode() {
    override fun assignCommands() {
        Proto.drive.setDefaultTask(HolonomicDriveTask(gamepad1, Proto.drive))
    }
}
