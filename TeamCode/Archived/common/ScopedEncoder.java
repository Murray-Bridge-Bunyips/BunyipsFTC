package org.murraybridgebunyips.bunyipslib;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.murraybridgebunyips.bunyipslib.external.units.Angle;
import org.murraybridgebunyips.bunyipslib.external.units.Distance;
import org.murraybridgebunyips.bunyipslib.external.units.Measure;

/**
 * Marker interface for encoders that are scoped to relative or absolute positions.
 *
 * @author Lucas Bubner, 2024
 */
public interface ScopedEncoder extends DcMotorEx {
    /**
     * Enable encoder and start tracking in the selected mode, which will also save a snapshot of the encoder position for relative tracking.
     *
     * @param mode the mode to track the encoder in
     */
    void track(@NonNull DcMotor.RunMode mode);

    /**
     * Reset encoder positions to zero. Useful when saved state is not needed or can be discarded.
     */
    void reset();

    /**
     * Get a movement reading in ticks from the encoder as per the scope.
     *
     * @param scope the scope to read the encoder in
     * @return encoder value relative to last [trackFromCurrentPosition] call, or since the last [reset] call
     */
    int getCurrentPosition(@NonNull Scope scope);

    /**
     * Get a movement reading in ticks from the encoder since the last [trackFromCurrentPosition]
     * Can use an optional parameter to use since [reset] position instead of [trackFromCurrentPosition]
     *
     * @return encoder value relative to last [trackFromCurrentPosition] call, or since the last [reset] call
     */
    @Override
    default int getCurrentPosition() {
        return getCurrentPosition(Scope.RELATIVE);
    }

    /**
     * Get the number of revolutions the encoder has travelled as per the scope.
     *
     * @param scope the scope to read the encoder in
     * @return revolutions indicating how far the encoder has travelled
     */
    Measure<Angle> getTravelledAngle(@NonNull Scope scope);

    /**
     * Get the number of revolutions the encoder has travelled since the last [trackFromCurrentPosition]
     * Can use an optional parameter to use since [reset] position instead of [trackFromCurrentPosition]
     *
     * @return revolutions indicating how far the encoder has travelled
     */
    default Measure<Angle> getTravelledAngle() {
        return getTravelledAngle(Scope.RELATIVE);
    }

    /**
     * Get a human distance of what the encoder has travelled as per the scope.
     *
     * @param scope the scope to read the encoder in
     * @return millimeters indicating how far the encoder has travelled
     */
    Measure<Distance> getTravelledDistance(@NonNull Scope scope);

    /**
     * Get a human distance of what the encoder has travelled since the last [trackFromCurrentPosition]
     * Can use an optional parameter to use since [reset] position instead of [trackFromCurrentPosition]
     *
     * @return millimeters indicating how far the encoder has travelled
     */
    default Measure<Distance> getTravelledDistance() {
        return getTravelledDistance(Scope.RELATIVE);
    }

    /**
     * Scope of the encoder tracking.
     */
    enum Scope {
        /**
         * Relative scope will return the encoder value since the last [trackFromCurrentPosition] call.
         */
        RELATIVE,

        /**
         * Global scope will return the encoder value since the last [reset] call.
         */
        GLOBAL
    }
}
