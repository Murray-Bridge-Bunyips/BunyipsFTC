package org.firstinspires.ftc.teamcode.common;

public abstract class Deadwheel implements Encoder {

    volatile double position = 0.0;

    // These inputs are for the BQLZR 600P/R (?) encoders
    private static final int WHEEL_DIAMETER_MM = 0;
    private static final int TICKS_PER_REVOLUTION = 0;

    /**
     * Enable encoder and begin tracking location (Ensure to reset to zero unless intended)
     */
    @Override
    public void enableTracking() {
        this.setMode(RunMode.RUN_USING_ENCODER);
    }

    /**
     * Disable encoder and keep last saved position
     */
    @Override
    public synchronized void disableTracking() {
        position += this.getCurrentPosition();
        this.setMode(RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Reset encoder position to zero
     */
    @Override
    public void resetTracking() {
        this.setMode(RunMode.STOP_AND_RESET_ENCODER);
        position = 0.0;
    }

    /**
     * Get the distance travelled by the encoder since the last reset and if tracking was enabled
     *
     * @return millimetres indicating how far the encoder has travelled
     */
    @Override
    public double getTravelledMM() {
        return (Math.PI * WHEEL_DIAMETER_MM) * (this.getEncoderReading() / TICKS_PER_REVOLUTION) + position;
    }

    /**
     * Get the reading from the encoder
     *
     * @return encoder value
     */
    @Override
    public double getEncoderReading() {
        return this.getCurrentPosition() + position;
    }

    /**
     * Return whether the encoders have reached a goal
     *
     * @param goal Goal in mm
     * @return boolean expression whether the encoders read a goal has been completed
     */
    @Override
    public boolean targetReached(double goal) {
        return Math.abs(this.getTravelledMM()) >= Math.abs(goal);
    }
}
