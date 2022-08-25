package org.firstinspires.ftc.teamcode.common;

public class ButtonHashmap {
    private String option1;
    private String option2;
    private char button1;
    private char button2;
    private char defaultsel;

    public ButtonHashmap(BunyipsOpMode opMode, String option1, String option2, char button1, char button2, char defaultbutton) {
        super(opMode);
        this.option1 = option1;
        this.option2 = option2;
        this.button1 = button1;
        this.button2 = button2;
        this.defaultbutton = defaultbutton;
    }

    public void map() {
        HashMap<ButtonControl, String> buttonMap = new HashMap<>();

        buttonMap.put(ButtonControl.button1, option1);
        buttonMap.put(ButtonControl.button2, option2);
        ButtonControl selectedButton = ButtonControl.defaultbutton;

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
        return selectedButton;
    }
}