package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public interface Encoder extends DcMotor {

    /**
     * Enable encoder and begin tracking location (Ensure to reset to zero unless intended)
     */
    void enableTracking();

    /**
     * Disable encoder and keep last saved position
     */
    void disableTracking();

    /**
     * Reset encoder position to zero
     */
    void resetTracking();

    /**
     * Get the distance travelled by the encoder since the last reset and if tracking was enabled
     * @return millimetres indicating how far the encoder has travelled
     */
    double getTravelledMM(double wheel_diameter_mm, int ticks_per_revolution);

    /**
     * Get the reading from the encoder
     * @return encoder value
     */
    double getEncoderReading();
}
