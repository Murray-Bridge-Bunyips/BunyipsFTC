package org.firstinspires.ftc.teamcode.proto.config;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

public class ProtoConfig extends RobotConfig {

    // Add declarations here
    public CameraName webcam;
    public int tfodMonitorViewId;

    public static ProtoConfig newConfig(HardwareMap hardwareMap, Telemetry telemetry) {
        ProtoConfig config = new ProtoConfig();
        config.init(hardwareMap, telemetry);
        return config;
    }

    @Override
    protected void init(HardwareMap hardwareMap, Telemetry telemetry) {
        setTelemetry(telemetry);

        // Add configurations here
        try {
            // Using manual error catching as the hardwareMap for getHardwareOn does not include
            // webcam configurations
            webcam = hardwareMap.get(WebcamName.class, "Webcam");
            tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        } catch (Exception e) {
            telemetry.addLine("Error configuring device 'Webcam'. Check connections.");
        }


        telemetry.addData("BunyipsOpMode Initialisation", "Complete");
        telemetry.update();
    }
}
