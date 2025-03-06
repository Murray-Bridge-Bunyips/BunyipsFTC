package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.GetDualSplitContourTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;
import org.firstinspires.ftc.teamcode.Cellphone;
import org.firstinspires.ftc.teamcode.common.centerstage.vision.RedTeamProp;

/**
 * Test triple spike mark positions.
 */
@TeleOp(name = "Tri Position Vision")
public class CellphoneTriPositionTask extends BunyipsOpMode {
    private Vision visionB;
    //    private Vision visionF;
    private GetDualSplitContourTask task;

    @Override
    protected void onInit() {
        visionB = new Vision(Cellphone.instance.cameraB);
        RedTeamProp proc = new RedTeamProp();
        visionB.init(proc);
        visionB.start(proc);
        visionB.startPreview();
        task = new GetDualSplitContourTask(proc);
        setInitTask(task);
    }

    @Override
    protected void activeLoop() {
        telemetry.add(task.getPosition());
        telemetry.add(visionB.getAllData());
    }
}
