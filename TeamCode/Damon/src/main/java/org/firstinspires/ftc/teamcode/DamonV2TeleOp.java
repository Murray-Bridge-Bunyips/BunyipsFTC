package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToPointDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
@Config
@TeleOp(name = "TeleOp")
public class DamonV2TeleOp extends BunyipsOpMode {
    public static PIDFCoefficients ALIGN_TO_POINT_PIDF_COEFFICIENTS = new PIDFCoefficients(2, 0, 0, 0);
    private final DamonV2 robot = new DamonV2();
    private PIDFController shooterPid;

    @Override
    protected void onInit() {
        robot.init();
        //shooterPid = robot.hw.shooter.getRunUsingEncoderController().pidf().get();
        //Check if startConfig in null
        //Check if its red or blue
        //Asign the goal varible


        StartingConfiguration.Position startConfig = Storage.memory().lastKnownStartingConfiguration;
        Vector2d goal;
        if(startConfig == null){
            goal = new Vector2d(-67, 62); //Red goal
        }
        else {
            if(startConfig.isRed()){ //if blue because the starting configs in the autos
                goal = new Vector2d(-67, -62);
            }
            else{ //if red
                goal = new Vector2d(-67, 62);
            }
        }

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> false);
        robot.drive.setDefaultTask(driveTask);



        //new HolonomicDriveTask(gamepad1, robot.drive).setAsDefaultTask();
        //gamepad1.button(RIGHT_BUMPER)
        //        .whileTrue(new AlignToPointDriveTask(() -> goal, gamepad1, robot.drive).withAlignmentOffset(Degrees.of(180)));

        gamepad1.button(X).or(gamepad2.button(X))
                .whileTrue(robot.intake.tasks.run(1))
                .whileTrue(robot.transferLeft.tasks.run(1)) .whileTrue(robot.transferRight.tasks.run(1));

        gamepad1.button(Y).or(gamepad2.axisGreaterThan(LEFT_TRIGGER, 0.9))
                .whileTrue(robot.shooter.tasks.run(1));



        gamepad1.button(A)
                .onTrue(robot.kicker.tasks.toggleBoth());

        gamepad1.button(B)
                .whileTrue(
                        new ParallelTaskGroup(
                        robot.shooter.tasks.run(0.85),
                                robot.transferLeft.tasks.run(1),
                                robot.transferLeft.tasks.run(1),
                                robot.intake.tasks.run(0.2)
                                ));


        //Need to add code that sets the angle of the hood according to what button was pressed

    }

    @Override
    protected void activeLoop() {
        //telemetry.addData("currentVelocity", shooterPid.getCurrentProcess());
        //telemetry.addData("targetVelocity", shooterPid.getSetpoint());
        //AlignToPointDriveTask.DEFAULT_CONTROLLER.setPIDF(ALIGN_TO_POINT_PIDF_COEFFICIENTS);

        Scheduler.update();
    }

}
