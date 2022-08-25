package org.firstinspires.ftc.teamcode.common;

public class ButtonHashmap {
    private String option1;
    private String option2;
    private char button1;
    private char button2;
    private char defaultsel;

    public ButtonHashmap(BunyipsOpMode opMode, String option1, String option2, char button1, char button2, char defaultsel) {
        super(opMode);
        this.option1 = option1;
        this.option2 = option2;
        this.button1 = button1;
        this.button2 = button2;
        this.defaultsel = defaultsel;
    }
}