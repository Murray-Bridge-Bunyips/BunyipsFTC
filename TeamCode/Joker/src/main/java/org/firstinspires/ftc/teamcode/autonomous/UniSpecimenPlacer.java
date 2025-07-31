/*

     ^
    / \
   / ! \  HI-JACKING HOT SPOT
  /_____\
     |

*/

package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.teleop.TeleOpCommandBASED.startingPos;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Joker;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.SymmetricPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Uni Specimen Placer", preselectTeleOp = "TeleOp")
public class UniSpecimenPlacer extends AutonomousBunyipsOpMode {
    private final Joker robot = new Joker();
    PoseMap currentPoseMap;

    @Override
    protected void onInitialise() {
        robot.init();
        setOpModes(
                StartingConfiguration.redRight().tile(2.5).backward(Inches.of(4)),
                StartingConfiguration.blueRight().tile(2.5).backward(Inches.of(4))
        ).assignButton(0, 0, Controls.B).assignButton(0, 1, Controls.X);
        telemetry.addData("lift current position", robot.hw.liftMotor.getCurrentPosition());
        telemetry.addData("lift target position", robot.hw.liftMotor.getTargetPosition());
        telemetry.addData("lift power", robot.hw.liftMotor.getPower());
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position startingPosition = (StartingConfiguration.Position) selectedOpMode.get();
        startingPos = startingPosition;
        currentPoseMap = startingPosition.isRed() ? new SymmetricPoseMap() : new IdentityPoseMap();

        robot.drive.setPose(startingPosition.toFieldPose());

        double grabX;
        double grabY;
        double hangY;
        double alignX;
        if (startingPosition.isRed()) {
            grabX = -24*2.6;
            grabY = 24*2.5;
            hangY = 24+9.9; // ziya was HERE. NOPE GIULIO WAS HERE
            alignX = -24*2.4;
        }
        else {
            grabX = -24*2.4;
            grabY = 24*2.525;
            hangY = 24+9.3;
            alignX = -24*2.3;
        }

        add(robot.drive.makeTrajectory(currentPoseMap)
                .strafeTo(new Vector2d(-24*1.8, 24*1.4), Inches)
                .strafeTo(new Vector2d(-24*1.8, 8), Inches)
                .strafeToLinearHeading(new Vector2d(alignX, 8), Inches, 90, Degrees)
                .strafeTo(new Vector2d(alignX, 24*2.2+1), Inches)
                .strafeTo(new Vector2d(alignX, 24*2), Inches)
                .waitFor(3, Seconds)
                .build()
                // extends intake, raises lift out of the way, then tucks intake
                .with(robot.tuck.get().after((robot.lift.tasks.goTo(500).timeout(Seconds.of(2)).after(robot.intake.tasks.goTo(150).timeout(Seconds.of(1))))))
        );

        add(robot.outtakeGrip.tasks.open());

// a man that is here his name was giulio
        add(robot.drive.makeTrajectory(new Pose2d(-24*2.4, 24*2, Math.toRadians(90)), currentPoseMap)
                .strafeTo(new Vector2d(grabX, grabY), Inches)
                .build()
                // moving lift up to correct height to grab specimen
                .with(robot.lift.tasks.goTo(270).timeout(Seconds.of(0.3)))
        );

        add(robot.outtakeGrip.tasks.close());
        wait(0.1, Seconds);

        // moving lift up above so specimen is off the wall
        add(robot.lift.tasks.goTo(700).timeout(Seconds.of(0.35)));

        add(robot.drive.makeTrajectory(new Pose2d(-24*2.6, 24*2.47, Math.toRadians(90)), currentPoseMap)
                .strafeTo(new Vector2d(-24*2, 24*1.835), Inches)
                .strafeToLinearHeading(new Vector2d(0, hangY), Inches, 270, Degrees)
                .build()
                // moving lift up ready to hang specimen
                .with(robot.lift.tasks.goTo(2400).timeout(Seconds.of(1.2))));

        // moving lift down to hang specimen
        add(robot.lift.tasks.goTo(1650).timeout(Seconds.of(0.6)));
        wait(0.05, Seconds);

        add(robot.outtakeGrip.tasks.open());

        add(robot.drive.makeTrajectory(new Pose2d(0, hangY, Math.toRadians(270)), currentPoseMap)
                .strafeTo(new Vector2d(-24*2.75, 24*2.25), Inches) // mods ban this guy
                .build() // mods unban this guy that was the owner of twitch dot television
                .with(robot.lift.tasks.home()));
//        Burning Programmer's Souls
//        Written by Lock and Balls
        // Copper Seal of Approval 2025
//        Copyright Bunyips
//        ------------------------------------
        // im in the code like syntax
        // and if theres too many errors in my code i wont compile it
//        ill put it in my local commit and save it like rocket fuel
        // his coder named lucas but i call him lukeass
//        and hes more then a coder hes a programmer
        // we're putting shit together like that code that copper built
        // on the hill cause this codes gonna run like molasses, cradle,
        // my code fits tighter then a gradle
        // if you hate it you can leave it like beaver
        // but in a day or two ill make you a believer in me
        // cause in the alphabet youll see
        // that LKB kicks a application, not your everyday machination
        // like chef golio my code is truly cookin,
        // peace to maddy stavast causes shes straight out of Adelaide, Australia
        // i dont code PHP or Kotlin when i yoddlin
        // i code a cup of Java, not a big glass of Python
        // or a C, C# if you have time ill drop code again
    }
}