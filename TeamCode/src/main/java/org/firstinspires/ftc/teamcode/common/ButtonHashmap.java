package org.firstinspires.ftc.teamcode.common;

import java.util.HashMap;
import java.util.Map;

public class ButtonHashmap {
    public static ButtonControl map(BunyipsOpMode opMode, String option1, String option2, ButtonControl button1, ButtonControl button2, ButtonControl defaultbutton) {
        HashMap<ButtonControl, String> buttonMap = new HashMap<>();

        buttonMap.put(button1, option1);
        buttonMap.put(button2, option2);
        ButtonControl selectedButton = defaultbutton;

        boolean autoClearState = opMode.telemetry.isAutoClear();

        opMode.telemetry.setAutoClear(false);

        opMode.telemetry.addLine("PRESSING ONE OF THE FOLLOWING BUTTONS WILL INITIALISE THE ROBOT TO THE RELEVANT OPERATION MODE:");
        for (Map.Entry<ButtonControl, String> es : buttonMap.entrySet()) {
            System.out.printf("%s: %s%n", es.getKey().name(), es.getValue());
            opMode.telemetry.addLine(String.format("%s: %s", es.getKey().name(), es.getValue()));
            if (ButtonControl.isSelected(opMode.gamepad1, es.getKey())) {
                selectedButton = es.getKey();
            }
        }

        opMode.telemetry.addLine(String.format("IF NO BUTTON IS PRESSED WITHIN 3 SECONDS, *%s* WILL BE RUN", buttonMap.get(selectedButton)));
        opMode.telemetry.update();


        long startTime = System.nanoTime();

        outerLoop:
        while (System.nanoTime() < startTime + 3000000000L) {
            for (Map.Entry<ButtonControl, String> es : buttonMap.entrySet()) {
                if (ButtonControl.isSelected(opMode.gamepad1, es.getKey())) {
                    selectedButton = es.getKey();
                    break outerLoop;
                }
            }

            opMode.idle();
        }

        opMode.telemetry.addLine(String.format("%s was selected: Running %s", selectedButton.name(), buttonMap.get(selectedButton)));
        opMode.telemetry.addLine("IF THIS SELECTION IS INCORRECT, QUIT THE OPMODE AND CHOOSE RESELECT");
        opMode.telemetry.update();

        opMode.telemetry.setAutoClear(autoClearState);


        opMode.telemetry.addLine(String.format("Loading tasks for %s", selectedButton.name()));
        return selectedButton;
    }
}