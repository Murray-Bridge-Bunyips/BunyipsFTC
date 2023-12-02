package org.murraybridgebunyips.ftc.bunyipslib.cameras;

public abstract class CameraType {
    protected double fx;
    protected double fy;
    protected double cx;
    protected double cy;

    public double getFx() {
        return fx;
    }

    public double getFy() {
        return fy;
    }

    public double getCx() {
        return cx;
    }

    public double getCy() {
        return cy;
    }
}
