package org.firstinspires.ftc.teamcode.teleop;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;

public class JonasRRTuning extends RoadRunnerTuningOpMode {
    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        Jonas jonas = new Jonas();
        jonas.init();
        return jonas.drive;
    }
}
