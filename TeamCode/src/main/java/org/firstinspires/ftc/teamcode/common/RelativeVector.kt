package org.firstinspires.ftc.teamcode.common

/**
 * Represents relative XYR vectors in 2D space.
 */
enum class RelativeVector(val vector: RobotVector) {
    // 2D translational vectors
    FORWARD         (RobotVector(0.0, 1.0, 0.0)),
    BACKWARD        (RobotVector(0.0, -1.0, 0.0)),
    LEFT            (RobotVector(-1.0, 0.0, 0.0)),
    RIGHT           (RobotVector(1.0, 0.0, 0.0)),

    // Fancy 2D translational vectors
    FORWARD_LEFT    (RobotVector(-1.0, 1.0, 0.0)),
    FORWARD_RIGHT   (RobotVector(1.0, 1.0, 0.0)),
    BACKWARD_LEFT   (RobotVector(-1.0, -1.0, 0.0)),
    BACKWARD_RIGHT  (RobotVector(1.0, -1.0, 0.0)),

    // 2D rotational vectors
    CLOCKWISE       (RobotVector(0.0, 0.0, 1.0)),
    ANTICLOCKWISE   (RobotVector(0.0, 0.0, -1.0));
}