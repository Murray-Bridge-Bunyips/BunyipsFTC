package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsSubsystem;

/**
 * Primary TeleOp.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Your Name, Year // TODO: Set this to your name and the current year!
 */
@TeleOp(name = "TeleOp")
public class MainTeleOp extends BunyipsOpMode {
    private final lilbro5000 robot = new lilbro5000();

    @Override
    protected void onInit() {
        robot.init();
        // TODO: Add any additional initialisation code here

    }

    @Override
    protected void activeLoop() {
        // TODO: Add your active looping code here

        BunyipsSubsystem.updateAll();
    }
}
