package org.firstinspires.ftc.teamcode.common.roadrunner.drive;

import com.acmerobotics.roadrunner.control.PIDCoefficients;

public class TankCoefficients {
    public PIDCoefficients AXIAL_PID = new PIDCoefficients(0, 0, 0);
    public PIDCoefficients CROSS_TRACK_PID = new PIDCoefficients(0, 0, 0);
    public PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);
    public double VX_WEIGHT = 1;
    public double OMEGA_WEIGHT = 1;

    public static class TankCoefficientsBuilder {

        private final TankCoefficients tankCoefficients;

        public TankCoefficientsBuilder() {
            tankCoefficients = new TankCoefficients();
        }

        public TankCoefficientsBuilder setAxialPID(PIDCoefficients axialPID) {
            tankCoefficients.AXIAL_PID = axialPID;
            return this;
        }

        public TankCoefficientsBuilder setCrossTrackPID(PIDCoefficients crossTrackPID) {
            tankCoefficients.CROSS_TRACK_PID = crossTrackPID;
            return this;
        }

        public TankCoefficientsBuilder setHeadingPID(PIDCoefficients headingPID) {
            tankCoefficients.HEADING_PID = headingPID;
            return this;
        }

        public TankCoefficientsBuilder setVXWeight(double vxWeight) {
            tankCoefficients.VX_WEIGHT = vxWeight;
            return this;
        }

        public TankCoefficientsBuilder setOmegaWeight(double omegaWeight) {
            tankCoefficients.OMEGA_WEIGHT = omegaWeight;
            return this;
        }

        public TankCoefficients build() {
            return tankCoefficients;
        }
    }
}
