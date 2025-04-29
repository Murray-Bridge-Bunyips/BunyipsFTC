package org.firstinspires.ftc.teamcode.arm;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.CompositeController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.SystemController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;

public class MutexLockedControllers {
    private final SystemController first;
    private final SystemController second;

    private volatile int mutexOwnedBy = 0;

    public MutexLockedControllers(SystemController first, SystemController second) {
        this.first = first;
        this.second = second;
    }

    public void update() {  // auto-called by the controllers on eval
        assert first.pidf().isPresent() && second.pidf().isPresent();
        PIDFController pid1 = first.pidf().get();
        PIDFController pid2 = second.pidf().get();
        if (!pid1.atSetpoint() && mutexOwnedBy == 0) mutexOwnedBy = 1;
        if (!pid2.atSetpoint() && mutexOwnedBy == 0) mutexOwnedBy = 2;
        if ((pid1.atSetpoint() && mutexOwnedBy == 1) || (pid2.atSetpoint() && mutexOwnedBy == 2)) mutexOwnedBy = 0;
    }

    public class First extends CompositeController {
        public First() {
            super(MutexLockedControllers.this.first, NULL, (out, ignored) -> {
                MutexLockedControllers.this.update();
                return mutexOwnedBy != 2 ? out : 0; // TODO: these really should be stopping target position from changing
                                                    //  not turning off power output completely
            });
        }
    }

    public class Second extends CompositeController {
        public Second() {
            super(MutexLockedControllers.this.second, NULL, (out, ignored) ->  {
                MutexLockedControllers.this.update();
                return mutexOwnedBy != 1 ? out : 0;
            });
        }
    }
}
