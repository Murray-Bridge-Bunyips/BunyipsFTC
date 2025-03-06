package org.firstinspires.ftc.teamcode.autonomous;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.SymmetricPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import org.firstinspires.ftc.teamcode.Joker;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Basket Side", preselectTeleOp = "TeleOp")
public class BasketSide extends AutonomousBunyipsOpMode {
    private final Joker robot = new Joker();
    PoseMap currentPoseMap;

    @Override
    protected void onInitialise() {
        robot.init();
        setOpModes(
                StartingConfiguration.redLeft().tile(2).backward(Inches.of(4)),
                StartingConfiguration.blueLeft().tile(2).backward(Inches.of(4))
        );
        //robot.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position startingPosition = (StartingConfiguration.Position) selectedOpMode.get();
        if (startingPosition.isBlue()) {robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);} else {robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);}
        currentPoseMap = startingPosition.isRed() ? new SymmetricPoseMap() : new IdentityPoseMap();

        robot.drive.setPose(startingPosition.toFieldPose());

        robot.drive.makeTrajectory(currentPoseMap)
                .strafeTo(new Vector2d(24*1.5, 8), Inches)
                .strafeTo(new Vector2d(24*2.1, 8), Inches)
                .strafeTo(new Vector2d(24*2.1, 24*3-(9+3.5-2.5)), Inches)
                .strafeTo(new Vector2d(24*2.1, 8), Inches)
                .strafeTo(new Vector2d(24*2.7, 8), Inches)
                .strafeTo(new Vector2d(24*2.7, 24*2.2+1), Inches)
                .strafeTo(new Vector2d(24*2.7, 8), Inches)
                .strafeTo(new Vector2d(24*3.1, 8), Inches)
                .strafeTo(new Vector2d(24*3.1, 24*2-2), Inches)
                .strafeTo(new Vector2d(24*3.1-6, 24*2-2-6), Inches)
                .addTask();

        add(robot.outtakeGrip.tasks.close());

        add(robot.drive.makeTrajectory(new Pose2d(24*3.1-6, 24*2-2-6, Math.toRadians(270)), currentPoseMap)
                .strafeToLinearHeading(new Vector2d(14+13, 0), Inches, 180, Degrees)
                .build()
                .with(robot.lift.tasks.goTo(368))
        );

        add(robot.lift.tasks.goTo(425));
    }
}