package org.firstinspires.ftc.teamcode.common;

public abstract class Deadwheel implements Encoder {

    volatile double position = 0.0;

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
    public void disableTracking() {
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
    public double getTravelledMM(double wheel_diameter_mm, int ticks_per_revolution) {
        return (Math.PI * wheel_diameter_mm) * (this.getEncoderReading() / ticks_per_revolution) + position;
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
}
