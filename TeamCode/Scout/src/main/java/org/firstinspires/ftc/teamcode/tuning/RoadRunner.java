package org.firstinspires.ftc.teamcode.tuning;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Scout;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;

/**
 * RoadRunner tuning OpMode.
 */
@TeleOp(name = "RoadRunner Tuning", group = "tuning")
public class RoadRunner extends RoadRunnerTuningOpMode {
    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        return Scout.instance.drive;
    }
}
