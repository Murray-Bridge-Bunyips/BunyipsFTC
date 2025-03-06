package org.firstinspires.ftc.teamcode.teleop

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicVectorDriveTask
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.Companion.default
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Companion.rising
import org.firstinspires.ftc.teamcode.Proto
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

/**
 * Primary TeleOp for Proto.
 *
 * @author Lucas Bubner, 2024
 */
@TeleOp(name = "TeleOp")
open class MainTeleOp : CommandBasedBunyipsOpMode() {
    override fun assignCommands() {
        Proto.drive default HolonomicVectorDriveTask(
            gamepad1,
            Proto.drive
        )
        driver() whenPressed Controls.BACK run HolonomicDriveTask(gamepad1, Proto.drive) finishIf { gamepad1 rising Controls.BACK }

        Proto.clawLift default Proto.clawLift.tasks.control { -gamepad2.lsy.toDouble() }
        Proto.clawRotator default Proto.clawRotator.tasks.controlDelta { gamepad2.rsy.toDouble() * 0.75f * (timer.deltaTime() to Seconds) }
        operator() whenRising (Controls.Analog.RIGHT_TRIGGER to { v -> v == 1.0f }) run Proto.clawLift.tasks.home() finishIf { gamepad2.lsy != 0.0f }
    }

    override fun periodic() {
        Proto.hw.clawIntake?.power = if (gamepad2.x) 1.0 else if (gamepad2.y) -1.0 else 0.0
    }
}