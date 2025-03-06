package org.firstinspires.ftc.teamcode.teleop

import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.NullLocalizer
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.Companion.default
import org.firstinspires.ftc.teamcode.Proto
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

/**
 * Variant of [MainTeleOp] that does not use localization for improved raw control.
 *
 * @author Lucas Bubner, 2024
 */
@TeleOp(name = "TeleOp (No Localization)")
class MainTeleOpNoLocalizer : MainTeleOp() {
    override fun onInitialise() {
        Proto.drive.localizer = NullLocalizer()
    }

    override fun assignCommands() {
        super.assignCommands()
        unbind(0)
        Proto.drive default HolonomicDriveTask(gamepad1, Proto.drive)
    }
}