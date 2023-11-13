package org.firstinspires.ftc.teamcode.common.roadrunner.drive;

public class TrackingWheelLocalizerCoefficients {

    public double TICKS_PER_REV;
    public double WHEEL_RADIUS = 2; // in
    public double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed
    public double LATERAL_DISTANCE = 10; // in; distance between the left and right wheels
    public double FORWARD_OFFSET = 4; // in; offset of the lateral wheel

    public static class TrackingWheelLocalizerCoefficientsBuilder {

        private final TrackingWheelLocalizerCoefficients trackingWheelCoefficients;

        public TrackingWheelLocalizerCoefficientsBuilder() {
            trackingWheelCoefficients = new TrackingWheelLocalizerCoefficients();
        }

        public TrackingWheelLocalizerCoefficientsBuilder setTicksPerRev(double ticksPerRev) {
            trackingWheelCoefficients.TICKS_PER_REV = ticksPerRev;
            return this;
        }

        public TrackingWheelLocalizerCoefficientsBuilder setWheelRadius(double wheelRadius) {
            trackingWheelCoefficients.WHEEL_RADIUS = wheelRadius;
            return this;
        }

        public TrackingWheelLocalizerCoefficientsBuilder setGearRatio(double gearRatio) {
            trackingWheelCoefficients.GEAR_RATIO = gearRatio;
            return this;
        }

        public TrackingWheelLocalizerCoefficientsBuilder setLateralDistance(double lateralDistance) {
            trackingWheelCoefficients.LATERAL_DISTANCE = lateralDistance;
            return this;
        }

        public TrackingWheelLocalizerCoefficientsBuilder setForwardOffset(double forwardOffset) {
            trackingWheelCoefficients.FORWARD_OFFSET = forwardOffset;
            return this;
        }

        public TrackingWheelLocalizerCoefficients build() {
            return trackingWheelCoefficients;
        }
    }
}
