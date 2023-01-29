package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * OpMode Abstract class that offers additional abstraction for opMode developers
 * including catch-all error handling and phased code execution.
 * Small modifications made by Lucas Bubner, FTC 15215
 */
public abstract class BunyipsOpMode extends LinearOpMode {

    protected MovingAverageTimer movingAverageTimer;
    protected long loopCount = 0;
    private boolean operationsCompleted;

    /**
     * Implement this method to define the code to run when the Init button is pressed on the Driver Station.
     */
    abstract protected void onInit();

    /**
     * Override to this method to allow code to run in a loop AFTER onInit has completed, until
     * start is pressed on the Driver Station or true is returned to this method.
     * If not implemented, the opMode will continue on as normal and wait for start.
     */
    protected boolean onInitLoop() throws InterruptedException { return true; }

    /**
     * Override to this method to allow code to execute once after all initialisation has finished.
     * Note if a task is running in an onInitLoop and start is pressed, this code will still be executed.
     */
    protected void onInitDone() throws InterruptedException {}

    /**
     * Override to this method to perform one time operations after start is pressed.
     * Unlike onInitDone, this will only execute once play is hit and not when initialisation is done.
     */
    protected void onStart() throws InterruptedException {}

    /**
     * Override to this method to perform one time operations after the activeLoop finishes
     */
    protected void onStop() throws InterruptedException {}

    /**
     * Implement this method to define the code to run when the Start button is pressed on the Driver station.
     * This method will be called on each hardware cycle.
     *
     * @throws InterruptedException
     */
    abstract protected void activeLoop() throws InterruptedException;


    /**
     * Override this method only if you need to do something outside of onInit() and activeLoop()
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {

        try {
            try {
                setup();
                onInit();

                while (opModeInInit()) {
                    if (onInitLoop()) break;
                }

                onInitDone();

                telemetry.addData("BunyipsOpMode Status", "INIT COMPLETE -- PLAY WHEN READY.");
                telemetry.update();

            } catch (Throwable e) {
                ErrorUtil.handleCatchAllException(e, telemetry);
            }

            waitForStart();
            clearTelemetryData();
            movingAverageTimer.reset();
            onStart();

            while (opModeIsActive() && !operationsCompleted) {

                try {
                    activeLoop();
                    loopCount++;
                } catch (InterruptedException ie) {
                    throw ie;
                } catch (Throwable e) {
                    ErrorUtil.handleCatchAllException(e, telemetry);
                }

                movingAverageTimer.update();
                telemetry.update();
                idle();
            }

            // Wait for user to hit stop
            while (opModeIsActive()) {
                idle();
            }

        } finally {
            onStop();
        }
    }

    // One-time setup for operations that need to be done for the opMode
    private void setup() {
        movingAverageTimer = new MovingAverageTimer(100);
    }

    /**
     * Clear data from the telemetry cache
     */
    public void clearTelemetryData() {
        if (telemetry.isAutoClear()) {
            telemetry.clear();
        } else {
            telemetry.clearAll();
        }
        if (opModeIsActive()) {
            idle();
        }
    }

    /**
     * Call to prevent hardware loop from calling activeLoop(), indicating an OpMode that is finished.
     */
    protected void setOperationsCompleted() {
        this.operationsCompleted = true;
        telemetry.addData("BunyipsOpMode Status", "HALTED ACTIVELOOP. ALL OPERATIONS COMPLETED.");
    }

}
