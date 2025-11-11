package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.R;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.Sound;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;

/**
 * Random tests
 */
@TeleOp
public class CellphoneTests extends BunyipsOpMode {
    private final Sound sound = new Sound(R.raw.vineboom);

    @Override
    protected void onInit() {
    }

    @Override
    protected void activeLoop() {
        if (gamepad1.getDebounced(Controls.A))
            sound.play();
    }
}
