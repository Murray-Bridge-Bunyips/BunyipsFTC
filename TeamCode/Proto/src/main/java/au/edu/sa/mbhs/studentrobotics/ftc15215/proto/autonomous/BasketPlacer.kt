package au.edu.sa.mbhs.studentrobotics.ftc15215.proto.autonomous

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Unit.Companion.of
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueLeft
import au.edu.sa.mbhs.studentrobotics.ftc15215.proto.Constants
import au.edu.sa.mbhs.studentrobotics.ftc15215.proto.Proto
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.frozenmilk.util.cell.RefCell
import kotlin.math.PI

@Autonomous
class BasketPlacer : AutonomousBunyipsOpMode() {
    override fun onReady(selectedOpMode: RefCell<*>?) {
        val start = blueLeft().tile(2.0).backward(2 of Inches).rotate(90 of Degrees).build().toFieldPose()
        val basketTarget = Constants.cl_MAX.toInt() - 925
        val basket = Pose2d(54.6, 53.6, PI / 4)
        Proto.drive.pose = start
        add(Proto.drive.makeTrajectory()
            .strafeToLinearHeading(basket.position, heading = basket.heading)
            .build().with(Proto.clawLift.tasks.goTo(basketTarget)))
    }
}