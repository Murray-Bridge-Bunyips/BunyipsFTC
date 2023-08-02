package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

/**
 * This OpMode is a test of the new SDK v8.2 wrappers. This is not to be used for any other
 * purpose, as it does not use the new SDK v8.2 wrappers in a way that is consistent with the
 * BunyipsOpMode ecosystem.
 * @author Lucas Bubner, 2023
 */
@TeleOp(name="VisionMinReproc", group="TEST")
//@Disabled
public class VisionMinReproc extends LinearOpMode {
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    private CameraName cam;

    @Override
    public void runOpMode() throws InterruptedException {
        cam = hardwareMap.get(WebcamName.class, "Webcam");
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(cam, aprilTag);
        waitForStart();
        while (opModeIsActive()) {
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            telemetry.addData("Detections: ", currentDetections.size());
            telemetry.update();
        }
    }
}
