package org.firstinspires.ftc.teamcode.common.roadrunner.drive;

public class TwoWheelTrackingLocalizerCoefficients {
    public double TICKS_PER_REV;
    public double WHEEL_RADIUS = 2; // in
    public double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public double PARALLEL_X; // X is the up and down direction
    public double PARALLEL_Y; // Y is the strafe direction

    public double PERPENDICULAR_X;
    public double PERPENDICULAR_Y;

    public static class TwoWheelTrackingLocalizerCoefficientsBuilder {

        private final TwoWheelTrackingLocalizerCoefficients twoWheelTrackingCoefficients;

        public TwoWheelTrackingLocalizerCoefficientsBuilder() {
            twoWheelTrackingCoefficients = new TwoWheelTrackingLocalizerCoefficients();
        }

        public TwoWheelTrackingLocalizerCoefficientsBuilder setTicksPerRev(double ticksPerRev) {
            twoWheelTrackingCoefficients.TICKS_PER_REV = ticksPerRev;
            return this;
        }

        public TwoWheelTrackingLocalizerCoefficientsBuilder setWheelRadius(double wheelRadius) {
            twoWheelTrackingCoefficients.WHEEL_RADIUS = wheelRadius;
            return this;
        }

        public TwoWheelTrackingLocalizerCoefficientsBuilder setGearRatio(double gearRatio) {
            twoWheelTrackingCoefficients.GEAR_RATIO = gearRatio;
            return this;
        }

        public TwoWheelTrackingLocalizerCoefficientsBuilder setParallelX(double parallelX) {
            twoWheelTrackingCoefficients.PARALLEL_X = parallelX;
            return this;
        }

        public TwoWheelTrackingLocalizerCoefficientsBuilder setParallelY(double parallelY) {
            twoWheelTrackingCoefficients.PARALLEL_Y = parallelY;
            return this;
        }

        public TwoWheelTrackingLocalizerCoefficientsBuilder setPerpendicularX(double perpendicularX) {
            twoWheelTrackingCoefficients.PERPENDICULAR_X = perpendicularX;
            return this;
        }

        public TwoWheelTrackingLocalizerCoefficientsBuilder setPerpendicularY(double perpendicularY) {
            twoWheelTrackingCoefficients.PERPENDICULAR_Y = perpendicularY;
            return this;
        }

        public TwoWheelTrackingLocalizerCoefficients build() {
            return twoWheelTrackingCoefficients;
        }
    }

}
