package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.redLeft;

import java.util.AbstractList;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Hook;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToPointDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;

/**
 * Basic Command-based TeleOp.
 * This is modelled after the basic robot from the robot configuration page.
 */
@Config
@TeleOp(name = "TeleOp")
public class DamonTeleOp extends BunyipsOpMode {
    public static PIDFCoefficients ALIGN_TO_POINT_PIDF_COEFFICIENTS = new PIDFCoefficients(2, 0, 0, 0);
    private final Vector2d goal = new Vector2d(-62, 62); // TODO: always assumes on red and that the robot starts from the centre of the field
    private final Damon robot = new Damon();
    private PIDFController shooterPid;

    @Override
    protected void onInit() {
        robot.init();
        shooterPid = robot.hw.shooter.getRunUsingEncoderController().pidf().get();

        new HolonomicDriveTask(gamepad1, robot.drive).setAsDefaultTask();
        gamepad1.button(X)
                .whileTrue(robot.intake.tasks.run(1));
        gamepad1.button(A)
                .whileTrue(robot.intake.tasks.run(-1));
        gamepad1.button(Y)
                .whileTrue(robot.shooter.tasks.run(1));
        gamepad1.button(B).and(shooterPid::atSetpoint)
                .whileTrue(robot.transferWheel.tasks.run(1));

        gamepad1.button(RIGHT_BUMPER)
                // TODO: goal alignment
                .whileTrue(new AlignToPointDriveTask(() -> goal, gamepad1, robot.drive).withAlignmentOffset(Degrees.of(180)));
    }

    @Override
    protected void activeLoop() {
        telemetry.addData("currentVelocity", shooterPid.getCurrentProcess());
        telemetry.addData("targetVelocity", shooterPid.getSetpoint());
        AlignToPointDriveTask.DEFAULT_CONTROLLER.setPIDF(ALIGN_TO_POINT_PIDF_COEFFICIENTS);
        Scheduler.update();
    }
}