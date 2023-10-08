package org.firstinspires.ftc.teamcode.common.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.TfodData;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.List;

/**
 * Init-task runner for obtaining the White Pixel randomisation mark for the autonomous period.
 * FTC 2023-2024 CENTERSTAGE
 * @author Lucas Bubner, 2023
 */
public class GetSpikeMarkTask extends Task implements AutoTask {
    private Vision vision;
    private SpikePosition position = SpikePosition.UNKNOWN;

    enum SpikePosition {
        LEFT,
        RIGHT,
        CENTER,
        // Camera has not attempted to detect the spike
        UNKNOWN,
        // Camera has attempted to detect the spike but has not found it
        UNDETECTED
    }

    public SpikePosition getSpikePosition() {
        return position;
    }

    public GetSpikeMarkTask(@NonNull BunyipsOpMode opMode, Vision vision) {
        super(opMode);
        this.vision = vision;
    }

    @Override
    public void init() {
        super.init();
        // As this is the init-task, we can assume that the vision portal is not open yet
        // This task will also leave the portal OPEN for further usages of the vision system
        vision.init(Vision.Processors.TFOD);
        vision.start(Vision.Processors.TFOD);
    }

    @Override
    public boolean isFinished() {
        // Unsure whether to let this task finish if the spike is detected; there may be a need to
        // keep the vision system going as the human operator may not have adjusted the spike position
        // and init-phase is required before randomisation. This may cause incorrect locking of the spike.
        // This is similar to PowerPlay and the signal, however, you could rotate the Signal to
        // an angle where the camera cannot see it, whereas the spike is always visible
        return super.isFinished();
//        return super.isFinished() || position != SpikePosition.UNKNOWN;
    }

    @Override
    public void run() {
        if (isFinished()) {
            // After detection we may have a long while to wait before we need to use the vision system again
            // Therefore we will pause all vision processing until we need it again
            vision.stop();
            if (position == SpikePosition.UNKNOWN) {
                position = SpikePosition.UNDETECTED;
            }
            return;
        }
        vision.tick();
        List<TfodData> tfodData = vision.getTfodData();
        if (tfodData.size() == 0) {
            return;
        }
        for (TfodData data : tfodData) {
            if (data.getLabel().equals("Spike")) {
                double x = data.getHorizontalTranslation();
                if (x < 0.33) {
                    position = SpikePosition.LEFT;
                } else if (x > 0.66) {
                    position = SpikePosition.RIGHT;
                } else {
                    position = SpikePosition.CENTER;
                }
                return;
            }
        }
    }
}
