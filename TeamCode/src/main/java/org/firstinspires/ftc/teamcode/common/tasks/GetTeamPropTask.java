package org.firstinspires.ftc.teamcode.common.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Dbg;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;

/**
 * Task for detecting on which spike the Team Prop is placed on.
 * This is measured from the starting position, with the camera facing towards the Spike Marks.
 *
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
 */
public class GetTeamPropTask extends Task {
    private final Vision vision;
    private TeamProp teamProp;
    private boolean initFired;
    private TeamProp.Positions position;

    public TeamProp.Positions getPosition() {
        return position;
    }

    public GetTeamPropTask(@NonNull BunyipsOpMode opMode, Vision vision) {
        super(opMode);
        this.vision = vision;
    }

    public GetTeamPropTask(@NonNull BunyipsOpMode opMode, Vision vision, TeamProp teamProp) {
        super(opMode);
        this.vision = vision;
        this.teamProp = teamProp;
    }

    /**
     * Late init method for dynamically installing a TeamProp processor
     * @param teamProp TeamProp Processor to use
     */
    public void setTeamProp(TeamProp teamProp) {
        if (initFired)
            throw new IllegalStateException("TeamProp has been already set, late init is no longer possible.");
        this.teamProp = teamProp;
    }

    @Override
    public void init() {
        if (teamProp == null) {
            Dbg.log("Cannot start Vision processing yet. Waiting...");
            return;
        }
        initFired = true;
        // Assumes VisionPortal is already initialised and running with teamProp as one of the processors
        // If this is not the case, the task will fail
        if (!vision.isInitialised()) {
            throw new IllegalStateException("Vision has not been initialised!");
        }
        if (!vision.getAttachedProcessors().contains(teamProp)) {
            throw new IllegalStateException("Vision has not attached the TeamProp processor using init()");
        }
        // TeamProp is aboard the processor, we're ready to go
        vision.start(teamProp);
    }

    @Override
    public void run() {
        if (teamProp == null) {
            // We can't do anything meaningful right now
            return;
        }
        if (!initFired) {
            // TeamProp is available and we should try to initialise it now
            init();
        }
        teamProp.tick();
        if (teamProp.getData().size() > 0) {
            // TeamProp will never have more than one data instance
            position = teamProp.getData().get(0).getPosition();
        }
        getOpMode().addTelemetry("Spike mark reading: %", position);
    }

    @Override
    public void onFinish() {
        // Pause TeamProp after we're done, we probably don't ever need it again but to remove
        // it from the processor list would have to involve reinitialising the VisionPortal
        vision.stop(teamProp);
    }

    @Override
    public boolean isTaskFinished() {
        // Init-task timing only
        return false;
    }
}