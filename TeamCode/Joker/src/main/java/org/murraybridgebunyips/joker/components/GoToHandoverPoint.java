package org.murraybridgebunyips.joker.components;

import com.qualcomm.robotcore.hardware.TouchSensor;

import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.WaitUntilTask;
import org.murraybridgebunyips.bunyipslib.tasks.groups.RaceTaskGroup;

public class GoToHandoverPoint extends RaceTaskGroup {
    /**
     * Does a home task until a touch sensor is pressed
     */
    public GoToHandoverPoint(HoldableActuator actuator, TouchSensor sensor) {
        super(actuator.tasks.home(), new WaitUntilTask(sensor::isPressed));
    }
}
