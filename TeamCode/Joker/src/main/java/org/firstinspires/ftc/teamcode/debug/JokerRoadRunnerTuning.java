package org.firstinspires.ftc.teamcode.debug;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;
import org.firstinspires.ftc.teamcode.Joker;

@TeleOp
public class JokerRoadRunnerTuning extends RoadRunnerTuningOpMode {
    private final Joker robot = new Joker();

    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        robot.init();
        return robot.drive;
    }
}
