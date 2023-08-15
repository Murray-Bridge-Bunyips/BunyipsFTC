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

    val angle: Double
        get() = vector.angle

    companion object {
        /**
         * Convert a robot vector to a relative vector.
         */
        fun convert(vector: RobotVector): RelativeVector {
            return when {
                vector.x > 0.5 && vector.y > 0.5 -> FORWARD_RIGHT
                vector.x > 0.5 && vector.y < -0.5 -> BACKWARD_RIGHT
                vector.x < -0.5 && vector.y > 0.5 -> FORWARD_LEFT
                vector.x < -0.5 && vector.y < -0.5 -> BACKWARD_LEFT
                vector.x > 0.5 -> RIGHT
                vector.x < -0.5 -> LEFT
                vector.y > 0.5 -> FORWARD
                vector.y < -0.5 -> BACKWARD
                vector.r > 0 -> CLOCKWISE
                vector.r < 0 -> ANTICLOCKWISE
                else -> throw IllegalArgumentException("$vector cannot be converted to a RelativeVector.")
            }
        }
    }
}