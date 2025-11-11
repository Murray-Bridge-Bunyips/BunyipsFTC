package org.firstinspires.ftc.teamcode.common;

import androidx.annotation.Nullable;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import dev.frozenmilk.util.cell.RefCell;

import java.util.Arrays;
import java.util.Objects;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;

/**
 * Autonomous Selector OpMode to populate last known alliance and origin information and no-op until completion.
 * <p>
 * Useful for debugging or as a Do Nothing Auto default OpMode applicable to any season.
 *
 * @author Lucas Bubner, 2025
 */
@Autonomous(name = "Select Alliance and Do Nothing", group = "dash")
public class AllianceSelectorNoOp extends AutonomousBunyipsOpMode {
    @Override
    protected void onInitialise() {
        setOpModes(new Object[]{StartingConfiguration.Alliance.getEntries().toArray(), StartingConfiguration.Origin.getEntries().toArray()})
                .captionLayer(0, "SELECT KNOWN ALLIANCE")
                .captionLayer(1, "SELECT KNOWN ORIGIN (OPTIONAL)");
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        Object[] data = (Object[]) selectedOpMode.get();
        if (Arrays.stream(data).allMatch(Objects::isNull)) {
            telemetry.addRetained("Starting configuration not set. No Gamepad options provided.").h1().color("red");
            return;
        }
        Storage.memory().lastKnownStartingConfiguration = new StartingConfiguration.Position(
                (StartingConfiguration.Alliance) data[0],
                data[1] != null ? (StartingConfiguration.Origin) data[1] : StartingConfiguration.Origin.LEFT, // sane default
                Inches.zero(), Inches.zero(), Radians.zero() // arbitrary
        );
        telemetry.addRetained("Starting configuration set: {%, %}", data[0], data[1] != null ? data[1] : "UNKNOWN (default LEFT)").h1();
    }
}