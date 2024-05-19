package org.murraybridgebunyips.cellphone.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.tasks.GetTriPositionContourTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.cellphone.components.CellphoneConfig;
import org.murraybridgebunyips.common.centerstage.vision.RedTeamProp;

/**
 * Test triple spike mark positions.
 */
@TeleOp(name = "Tri Position Vision")
public class CellphoneTriPositionTask extends BunyipsOpMode {
    private final CellphoneConfig config = new CellphoneConfig();
    private Vision visionB;
//    private Vision visionF;
    private GetTriPositionContourTask task;

    @Override
    protected void onInit() {
        config.init(this);
        visionB = new Vision(config.cameraB);
        RedTeamProp proc = new RedTeamProp();
        visionB.init(proc);
        visionB.start(proc);
        visionB.startPreview();
        task = new GetTriPositionContourTask(proc);
        setInitTask(task);
    }

    @Override
    protected void activeLoop() {
        addTelemetry(task.getPosition());
        addTelemetry(visionB.getAllData());
    }
}
