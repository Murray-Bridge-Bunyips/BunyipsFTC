package org.firstinspires.ftc.teamcode.autonomous;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

/**
 * Moves forward by 1 field tile. Emergency auto if all else fails ...
 *
 * @author Lucas Bubner, 2025
 */
@Autonomous(name = "Move Forward by 1 Field Tile")
public class MoveForward extends AutonomousBunyipsOpMode {
    private final Jonas robot = new Jonas();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        robot.drive.setPose(Geometry.zeroPose()); // using relative positioning
        robot.drive.makeTrajectory()
                .lineToX(1, FieldTile)
                .addTask();
    }
}
