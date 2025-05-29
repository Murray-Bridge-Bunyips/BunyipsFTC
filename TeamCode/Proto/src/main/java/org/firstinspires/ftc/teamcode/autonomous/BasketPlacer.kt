package org.firstinspires.ftc.teamcode.autonomous

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf.degToRad
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Unit.Companion.of
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecond
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Milliseconds
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.constraints.Vel
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueLeft
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.frozenmilk.util.cell.RefCell
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.Proto

@Autonomous(name = "0+4 Basket Placer, 2nd tile from LEFT wall touching, 90° CCW", preselectTeleOp = "TeleOp")
class BasketPlacer : AutonomousBunyipsOpMode() {
    private val basketLiftTarget = Constants.cl_MAX.toInt() - 925
    private val basket = Pose2d(55.6, 52.9, 40.degToRad())
    private val waypoints = listOf(
        Pose2d(25.62, 36.2, -30.degToRad()) to (10 to -5),
        Pose2d(27.5, 37.7, -30.degToRad()) to (15 to -10),
        Pose2d(32.2, 37.5, -30.degToRad()) to (15 to -10)
    )

    override fun onReady(selectedOpMode: RefCell<*>?) {
        Proto.rotator.update()
        Proto.drive.pose = blueLeft()
            .tile(2.0)
            .backward(2 of Inches)
            .rotate(90 of Degrees)
            .build()
            .toFieldPose()
        Proto.drive.makeTrajectory()
            .setTangent(270.0, Degrees)
            .afterTime(0.0, a = Proto.lift.tasks.goTo(basketLiftTarget))
            .splineToLinearHeading(poseHeadingRad = basket, tangent = basket.heading)
            .stopAndAdd(
                Proto.rotator.tasks.setTo(0.5).forAtLeast(500 of Milliseconds)
                    .then(Proto.intake.tasks.runFor(500 of Milliseconds, Constants.i_EJECT))
            ).also {
                for (waypoint in waypoints) {
                    it.setReversed(true)
                        .afterTime(
                            0.0,
                            a = Proto.lift.tasks.home().with(Proto.rotator.tasks.open().after(1 of Seconds))
                                .with(Proto.intake.tasks.runFor(3 of Seconds, Constants.i_INTAKE))
                        )
                        .setVelConstraints { _, _, s -> if (s >= 30) 12.0 else 40.0 }
                        .splineToSplineHeading(
                            poseHeadingRad = waypoint.first,
                            tangent = waypoint.first.heading /* burger */
                        )
                        .setReversed(false)
                        .splineToConstantHeading(
                            pos = Vector2d(
                                waypoint.first.position.x + waypoint.second.first,
                                waypoint.first.position.y + waypoint.second.second
                            ), tangent = waypoint.first.heading
                        ) // +burger
                        .resetVelConstraints()
                        .afterTime(
                            0.0,
                            a = Proto.lift.tasks.goTo(basketLiftTarget).with(Proto.rotator.tasks.close())
                        ).also { last ->
                            if (waypoint == waypoints.last())
                                last
                                    .setReversed(true)
                                    // Don't hit the wall
                                    .splineToSplineHeading(
                                        poseHeadingRad = waypoint.first,
                                        tangent = waypoint.first.heading
                                    )
                                    .setReversed(false)
                        }
                        .setTangent(90.0, Degrees)
                        .splineToSplineHeading(poseHeadingRad = basket, tangent = basket.heading)
                        .stopAndAdd(
                            Proto.rotator.tasks.setTo(0.5).forAtLeast(500 of Milliseconds)
                                .then(Proto.intake.tasks.runFor(500 of Milliseconds, Constants.i_EJECT))
                        )
                }
            }
            .setReversed(true)
            .afterTime(
                0.0,
                a = Proto.lift.tasks.goTo(1900) timeout (3 of Seconds) with Proto.rotator.tasks.setTo(0.4)
            )
            .splineToSplineHeading(Pose2d(38.8, 18.9, 180.degToRad()), tangent = 270.degToRad())
            .setVelConstraints(Vel.ofMax(FieldTilesPerSecond.of(0.5)))
            .splineToConstantHeading(Vector2d(18.0, 6.0), tangent = 180.degToRad())
            // giulio is the best coder here i am better then lucas and we all know it. i am java
            .addTask()
    }
}