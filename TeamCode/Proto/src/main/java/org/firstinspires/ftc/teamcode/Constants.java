package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
@SuppressWarnings("MissingJavadoc")
public final class Constants {
    public static final double i_INTAKE = 1;
    public static final double i_EJECT = -1;
    public static double cl_kP = 0.004;
    public static double cl_kG = 0.01;
    public static double cl_TPS = 2000;
    public static long cl_MAX = 5700;
    public static double cr_MIN = 0;
    public static double cr_MAX = 1;

    private Constants() {
    }
}