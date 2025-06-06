package org.firstinspires.ftc.teamcode.tuning;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Scout;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;

/**
 * RoadRunner tuning OpMode.
 */
// OpMode is registered in the Registrar class
public class RoadRunner extends RoadRunnerTuningOpMode {
    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        return Scout.instance.drive;
    }
}
