package org.firstinspires.ftc.teamcode.common.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.vision.data.TfodData;
import org.firstinspires.ftc.teamcode.common.Vision;

import java.util.List;

/**
 * Task for obtaining the White Pixel randomisation mark for the autonomous period.
 * This task is to be run when the camera is facing the randomisation mark, and can be used
 * to determine if there is a Pixel in front of the camera or not. Paired with the result of the
 * task, the OpMode can map where the Pixel will be.
 * FTC 2023-2024 CENTERSTAGE
 *
 * @author Lucas Bubner, 2023
 */
public class GetWhitePixelTask extends Task {
    private final Vision vision;
    private final Aggression aggression;

    private int spikeFrames;
    private double confidence;

    /**
     * Updated by the task to indicate if a spike has been found
     */
    private volatile boolean foundSpike;

    public GetWhitePixelTask(@NonNull BunyipsOpMode opMode, Vision vision, Aggression aggression) {
        super(opMode);
        this.vision = vision;
        this.aggression = aggression;
    }

    public boolean hasFoundSpike() {
        return foundSpike;
    }

    @Override
    public void init() {
        super.init();
        // As this is the init-task, we can assume that the vision portal is not open yet
        // This task will also leave the portal OPEN for further usages of the vision system
        // For this we are using the default settings of TFOD for white spike detection
    }

    @Override
    public boolean isFinished() {
        switch (aggression) {
            case INSTANT:
                return super.isFinished() || foundSpike;
            case CAPTURE:
                if (foundSpike) {
                    spikeFrames++;
                } else {
                    spikeFrames = 0;
                }
                return super.isFinished() || (spikeFrames >= GetWhitePixelTask.SPIKE_FRAME_THRESHOLD && confidence >= GetWhitePixelTask.SPIKE_CONFIDENCE_THRESHOLD);
            case TIMEOUT:
            default:
                return super.isFinished();
        }
    }

    @Override
    public void run() {
//        vision.tick();
//        List<TfodData> tfodData = vision.getTfodData();
//        if (tfodData.size() == 0) {
//            foundSpike = false;
//            return;
//        }
//        for (TfodData data : tfodData) {
//            if (!data.getLabel().equals("Spike")) {
//                return;
//            }
//            foundSpike = true;
//            confidence = data.getConfidence();
//        }
    }

    @Override
    public void onFinish() {
        // We will not need TFOD anymore
//        vision.stop(Vision.Processors.TFOD);
    }

    enum Aggression {
        // Immediately report as finished if a spike is detected for one frame
        INSTANT,
        // Report as finished if a spike is detected under a set of criteria (confidence, frames)
        CAPTURE,
        // Never report as finished until the timeout, result will be determined at the end of the timeout
        TIMEOUT
    }

    /**
     * For use in CAPTURE mode, lock in the spike detection if it is detected for this many frames
     */
    public static int SPIKE_FRAME_THRESHOLD = 5;

    /**
     * For use in CAPTURE mode, lock in the spike detection if it is detected with this confidence
     */
    public static double SPIKE_CONFIDENCE_THRESHOLD = 0.95;
}
