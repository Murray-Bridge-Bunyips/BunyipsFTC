package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;

@TeleOp(name = "RoadRunner Tuning")
public class RoadRunnerTuning extends RoadRunnerTuningOpMode {
    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        Lilbro5000 robot = new Lilbro5000(); // Assuming @RobotConfig.AutoInit is not being used
        robot.init();
        return robot.drive; // Assumes `robot.drive` is exposing a tunable RoadRunnerDrive using a standard Accumulator, with Localizer configured
    }
}