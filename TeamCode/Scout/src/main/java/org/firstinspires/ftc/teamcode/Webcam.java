package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Exceptions;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.processors.AprilTag;

/**
 * Webcam streaming and testing.
 */
@TeleOp(name = "Webcam Streaming", group = "z")
public class Webcam extends BunyipsOpMode {
    @Override
    protected void onInit() {
        Vision vision = Scout.instance.optionalVision;
        if (vision == null)
            throw new Exceptions.EmergencyStop("Webcam not configured!");
        AprilTag at = new AprilTag();
        vision.init(at);
        vision.start(at);
        vision.startPreview();
    }

    @Override
    protected void activeLoop() {
        telemetry.add(Scout.instance.optionalVision.getAllData());
        Scout.instance.optionalVision.update();
    }
}
