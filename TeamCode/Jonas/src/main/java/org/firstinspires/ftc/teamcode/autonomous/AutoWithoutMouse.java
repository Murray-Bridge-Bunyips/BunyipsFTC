package org.firstinspires.ftc.teamcode.autonomous;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Move Forward", preselectTeleOp = "TeleOp")
public class AutoWithoutMouse extends AutonomousBunyipsOpMode {
    private final Jonas robot = new Jonas();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {

    }

    @Override
    protected void periodic() {
        robot.drive.setPower(new PoseVelocity2d(new Vector2d(1, 0), 0));
        wait(Seconds.of(1));
        robot.drive.setPower(new PoseVelocity2d(new Vector2d(0, 0), 0));
    }
}
