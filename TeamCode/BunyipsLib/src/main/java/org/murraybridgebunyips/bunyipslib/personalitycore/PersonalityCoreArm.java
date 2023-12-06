package org.murraybridgebunyips.bunyipslib.personalitycore;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.murraybridgebunyips.bunyipslib.BunyipsComponent;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.DualClaws;
import org.murraybridgebunyips.bunyipslib.NullSafety;
import org.murraybridgebunyips.bunyipslib.personalitycore.submodules.PersonalityCoreClawMover;
import org.murraybridgebunyips.bunyipslib.personalitycore.submodules.PersonalityCoreClawRotator;
import org.murraybridgebunyips.bunyipslib.personalitycore.submodules.PersonalityCoreHook;
import org.murraybridgebunyips.bunyipslib.personalitycore.submodules.PersonalityCoreManagementRail;

/**
 * Overhead class that handles a single instantiation of other PersonalityCore components.
 * Allows a one-level chain pattern to be used to call methods on the submodules.
 * @author Lucas Bubner, 2023
 * @noinspection UnusedReturnValue
 */
@Config
public class PersonalityCoreArm extends BunyipsComponent {
    private PersonalityCoreClawMover clawMover;
    private PersonalityCoreClawRotator clawRotator;
    private PersonalityCoreHook hook;
    private PersonalityCoreManagementRail managementRail;
    private DualClaws claws;

    public static double LEFT_CLAW_OPEN = 0.0;
    public static double RIGHT_CLAW_OPEN = 0.0;
    public static double LEFT_CLAW_CLOSED = 1.0;
    public static double RIGHT_CLAW_CLOSED = 1.0;

    public PersonalityCoreArm(@NonNull BunyipsOpMode opMode, CRServo pixelMotion, Servo pixelAlignment, Servo suspenderHook, DcMotorEx suspenderActuator, Servo leftPixel, Servo rightPixel) {
        super(opMode);
        if (NullSafety.assertComponentArgs(getOpMode(), PersonalityCoreClawMover.class, pixelMotion))
            clawMover = new PersonalityCoreClawMover(getOpMode(), pixelMotion);
        if (NullSafety.assertComponentArgs(getOpMode(), PersonalityCoreClawRotator.class, pixelAlignment))
            clawRotator = new PersonalityCoreClawRotator(getOpMode(), pixelAlignment);
        if (NullSafety.assertComponentArgs(getOpMode(), PersonalityCoreHook.class, suspenderHook))
            hook = new PersonalityCoreHook(getOpMode(), suspenderHook);
        if (NullSafety.assertComponentArgs(getOpMode(), PersonalityCoreManagementRail.class, suspenderActuator))
            managementRail = new PersonalityCoreManagementRail(getOpMode(), suspenderActuator);
        if (NullSafety.assertComponentArgs(getOpMode(), DualClaws.class, leftPixel, rightPixel))
            claws = new DualClaws(getOpMode(), leftPixel, rightPixel, LEFT_CLAW_CLOSED, LEFT_CLAW_OPEN, RIGHT_CLAW_CLOSED, RIGHT_CLAW_OPEN);
    }

    public PersonalityCoreClawMover getClawMover() {
        return clawMover;
    }

    public PersonalityCoreClawMover actuateClawMoverUsingController(double y) {
        clawMover.actuateUsingController(y);
        return clawMover;
    }

    public PersonalityCoreClawMover setClawMoverPower(double power) {
        clawMover.setPower(power);
        return clawMover;
    }

    public PersonalityCoreClawMover runClawMoverFor(double seconds, double power) {
        clawMover.runFor(seconds, power);
        return clawMover;
    }

    public PersonalityCoreClawRotator getClawRotator() {
        return clawRotator;
    }

    public PersonalityCoreClawRotator faceClawToBoard() {
        clawRotator.faceBoard();
        return clawRotator;
    }

    public PersonalityCoreClawRotator faceClawToGround() {
        clawRotator.faceGround();
        return clawRotator;
    }

    public PersonalityCoreClawRotator actuateClawRotatorUsingController(double y) {
        clawRotator.actuateUsingController(y);
        return clawRotator;
    }

    public PersonalityCoreClawRotator setClawRotatorPosition(double target) {
        clawRotator.setPosition(target);
        return clawRotator;
    }

    public PersonalityCoreClawRotator setClawRotatorDegrees(double degrees) {
        clawRotator.setDegrees(degrees);
        return clawRotator;
    }

    public PersonalityCoreHook getHook() {
        return hook;
    }

    public PersonalityCoreHook actuateHookUsingController(double y) {
        hook.actuateUsingController(y);
        return hook;
    }

    public PersonalityCoreHook setHookPosition(double y) {
        hook.setPosition(y);
        return hook;
    }

    public PersonalityCoreHook extendHook() {
        hook.extend();
        return hook;
    }

    public PersonalityCoreHook retractHook() {
        hook.retract();
        return hook;
    }

    public PersonalityCoreHook uprightHook() {
        hook.upright();
        return hook;
    }

    public PersonalityCoreManagementRail getManagementRail() {
        return managementRail;
    }

    public PersonalityCoreManagementRail actuateManagementRailUsingController(double y) {
        managementRail.actuateUsingController(y);
        return managementRail;
    }

    public PersonalityCoreManagementRail setManagementRailPower(double p) {
        managementRail.setPower(p);
        return managementRail;
    }

    public DualClaws getClaws() {
        return claws;
    }

    public DualClaws toggleClaw(DualClaws.ServoSide side) {
        claws.toggleServo(side);
        return claws;
    }

    public DualClaws closeClaw(DualClaws.ServoSide side) {
        claws.closeServo(side);
        return claws;
    }

    public DualClaws openClaw(DualClaws.ServoSide side) {
        claws.openServo(side);
        return claws;
    }

    public void update() {
        if (clawMover != null) clawMover.update();
        if (clawRotator != null) clawRotator.update();
        if (hook != null) hook.update();
        if (managementRail != null) managementRail.update();
        if (claws != null) claws.update();
    }
}
