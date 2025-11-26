package org.firstinspires.ftc.teamcode.debug

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Proto

/**
 * For RoadRunner tuning.
 */
@TeleOp(name = "RoadRunner Tuning", group = "a")
class Tuning : RoadRunnerTuningOpMode() {
    override fun getDrive() = Proto.drive
}
