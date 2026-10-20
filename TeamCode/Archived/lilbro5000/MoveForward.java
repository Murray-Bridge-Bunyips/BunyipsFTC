package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
    private final Lilbro5000 robot = new Lilbro5000();

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
