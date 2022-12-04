package org.firstinspires.ftc.teamcode.common;

/**
 * Custom odometer class to control and monitor field positioning data through all available means
 * of sensor input, including deadwheels, IMU, and camera readings.
 * This class aims to provide field-centric functionality, instead of absolute positioning.
 *
 * @author Lucas Bubner; FTC 15215; Dec 2022
 */
public class FieldPositioning extends BunyipsComponent {

    // Grand running variable that stores the robot position
    private int field_position;

    /**
     * A 2D array of the standard FTC field, from the perspective of blue-left and red-right
     */
    private static final int[][] Field = {
            {1, 2, 3, 4, 5, 6},
            {7, 8, 9, 10, 11, 12},
            {13, 14, 15, 16, 17, 18},
            {19, 20, 21, 22, 23, 24},
            {25, 26, 27, 28, 29, 30},
            {31, 32, 33, 34, 35, 36}
    };

    /**
     * Legal starting positions for POWERPLAY 2022
     */
    public enum StartingPositions {
        RED_LEFT,
        RED_RIGHT,
        BLUE_LEFT,
        BLUE_RIGHT
    }

    /**
     * Starting position relative to the robot where it is placed at a specific location
     */
    public enum StartingDirections {
        FORWARD,
        BACKWARD,
        LEFTWARD,
        RIGHTWARD
    }

    private final Deadwheel x, y;
    private final CameraOp cam;
    private final IMUOp imu;

    /**
     * Initial orientation of the robot on the field.
     *
     * @param position starting position of this opMode, in order to determine the starting matrix
     */
    public FieldPositioning(BunyipsOpMode opMode, Deadwheel x, Deadwheel y, CameraOp cam, IMUOp imu, StartingPositions position, StartingDirections direction) {
        super(opMode);
        this.x = x;
        this.y = y;
        this.cam = cam;
        this.imu = imu;
        switch (position) {
            case RED_LEFT:
                field_position = Field[0][1];
                break;
            case RED_RIGHT:
                field_position = Field[0][4];
                break;
            case BLUE_LEFT:
                field_position = Field[5][1];
                break;
            case BLUE_RIGHT:
                field_position = Field[5][4];
                break;
        }

        // This functionality has been implemented due to the robot initialising the heading at
        // zero upon initialisation, which will hamper with field-oriented angles if starting not
        // at the proper vector.
        switch (direction) {
            case FORWARD:
                // No offset, the IMU at 0 degrees is towards the opposite site and is what we want
                break;
            case BACKWARD:
                // Facing 180 degrees the wrong way, must flip 180 degrees for 0 to be towards the opponents
                imu.setOffset(180);
                break;
            case LEFTWARD:
                // Facing 90 degrees towards the left
                imu.setOffset(270);
                break;
            case RIGHTWARD:
                // Facing 90 degrees towards the right
                imu.setOffset(90);
                break;
        }
    }

    /**
     * Update matrices and data with newest information
     */
    public void update() {
    }

    /**
     * Get the current Field index of where the robot is located
     */
    public int getLocation() {
        return 0;
    }

    /**
     * Manually set field position if we know we are at a certain location (for example, if the opmode
     * has capabilities of knowing they are in the right corner of the field, it can set that here)
     */
    public void setLocation(int location) {
        this.field_position = location;
    }

}
