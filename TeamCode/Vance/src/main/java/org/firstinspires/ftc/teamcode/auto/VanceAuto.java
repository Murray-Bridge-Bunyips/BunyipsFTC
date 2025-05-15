package org.firstinspires.ftc.teamcode.auto;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Milliseconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Vance;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous
public class VanceAuto extends AutonomousBunyipsOpMode {
    private final Vance vance = Vance.instance;
    private final Pose2d basketPos = Geometry.poseFrom(new Vector2d(56.86, 55.04), Inches, 230.00, Degrees);
    private final int placeHeight = 500;

    @Override
    protected void onInitialise() {
        setOpModes("Red", "Blue");
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        String res = (String) selectedOpMode.get(); // john drake approves!!!!!!!!!!!!

        switch (res) {
            case "Red":

            case "Blue":
                vance.drive.setPose(new Vector2d(36.03, 62.31), Inches, -90.00, Degrees);
                vance.drive.makeTrajectory()
                        .splineTo(basketPos.position, basketPos.heading)
                        .addTask();

                pickAndPlace(250);
                pickAndPlace(20);
                // TODO: TOOD: will need a unique one for third sample since it's against the wall
        }
    }


    private void pickAndPlace(int degreeToTurnTo) {
        vance.drive.makeTrajectory(basketPos).turn(degreeToTurnTo).addTask();
        add(vance.shoulder.tasks.home());
        add(vance.intake.tasks.runFor(Milliseconds.of(500), 1));
        add(vance.shoulder.tasks.goTo(placeHeight));
    }
}
