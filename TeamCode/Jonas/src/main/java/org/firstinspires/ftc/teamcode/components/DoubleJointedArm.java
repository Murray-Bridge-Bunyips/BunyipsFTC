package org.firstinspires.ftc.teamcode.components;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Jonas;

import java.util.function.DoubleSupplier;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsSubsystem;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.DualTelemetry;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;

public class DoubleJointedArm extends BunyipsSubsystem {

    public final DoubleJointedArm.Tasks tasks = new DoubleJointedArm.Tasks();

    public HoldableActuator shoulder;
    public HoldableActuator elbow;

    private Enabled enabled = Enabled.NEUTRAL;

    private enum Enabled {
        NEUTRAL,
        SHOULDER,
        ELBOW
    }

    public DoubleJointedArm(Jonas.Hardware jonas) {
        this.shoulder = new HoldableActuator(jonas.shoulder)
                .withName("shoulder");

        this.elbow = new HoldableActuator(jonas.elbow)
                .withName("elbow");

        delegate(shoulder, elbow);
    }

    public class Tasks {
        @NonNull
        public Task control(@NonNull DoubleSupplier shoulderPowerSupplier, @NonNull DoubleSupplier elbowPowerSupplier) {
            shoulder.setDefaultTask(shoulder.tasks.control(() -> enabled == Enabled.SHOULDER ? shoulderPowerSupplier.getAsDouble() : 0));
            elbow.setDefaultTask(elbow.tasks.control(() -> enabled == Enabled.ELBOW ? elbowPowerSupplier.getAsDouble() : 0));
            return Task.task().periodic(() -> {
                switch (enabled) {
                    case NEUTRAL:
                        if (shoulderPowerSupplier.getAsDouble() != 0) {
                            enabled = Enabled.SHOULDER;
                        }
                        else if (elbowPowerSupplier.getAsDouble() != 0) {
                            enabled = Enabled.ELBOW;
                        }
                        break;

                    case SHOULDER:
                        if (shoulderPowerSupplier.getAsDouble() == 0) {
                            enabled = Enabled.NEUTRAL;
                            break;
                        }
                        break;

                    case ELBOW:
                        if (elbowPowerSupplier.getAsDouble() == 0) {
                            enabled = Enabled.NEUTRAL;
                            break;
                        }
                        break;

                    default:
                        enabled = Enabled.NEUTRAL;
                        break;
                }
                        DualTelemetry.smartAdd(DoubleJointedArm.this.toString(), "enabled is %", enabled);
                        DualTelemetry.smartAdd(DoubleJointedArm.this.toString(), "shoulder is doing %", shoulder.getCurrentTask());
                        DualTelemetry.smartAdd(DoubleJointedArm.this.toString(), "elbow is doing %", elbow.getCurrentTask());
                        DualTelemetry.smartAdd(DoubleJointedArm.this.toString(), "shoulderPowerSupplier: %", shoulderPowerSupplier.getAsDouble());
                        DualTelemetry.smartAdd(DoubleJointedArm.this.toString(), "elbowPowerSupplier: %", elbowPowerSupplier.getAsDouble());
            })
                    .onFinish(() -> enabled = Enabled.NEUTRAL)
                    .named(forThisSubsystem("Control"))
                    .on(DoubleJointedArm.this, false);
        }
    }

    @Override
    protected void periodic() {

    }
}