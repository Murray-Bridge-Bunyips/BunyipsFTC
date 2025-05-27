package org.firstinspires.ftc.teamcode.debug

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Proto

@TeleOp(name = "Test Vertical Lift", group = "a")
@Disabled
class VerticalLiftTest : BunyipsOpMode() {
    override fun activeLoop() {
        Proto.clawLift.setPower(-gamepad1.lsy.toDouble())
        Proto.clawLift.update()
        Motor.debug(Proto.hw.clawLift!!, "Claw Lift", t)
    }
}