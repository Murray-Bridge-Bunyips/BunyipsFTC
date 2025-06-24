package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.ExportToBlocks;

import java.util.ArrayDeque;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.Hook;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Ref;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;
import dev.frozenmilk.util.cell.RefCell;

/**
 * myBlocks to BunyipsLib interface for Scout.
 *
 * @author Lucas Bubner, 2025
 */
public class API extends BlocksOpModeCompanion {
    private static final ArrayDeque<Task> actions = new ArrayDeque<>();
    private static final RefCell<Pose2d> lastSplice = Ref.of(Geometry.zeroPose());

    @Hook(on = Hook.Target.POST_STOP)
    private static void cleanup() {
        Storage.memory().lastKnownPosition = Geometry.zeroPose();
        lastSplice.accept(Geometry.zeroPose());
        actions.clear();
    }

    @ExportToBlocks(
            color = 177,
            comment = "Queues movement forward by the desired distance in centimeters.",
            heading = "queue Movement",
            parameterLabels = "Centimeters (Forward)",
            parameterDefaultValues = "30"
    )
    public static void moveForward(double centimeters) {
        Task task = Scout.instance.drive.makeTrajectory(lastSplice.get())
                .strafeTo(lastSplice.get().times(new Vector2d(Inches.convertFrom(centimeters, Centimeters), 0)))
                .build(lastSplice);
        actions.add(task);
    }

    @ExportToBlocks(
            color = 197,
            comment = "Queues movement backward by the desired distance in centimeters.",
            heading = "queue Movement",
            parameterLabels = "Centimeters (Backward)",
            parameterDefaultValues = "30"
    )
    public static void moveBackward(double centimeters) {
        moveForward(-centimeters);
    }

    @ExportToBlocks(
            color = 306,
            comment = "Queues Counterclockwise in-place rotation movement by the desired angle in degrees.",
            heading = "queue Rotation",
            parameterLabels = "Degrees (Anti-clockwise, left)",
            parameterDefaultValues = "90"
    )
    public static void rotateCCW(double degrees) {
        Task task = Scout.instance.drive.makeTrajectory(lastSplice.get())
                .turn(degrees, Degrees)
                .build(lastSplice);
        actions.add(task);
    }

    @ExportToBlocks(
            color = 340,
            comment = "Queues Clockwise in-place rotation movement by the desired angle in degrees.",
            heading = "queue Rotation",
            parameterLabels = "Degrees (Clockwise, right)",
            parameterDefaultValues = "90"
    )
    public static void rotateCW(double degrees) {
        rotateCCW(-degrees);
    }

    @ExportToBlocks(
            color = 123,
            comment = "Blocking executes all queued API tasks after start and terminate the OpMode on completion.",
            heading = "Execute drive actions"
    )
    public static void go() {
        linearOpMode.waitForStart();
        while (linearOpMode.opModeIsActive()) {
            Scout.instance.drive.update();
            Task current = actions.peekFirst();
            if (current == null)
                break;
            if (current.poll()) {
                actions.removeFirst();
                continue;
            }
            current.execute();
            linearOpMode.telemetry.addData("Runtime (s)", Mathf.round(linearOpMode.getRuntime(), 1));
            linearOpMode.telemetry.addData("Executing", current.toVerboseString());
            linearOpMode.telemetry.update();
        }
    }
}
