package org.firstinspires.ftc.teamcode.bertie;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.ButtonControl;
import org.firstinspires.ftc.teamcode.common.MessageTask;
import org.firstinspires.ftc.teamcode.common.Task;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

@Autonomous(name = "<BERTIE> Bertie Auto test")
public class BertieAutonomous extends BunyipsOpMode {
    private BertieBunyipConfiguration config;
    private BertieBunyipDrive drive = null;
    private BertieArm lift = null;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = BertieBunyipConfiguration.newConfig(hardwareMap, telemetry);

        try {
            drive = new BertieBunyipDrive(this,
                    config.frontLeft, config.frontRight,
                    config.backLeft, config.backRight,
                    false);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise motors.");
        }

        HashMap<ButtonControl, String> buttonMap = new HashMap<>();

        buttonMap.put(ButtonControl.B, "Blue");
        buttonMap.put(ButtonControl.A, "Red");
        ButtonControl selectedButton = ButtonControl.A;

        boolean autoClearState = telemetry.isAutoClear();

        telemetry.setAutoClear(false);

        telemetry.addLine("PRESSING ONE OF THE FOLLOWING BUTTONS WILL INITIALISE THE ROBOT TO THE RELEVANT OPERATION MODE:");
        for (Map.Entry<ButtonControl, String> es : buttonMap.entrySet()) {
            System.out.printf("%s: %s%n", es.getKey().name(), es.getValue());
            telemetry.addLine(String.format("%s: %s", es.getKey().name(), es.getValue()));
            if (ButtonControl.isSelected(gamepad1, es.getKey())) {
                selectedButton = es.getKey();
            }
        }

        telemetry.addLine(String.format("IF NO BUTTON IS PRESSED WITHIN 3 SECONDS, *%s* WILL BE RUN", buttonMap.get(selectedButton)));
        telemetry.update();


        long startTime = System.nanoTime();

        outerLoop:
        while (System.nanoTime() < startTime + 3000000000L) {
            for (Map.Entry<ButtonControl, String> es : buttonMap.entrySet()) {
//                System.out.printf("%s: %s%n", es.getKey().name(), es.getValue());
//                telemetry.addLine(String.format("%s: %s", es.getKey().name(), es.getValue()));
                if (ButtonControl.isSelected(gamepad1, es.getKey())) {
                    selectedButton = es.getKey();
                    break outerLoop;
                }
            }

            idle();
        }

        telemetry.addLine(String.format("%s was selected: Running %s", selectedButton.name(), buttonMap.get(selectedButton)));
        telemetry.addLine("IF THIS SELECTION IS INCORRECT, QUIT THE OPMODE AND CHOOSE RESELECT");
        telemetry.update();

        telemetry.setAutoClear(autoClearState);


        telemetry.addLine(String.format("Loading tasks for %s", selectedButton.name()));
        switch(selectedButton) {
            case A:
                tasks.add(new MessageTask(this, 1, "Loaded red"));
                tasks.add(new BertieDriveTask(this, 0.5, drive, 1,0,0));
                tasks.add(new BertieDriveTask(this, 0.5, drive, 0,1,0));
                tasks.add(new BertieDriveTask(this, 1, drive, -1,0,0));
                tasks.add(new BertieDriveTask(this, 1, drive, 0,-1,0));
                break;
                case B:
                    tasks.add(new MessageTask(this, 1, "Loaded blue"));
                    break;
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        Task currentTask = tasks.peekFirst();
        if (currentTask == null) {
            return;
        }
        currentTask.run();
        if (currentTask.isFinished()) {
            tasks.removeFirst();
        }
        if (tasks.isEmpty()) {
            drive.setSpeedXYR(0,0,0);
            drive.update();
        }
    }
}
