package org.firstinspires.ftc.teamcode.debug

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

/**
 * No-op to init hardware and print timer status.
 */
@TeleOp(name = "No-op", group = "a")
@Disabled
class Noop : BunyipsOpMode() {
    override fun activeLoop() {
        telemetry.add(timer)
    }
}
