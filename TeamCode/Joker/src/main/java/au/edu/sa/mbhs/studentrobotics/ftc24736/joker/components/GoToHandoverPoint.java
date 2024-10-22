package au.edu.sa.mbhs.studentrobotics.ftc24736.joker.components;

import com.qualcomm.robotcore.hardware.TouchSensor;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.WaitUntilTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.RaceTaskGroup;

public class GoToHandoverPoint extends RaceTaskGroup {
    /**
     * Does a home task until a touch sensor is pressed.
     */
    public GoToHandoverPoint(HoldableActuator actuator, TouchSensor sensor) {
        super(actuator.tasks.home(), new WaitUntilTask(sensor::isPressed));
    }
}
