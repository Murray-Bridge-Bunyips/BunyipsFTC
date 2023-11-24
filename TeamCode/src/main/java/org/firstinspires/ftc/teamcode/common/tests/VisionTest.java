package org.firstinspires.ftc.teamcode.common.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Dbg;
import org.firstinspires.ftc.teamcode.common.UserSelection;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.cameras.C920;
import org.firstinspires.ftc.teamcode.common.vision.AprilTag;
import org.firstinspires.ftc.teamcode.common.vision.FtcDashboardBitmap;
import org.firstinspires.ftc.teamcode.common.vision.Processor;
import org.firstinspires.ftc.teamcode.common.vision.TFOD;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;

import java.util.ArrayList;

import kotlin.Unit;

/**
 * Test Vision processor detections and data throughput
 * Compatible with all robots with a hardware device webcam "webcam"
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "COMMON: Vision Test")
//@Disabled
@Config
public class VisionTest extends BunyipsOpMode {
    private Vision vision;
    private Telemetry.Item i;
    private final UserSelection<Procs> proc = new UserSelection<>(this, this::callback, Procs.values());

    public static int RED_ELEMENT_R = 175;
    public static int RED_ELEMENT_G = 27;
    public static int RED_ELEMENT_B = 2;

    public static int BLUE_ELEMENT_R = 26;
    public static int BLUE_ELEMENT_G = 57;
    public static int BLUE_ELEMENT_B = 178;

    @SuppressWarnings("rawtypes")
    private Unit callback(Procs selection) {
        if (selection == null) {
            return Unit.INSTANCE;
        }
        ArrayList<Processor> processors = new ArrayList<>();
        switch (selection) {
            case TFOD:
                TFOD tf = new TFOD();
                processors.add(tf);
                break;
            case APRILTAG:
                AprilTag at = new AprilTag(new C920());
                processors.add(at);
                break;
            case TEAMPROP_RED:
                TeamProp tpr = new TeamProp(RED_ELEMENT_R, RED_ELEMENT_G, RED_ELEMENT_B);
                processors.add(tpr);
                break;
            case TEAMPROP_BLUE:
                TeamProp tpb = new TeamProp(BLUE_ELEMENT_R, BLUE_ELEMENT_G, BLUE_ELEMENT_B);
                processors.add(tpb);
                break;
        }
        // Always add the FtcDashboardBitmap processor
        FtcDashboardBitmap fdb = new FtcDashboardBitmap();
        processors.add(fdb);

        vision.init(processors.toArray(new Processor[0]));
        vision.start(processors.toArray(new Processor[0]));

        FtcDashboard.getInstance().startCameraStream(fdb, 0);
        i = addRetainedTelemetry("Camera Stream available.");
        return Unit.INSTANCE;
    }

    @Override
    protected boolean onInitLoop() {
        return !proc.isAlive();
    }

    @Override
    protected void onInit() {
        try {
            WebcamName webcam = (WebcamName) hardwareMap.get("webcam");
            vision = new Vision(this, webcam);
        } catch (IllegalArgumentException e) {
            Dbg.error("VisionTest is missing a webcam called 'webcam'!");
            exit();
        }
        proc.start();
    }

    @Override
    protected void onStart() {
        if (vision == null) {
            exit();
        }
        removeTelemetryItems(i);
    }

    @Override
    protected void activeLoop() {
        vision.tickAll();
        addTelemetry(String.valueOf(vision.getAllData()));
    }

    @Override
    protected void onStop() {
        FtcDashboard.getInstance().stopCameraStream();
        vision.terminate();
    }

    private enum Procs {
        TFOD,
        APRILTAG,
        TEAMPROP_RED,
        TEAMPROP_BLUE
    }
}
