package org.firstinspires.ftc.teamcode.debug;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import org.firstinspires.ftc.teamcode.Cellphone;

/**
 * Second edition of the fake mecanum drive (meccanum droive)
 */
@TeleOp
public class CellphoneMeccanumDroive2 extends BunyipsOpMode {
    @Override
    protected void activeLoop() {
        Cellphone.instance.dummyDrive.setPower(Controls.vel(gamepad1.lsx, gamepad1.lsy, gamepad1.rsx));
        Cellphone.instance.dummyDrive.periodic();
        if (gamepad1.a) {
            Actions.runBlocking(Cellphone.instance.dummyDrive.makeTrajectory().lineToX(60).build());
        }
    }
}
