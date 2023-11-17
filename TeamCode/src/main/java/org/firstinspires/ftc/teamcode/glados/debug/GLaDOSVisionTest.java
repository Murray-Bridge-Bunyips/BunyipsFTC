package org.firstinspires.ftc.teamcode.glados.debug;

import android.graphics.Bitmap;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.UserSelection;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.cameras.C920;
import org.firstinspires.ftc.teamcode.common.vision.AprilTag;
import org.firstinspires.ftc.teamcode.common.vision.Processor;
import org.firstinspires.ftc.teamcode.common.vision.TFOD;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

import java.util.ArrayList;

import kotlin.Unit;

/**
 * Test Vision processor detections and data throughput
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLaDOS: Vision Test", group = "GLaDOS")
public class GLaDOSVisionTest extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private Vision vision;
    private Telemetry.Item i;
    private final UserSelection<Procs> proc = new UserSelection<>(this, this::callback, Procs.values());

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
            case TEAMPROP:
                TeamProp tp = new TeamProp(255, 0, 0);
                processors.add(tp);
                break;
            case ALL:
                tf = new TFOD();
                at = new AprilTag(new C920());
                tp = new TeamProp(255, 0, 0);
                processors.add(tf);
                processors.add(at);
                processors.add(tp);
                break;
        }
        vision.init(processors.toArray(new Processor[0]));
        vision.start(processors.toArray(new Processor[0]));
        i = addRetainedTelemetry("Camera Stream available.");
        return Unit.INSTANCE;
    }

    @Override
    protected boolean onInitLoop() {
        return !proc.isAlive();
    }

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        vision = new Vision(this, config.webcam);
        proc.start();
    }

    @Override
    protected void onStart() {
        removeTelemetryItems(i);
    }

    @Override
    protected void activeLoop() {
        if (vision == null) return;
        vision.tickAll();
        addTelemetry(String.valueOf(vision.getAllData()));
    }

    private enum Procs {
        TFOD,
        APRILTAG,
        TEAMPROP,
        ALL
    }
}
