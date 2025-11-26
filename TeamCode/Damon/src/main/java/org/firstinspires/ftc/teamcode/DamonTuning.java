package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;

@TeleOp(name = "RoadRunner Tuning")
public class DamonTuning extends RoadRunnerTuningOpMode {
    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        Damon damon = new Damon(); // Assuming @RobotConfig.AutoInit is not being used
        damon.init();
        return damon.drive; // robot.drive is using a standard Accumulator, with Localizer configured
    }
}