package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;


/**
 * Abstract class to use as parent to the class you will define to mirror a "saved configuration" on the Robot Controller
 */
public abstract class RobotConfig {

    private static final ArrayList<String> errors = new ArrayList<>();
    private Telemetry telemetry;

    /**
     * Assign your class instance variables to the saved device names in the hardware map
     */
    abstract protected void init(HardwareMap hardwareMap, Telemetry telemetry);

    /**
     * Accessor for the telemetry utility
     * @return {@link Telemetry}
     */
    protected Telemetry getTelemetry() {
        return telemetry;
    }

    /**
     * Setter method for telemetry utility
     *
     * @param {@link TelemetryUtil}
     */
    protected void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    /**
     * Return an ArrayList of the hardware errors that happened during hardware initialisation
     *
     * @return ArrayList of Strings that correlate to hardware devices that failed to initialise
     */
    protected static ArrayList<String> getHardwareErrors() {
        return errors.size() != 0 ? errors : null;
    }

    /**
     * Convenience method for reading the device from the hardwareMap without having to check for exceptions
     *
     * @param name                  name of device saved in the configuration file
     * @param hardwareDeviceMapping the object of the item to configure
     */
    protected HardwareDevice getHardwareOn(String name, Object hardwareDeviceMapping) {
        HardwareDevice hardwareDevice = null;
        try {
            HardwareMap.DeviceMapping<HardwareDevice> deviceMapping = (HardwareMap.DeviceMapping<HardwareDevice>) hardwareDeviceMapping;
            hardwareDevice = deviceMapping.get(name);
        } catch (Throwable e) {
            try {
                getTelemetry().addLine("A fatal error occurred configuring the device: " + name);
                errors.add(name);
                ErrorUtil.handleCatchAllException(e, getTelemetry());
            } catch (InterruptedException e1) {
                DbgLog.msg(e.getLocalizedMessage());
            }
        }

        return hardwareDevice;
    }
}
