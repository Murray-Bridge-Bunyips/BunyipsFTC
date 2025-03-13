package org.firstinspires.ftc.teamcode.autonomous

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf.degToRad
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Unit.Companion.of
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Milliseconds
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueLeft
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.frozenmilk.util.cell.RefCell
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.Proto

@Autonomous
class BasketPlacer : AutonomousBunyipsOpMode() {
    override fun onReady(selectedOpMode: RefCell<*>?) {
        val start = blueLeft().tile(2.0).backward(2 of Inches).rotate(90 of Degrees).build().toFieldPose()
        val basketTarget = Constants.cl_MAX.toInt() - 925
        Proto.drive.pose = start
        Proto.drive.makeTrajectory()
            .setTangent(270.0, Degrees)
            .afterTime(0.0, a = Proto.clawLift.tasks.goTo(basketTarget))
            .splineToLinearHeading(vector = Vector2d(54.6, 53.6), heading = 40.degToRad(), tangent = 40.degToRad())
            .stopAndAdd(
                Proto.clawRotator.tasks.setTo(0.5).forAtLeast(500 of Milliseconds)
                    .then(Proto.runIntake(Proto.IntakeDirection.EJECT))
            )
            .setReversed(true)
            .afterTime(
                0.0,
                a = Proto.clawLift.tasks.home().with(Proto.clawRotator.tasks.open())
                    .with(Proto.runIntake(Proto.IntakeDirection.RETRIEVE, 5 of Seconds))
            )
            .setVelConstraints { _, _, s -> if (s >= 30) 12.0 else 40.0  }
            .splineToSplineHeading( //burger
                vector = Vector2d(25.84, 35.00),
                heading = (-30).degToRad(),
                tangent = (-30).degToRad()
            )
            .setReversed(false)
            .splineToConstantHeading(pos = Vector2d(37.72, 29.14), tangent = (-30).degToRad())
            .resetVelConstraints()
            .afterTime(0.0, a = Proto.clawLift.tasks.goTo(basketTarget).with(Proto.clawRotator.tasks.close()))
            .setTangent(90.0, Degrees)
            .splineToSplineHeading(vector = Vector2d(54.6, 53.6), heading = 40.degToRad(), tangent = 40.degToRad())
            .stopAndAdd(
                Proto.clawRotator.tasks.setTo(0.5).forAtLeast(500 of Milliseconds)
                    .then(Proto.runIntake(Proto.IntakeDirection.EJECT))
            )
            // giulio is the best coder here i am better then lucas and we all know it. i am java
            .addTask()

        /*
            Pose2d pose = blueLeft().tile(2.0).backward(Inches.of(2)).rotate(Degrees.of(90)).build().toFieldPose();
            drive.makeTrajectory(pose)
                    .setTangent(270, Degrees)
                    .splineToLinearHeading(new Vector2d(54.6, 53.6), Mathf.degToRad(40), Mathf.degToRad(40))
                    .setReversed(true)
                    .splineToSplineHeading(new Vector2d(30.38, 39.71), Mathf.degToRad(-50), Mathf.degToRad(-50))
                    .setReversed(false)
                    .splineToConstantHeading(new Vector2d(38.49, 34.2), Mathf.degToRad(-50))
                    .setTangent(90, Degrees)
                    .splineToSplineHeading(new Vector2d(54.6, 53.6), Mathf.degToRad(40), Mathf.degToRad(40))
                    .addTask();
         */
    }
}