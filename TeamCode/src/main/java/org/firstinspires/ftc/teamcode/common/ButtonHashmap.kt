package org.firstinspires.ftc.teamcode.common;

import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class to ask for user input from a controller in order to determine a pre-determined
 * set of instructions before Autonomous. Supports up to 4 options and empty strings in the static
 * constructor will be ignored and not shown.
 */
public class ButtonHashmap {
    public static ButtonControl map(BunyipsOpMode opMode, String A, String B, String Y, String X) {
        HashMap<ButtonControl, String> buttonMap = new HashMap<>();

        // Array of buttons which correlate each button to each task set
        ButtonControl button1 = ButtonControl.A;
        ButtonControl button2 = ButtonControl.B;
        ButtonControl button3 = ButtonControl.Y;
        ButtonControl button4 = ButtonControl.X;

        // Default button
        ButtonControl selectedButton = ButtonControl.A;

        if (!A.isEmpty()) {
            buttonMap.put(button1, A);
        }
        if (!B.isEmpty()) {
            buttonMap.put(button2, B);
        }
        if (!Y.isEmpty()) {
            buttonMap.put(button3, Y);
        }
        if (!X.isEmpty()) {
            buttonMap.put(button4, X);
        }

        boolean autoClearState = opMode.telemetry.isAutoClear();
        opMode.telemetry.setAutoClear(false);

        opMode.telemetry.addLine("PRESSING ONE OF THE FOLLOWING BUTTONS WILL INITIALISE THE ROBOT TO THE RELEVANT OPERATION MODE:");
        for (Map.Entry<ButtonControl, String> es : buttonMap.entrySet()) {
            // System.out.printf("%s: %s%n", es.getKey().name(), es.getValue());
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
        opMode.telemetry.addLine("IF THIS SELECTION IS INCORRECT, QUIT THE OPMODE AND RESELECT NOW");
        opMode.telemetry.update();
        opMode.telemetry.setAutoClear(autoClearState);

        opMode.telemetry.addLine(String.format("ButtonHashmap has returned button '%s' to the OpMode.", selectedButton.name()));
        return selectedButton;
    }
}