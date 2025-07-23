package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Milliseconds;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.ExportToBlocks;

import java.util.ArrayDeque;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.Hook;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.WaitTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dashboard;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Ref;
import dev.frozenmilk.util.cell.RefCell;

/**
 * myBlocks to BunyipsLib interface for Scout.
 *
 * @author Lucas Bubner, 2025
 */
public class API extends BlocksOpModeCompanion {
    private static final ArrayDeque<Task> actions = new ArrayDeque<>();
    private static final RefCell<Pose2d> lastSplice = Ref.of(Geometry.zeroPose());
    private static double distanceMultiplier = 1;
    private static double angleMultiplier = 1;

    @Hook(on = Hook.Target.POST_STOP)
    private static void cleanup() {
        actions.clear();
        lastSplice.accept(Geometry.zeroPose());
        distanceMultiplier = 1;
        angleMultiplier = 1;
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
                .strafeTo(lastSplice.get().times(new Vector2d(Inches.convertFrom(centimeters * distanceMultiplier, Centimeters), 0)))
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
            color = 320,
            comment = "Queues Counterclockwise in-place rotation movement by the desired angle in degrees.",
            heading = "queue Rotation",
            parameterLabels = "Degrees (Anti-clockwise, left)",
            parameterDefaultValues = "90"
    )
    public static void rotateCCW(double degrees) {
        Task task = Scout.instance.drive.makeTrajectory(lastSplice.get())
                .turn(degrees * angleMultiplier, Degrees)
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
            color = 20,
            comment = "Queues a pause or wait in the execution cycle for the desired amount of time in milliseconds.",
            heading = "queue Wait",
            parameterLabels = "Time (Milliseconds)",
            parameterDefaultValues = "500"
    )
    public static void pause(double milliseconds) {
        actions.add(new WaitTask(milliseconds, Milliseconds));
    }

    @ExportToBlocks(
            color = 50,
            comment = "Sets a multiplicative factor that will apply to all forward and backward distances hereon.",
            heading = "set Distance Multiplier",
            parameterLabels = "Multiplier (Dist.)",
            parameterDefaultValues = "1"
    )
    public static void setDistanceMultiplier(double distanceMultiplier) {
        API.distanceMultiplier = distanceMultiplier;
    }

    @ExportToBlocks(
            color = 60,
            comment = "Sets a multiplicative factor that will apply to all rotate angles hereon.",
            heading = "set Angle Multiplier",
            parameterLabels = "Multiplier (Ang.)",
            parameterDefaultValues = "1"
    )
    public static void setAngleMultiplier(double angleMultiplier) {
        API.angleMultiplier = angleMultiplier;
    }

    @ExportToBlocks(
            color = 123,
            comment = "Blocking executes all queued API tasks after start and terminate the OpMode on completion.",
            heading = "Execute drive actions"
    )
    public static void go() {
        Dashboard.USING_SYNCED_PACKETS = true;
        linearOpMode.telemetry.addData("Queue", actions.toString());
        linearOpMode.telemetry.update();
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
            linearOpMode.telemetry.addData("Executing", current.toVerboseString());
            linearOpMode.telemetry.update();
            Dashboard.sendAndClearSyncedPackets();
        }
    }
}
