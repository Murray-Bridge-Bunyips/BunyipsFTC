package org.firstinspires.ftc.teamcode.common.roadrunner.drive;

import com.acmerobotics.roadrunner.control.PIDCoefficients;

/**
 * Constants for RoadRunner mecanum drive.
 * Reworked to use a builder for multiple robot configurations.
 */
public class MecanumCoefficients {
    public PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(0, 0, 0);
    public PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);
    public double LATERAL_MULTIPLIER = 1;
    public double VX_WEIGHT = 1;
    public double VY_WEIGHT = 1;
    public double OMEGA_WEIGHT = 1;

    public static class MecanumCoefficientsBuilder {

        private final MecanumCoefficients mecanumCoefficients;

        public MecanumCoefficientsBuilder() {
            mecanumCoefficients = new MecanumCoefficients();
        }

        public MecanumCoefficientsBuilder setTranslationalPID(PIDCoefficients translationalPID) {
            mecanumCoefficients.TRANSLATIONAL_PID = translationalPID;
            return this;
        }

        public MecanumCoefficientsBuilder setHeadingPID(PIDCoefficients headingPID) {
            mecanumCoefficients.HEADING_PID = headingPID;
            return this;
        }

        public MecanumCoefficientsBuilder setLateralMultiplier(double lateralMultiplier) {
            mecanumCoefficients.LATERAL_MULTIPLIER = lateralMultiplier;
            return this;
        }

        public MecanumCoefficientsBuilder setVXWeight(double vxWeight) {
            mecanumCoefficients.VX_WEIGHT = vxWeight;
            return this;
        }

        public MecanumCoefficientsBuilder setVYWeight(double vyWeight) {
            mecanumCoefficients.VY_WEIGHT = vyWeight;
            return this;
        }

        public MecanumCoefficientsBuilder setOmegaWeight(double omegaWeight) {
            mecanumCoefficients.OMEGA_WEIGHT = omegaWeight;
            return this;
        }

        public MecanumCoefficients build() {
            return mecanumCoefficients;
        }
    }
}
