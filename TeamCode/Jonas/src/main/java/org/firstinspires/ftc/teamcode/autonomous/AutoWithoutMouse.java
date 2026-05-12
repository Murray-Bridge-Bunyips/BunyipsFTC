package org.firstinspires.ftc.teamcode.autonomous;

import androidx.annotation.Nullable;

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
        //TODO: Get the robot to move forward
    }
}
