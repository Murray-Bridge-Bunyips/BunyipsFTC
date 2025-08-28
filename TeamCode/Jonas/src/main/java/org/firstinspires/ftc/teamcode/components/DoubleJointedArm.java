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
            return Task.task().periodic(() -> {
                switch (enabled) {
                    case NEUTRAL:
                        shoulder.cancelCurrentTask();
                        elbow.cancelCurrentTask();
                        if (shoulderPowerSupplier.getAsDouble() != 0) {
                            enabled = Enabled.SHOULDER;
                        }
                        else if (elbowPowerSupplier.getAsDouble() != 0) {
                            enabled = Enabled.ELBOW;
                        }

                    case SHOULDER:
                        if (shoulderPowerSupplier.getAsDouble() == 0) {
                            enabled = Enabled.NEUTRAL;
                            break;
                        }
                        shoulder.setCurrentTask(shoulder.tasks.control(shoulderPowerSupplier));
                        break;

                    case ELBOW:
                        if (elbowPowerSupplier.getAsDouble() == 0) {
                            enabled = Enabled.NEUTRAL;
                            break;
                        }
                        elbow.setCurrentTask(elbow.tasks.control(elbowPowerSupplier));
                        break;

                    default:
                        enabled = Enabled.NEUTRAL;
                        break;
                }
                        DualTelemetry.smartAdd(DoubleJointedArm.this.toString(), "enabled is %", enabled);
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