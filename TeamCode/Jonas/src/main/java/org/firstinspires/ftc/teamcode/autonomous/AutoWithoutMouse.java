package org.firstinspires.ftc.teamcode.autonomous;


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
        robot.drive.setPower(new PoseVelocity2d(new Vector2d(1, 0), 0));
    }
}
