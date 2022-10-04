package org.firstinspires.ftc.teamcode.proto.config;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;

public class ProtoDrive extends BunyipsComponent {

    private BunyipsOpMode opmode;
    private DcMotorEx bl;
    private DcMotorEx br;
    private DcMotorEx fl;
    private DcMotorEx fr;
    private CameraOp cam;

    private final Telemetry.Item item;
    private boolean showTelemetry = true;

    public ProtoDrive(BunyipsOpMode opMode,
                      DcMotorEx bl, DcMotorEx br, DcMotorEx fl, DcMotorEx fr,
                      CameraOp cam, boolean showTelemetry) {
        super(opMode);
        this.bl = bl;
        this.br = br;
        this.fl = fl;
        this.fr = fr;
        this.cam = cam;
        if (showTelemetry) {
            item = opMode.telemetry.addData("Add telemetry","Data here");
            item.setRetained(true);
        } else {
            item = null;
        }
    }

    public void update() {
        if (cam.vuforiaEnabled) {
            vuTick();
        }
        // Put motor updates here
    }

    private void vuTick() {
        OpenGLMatrix VuforiaMatrix = cam.targetRawMatrix();
        VectorF translation = VuforiaMatrix.getTranslation();
        Orientation rotation = Orientation.getOrientation(VuforiaMatrix, EXTRINSIC, XYZ, DEGREES);
    }

}
