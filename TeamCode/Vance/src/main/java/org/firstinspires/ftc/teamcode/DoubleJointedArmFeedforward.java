// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Amps;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Gs;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.KilogramSquareMeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Kilograms;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Meters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.MetersPerSecondPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.RadiansPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Volts;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Angle;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Current;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Distance;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Mass;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Measure;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Mult;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Velocity;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Voltage;

/**
 * Calculates feedforward voltages for a double-jointed arm.
 * <a href="https://github.com/Mechanical-Advantage/RobotCode2023/blob/49e7a6cb6864e68659f7380cb7cc3d485e78fa5a/src/main/java/org/littletonrobotics/frc2023/util/DoubleJointedArmFeedforward.java">Source</a>
 * <a href="https://www.chiefdelphi.com/t/whitepaper-two-jointed-arm-dynamics/423060">Origin</a>
 */
public class DoubleJointedArmFeedforward {
    private final Joint shoulder;
    private final Joint elbow;

    public DoubleJointedArmFeedforward(Joint shoulder, Joint elbow) {
        this.shoulder = shoulder;
        this.elbow = elbow;
    }

    public VectorF calculate(VectorF position) {
        return calculate(position, new VectorF(0, 0), new VectorF(0, 0));
    }

    public VectorF calculate(VectorF position, VectorF velocity, VectorF acceleration) {
        var M = new GeneralMatrixF(2, 2);
        var C = new GeneralMatrixF(2, 2);
        var Tg = new GeneralMatrixF(2, 1);

        M.put(
                0,
                0,
                (float) (shoulder.mass().in(Kilograms) * Math.pow(shoulder.centerOfGravityRadius().in(Meters), 2.0)
                        + elbow.mass().in(Kilograms) * (Math.pow(shoulder.length().in(Meters), 2.0) + Math.pow(elbow.centerOfGravityRadius().in(Meters), 2.0))
                        + shoulder.momentOfInertia().in(KilogramSquareMeters)
                        + elbow.momentOfInertia().in(KilogramSquareMeters)
                        + 2
                        * elbow.mass().in(Kilograms)
                        * shoulder.length().in(Meters)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Math.cos(position.get(1))));
        M.put(
                1,
                0,
                (float) (elbow.mass().in(Kilograms) * Math.pow(elbow.centerOfGravityRadius().in(Meters), 2.0)
                        + elbow.momentOfInertia().in(KilogramSquareMeters)
                        + elbow.mass().in(Kilograms)
                        * shoulder.length().in(Meters)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Math.cos(position.get(1))));
        M.put(
                0,
                1,
                (float) (elbow.mass().in(Kilograms) * Math.pow(elbow.centerOfGravityRadius().in(Meters), 2.0)
                        + elbow.momentOfInertia().in(KilogramSquareMeters)
                        + elbow.mass().in(Kilograms)
                        * shoulder.length().in(Meters)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Math.cos(position.get(1))));
        M.put(1, 1, (float) (elbow.mass().in(Kilograms) * Math.pow(elbow.centerOfGravityRadius().in(Meters), 2.0) + elbow.momentOfInertia().in(KilogramSquareMeters)));
        C.put(
                0,
                0,
                (float) (elbow.mass().negate().in(Kilograms)
                        * shoulder.length().in(Meters)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Math.sin(position.get(1))
                        * velocity.get(1)));
        C.put(
                1,
                0,
                (float) (elbow.mass().in(Kilograms)
                        * shoulder.length().in(Meters)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Math.sin(position.get(1))
                        * velocity.get(0)));
        C.put(
                0,
                1,
                (float) (elbow.mass().negate().in(Kilograms)
                        * shoulder.length().in(Meters)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Math.sin(position.get(1))
                        * (velocity.get(0) + velocity.get(1))));
        Tg.put(
                0,
                0,
                (float) ((shoulder.mass().in(Kilograms) * shoulder.centerOfGravityRadius().in(Meters) + elbow.mass().in(Kilograms) * shoulder.length().in(Meters))
                        * Gs.one().in(MetersPerSecondPerSecond)
                        * Math.cos(position.get(0))
                        + elbow.mass().in(Kilograms)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Gs.one().in(MetersPerSecondPerSecond)
                        * Math.cos(position.get(0) + position.get(1))));
        Tg.put(
                1,
                0,
                (float) (elbow.mass().in(Kilograms)
                        * elbow.centerOfGravityRadius().in(Meters)
                        * Gs.one().in(MetersPerSecondPerSecond)
                        * Math.cos(position.get(0) + position.get(1))));

        var torque = M.multiplied(acceleration).added(C.multiplied(velocity)).added(Tg);
        return new VectorF(
                getVoltageRequired(shoulder, torque.get(0, 0), velocity.get(0)) / (float) shoulder.nominalVoltage().in(Volts),
                getVoltageRequired(elbow, torque.get(1, 0), velocity.get(1)) / (float) shoulder.nominalVoltage().in(Volts)
        );
    }

    private float getVoltageRequired(Joint joint, float torqueNm, float speedRadPerSec) {
        return 1.0f / joint.getKvRadPerSecPerVolt() * speedRadPerSec + 1.0f / joint.getKtNMPerAmp() * joint.getROhms() * torqueNm;
    }

    /**
     * Construct a new Joint.
     *
     * @param mass the mass of this joint
     * @param length the length of this joint
     * @param momentOfInertia the moment of inertia for this joint (how hard it is to rotate)
     * @param centerOfGravityRadius cg radius
     * @param nominalVoltage voltage at which constants were measured (usually 12)
     * @param stallCurrent amperage at stalled
     * @param freeSpeed angular velocity at free/no load moving speed
     * @param freeCurrent amperage at free moving/no load speed
     * @param stallTorqueNewtonMeters torque when stalled
     */
    public record Joint(Measure<Mass> mass, Measure<Distance> length,
                        Measure<Mult<Mult<Mass, Distance>, Distance>> momentOfInertia,
                        Measure<Distance> centerOfGravityRadius, Measure<Voltage> nominalVoltage,
                        Measure<Current> stallCurrent, Measure<Velocity<Angle>> freeSpeed, Measure<Current> freeCurrent,
                        float stallTorqueNewtonMeters) {
        public float getROhms() {
            return (float) (nominalVoltage.in(Volts) / stallCurrent.in(Amps));
        }
        public float getKvRadPerSecPerVolt() {
            return (float) (freeSpeed.in(RadiansPerSecond) / (nominalVoltage.in(Volts) - getROhms() * freeCurrent.in(Amps)));
        }
        public float getKtNMPerAmp() {
            return (float) (stallTorqueNewtonMeters / stallCurrent.in(Amps));
        }
    }
}