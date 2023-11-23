package org.firstinspires.ftc.teamcode.wheatley.debug;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.UserSelection;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.cameras.C920;
import org.firstinspires.ftc.teamcode.common.vision.AprilTag;
import org.firstinspires.ftc.teamcode.common.vision.FtcDashboardBitmap;
import org.firstinspires.ftc.teamcode.common.vision.Processor;
import org.firstinspires.ftc.teamcode.common.vision.TFOD;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

import java.util.ArrayList;

import kotlin.Unit;

/**
 * Vision test for Wheatley
 * 
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
 */
@TeleOp(name = "WHEATLEY: Vision Test", group = "WHEATLEY")
public class WheatleyVisionTest extends BunyipsOpMode {
    private WheatleyConfig config = new WheatleyConfig();
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
                TeamProp tp = new TeamProp(0, 100, 100);
                processors.add(tp);
                break;
            case ALL:
                tf = new TFOD();
                at = new AprilTag(new C920());
                tp = new TeamProp(0, 0, 255);
                processors.add(tf);
                processors.add(at);
                processors.add(tp);
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
        config = (WheatleyConfig) RobotConfig.newConfig(this, config, hardwareMap);
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

    @Override
    protected void onStop() {
        FtcDashboard.getInstance().stopCameraStream();
        vision.terminate();
    }

    private enum Procs {
        TFOD,
        APRILTAG,
        TEAMPROP,
        ALL
    }
}

