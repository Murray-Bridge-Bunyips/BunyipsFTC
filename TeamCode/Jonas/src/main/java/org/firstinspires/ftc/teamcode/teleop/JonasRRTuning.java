package org.firstinspires.ftc.teamcode.teleop;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;

@TeleOp(name = "RoadRunner Tuning")
public class JonasRRTuning extends RoadRunnerTuningOpMode {
    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        Jonas jonas = new Jonas();
        jonas.init();
        return jonas.drive;
    }
}
