package org.firstinspires.ftc.teamcode.teleop

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicVectorDriveTask
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Companion.rising
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Proto

/**
 * Primary TeleOp for Proto.
 *
 * @author Lucas Bubner, 2024
 */
@TeleOp(name = "TeleOp")
open class MainTeleOp : CommandBasedBunyipsOpMode() {
    override fun assignCommands() {
        HolonomicVectorDriveTask(gamepad1, Proto.drive).setAsDefaultTask()
        driver() whenPressed Controls.BACK run HolonomicDriveTask(gamepad1, Proto.drive) finishIf { gamepad1 rising Controls.BACK }
        Proto.lift.tasks.control { -gamepad2.lsy.toDouble() }.setAsDefaultTask()
        Proto.rotator.tasks.controlDelta { gamepad2.rsy.toDouble() * (timer.deltaTime() to Seconds) }.setAsDefaultTask()
        Proto.intake.tasks.control { if (gamepad2.x) 1.0 else if (gamepad2.y) -1.0 else 0.0 }.setAsDefaultTask()
        operator() whenRising (Controls.Analog.RIGHT_TRIGGER to { v -> v == 1.0f }) run Proto.lift.tasks.home() finishIf { gamepad2.lsy != 0.0f }
    }
}