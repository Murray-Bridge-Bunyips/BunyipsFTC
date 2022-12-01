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
    private HardwareMap hardwareMap;

    /**
     * Assign your class instance variables to the saved device names in the hardware map
     */
    abstract protected void init(HardwareMap hardwareMap, Telemetry telemetry);

    // Telemetry functions during initialisation
    protected Telemetry getTelemetry() {
        return telemetry;
    }

    protected void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    // HardwareMap functions during initialisation
    protected HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    protected void setHardwareMap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
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
     * Convenience method for reading the device from the hardwareMap without having to check for exceptions.
     * Uses class initialistion instead of hardwareMap initialistion to widen the range of devices, supporting
     * custom classes for dead wheels and etc.
     *
     * @param name   name of device saved in the configuration file
     * @param device the class of the item to configure, in final abstraction extending HardwareDevice
     */
    protected HardwareDevice getHardware(String name, Class<?> device) {
        HardwareDevice hardwareDevice = null;
        try {
            hardwareDevice = (HardwareDevice) hardwareMap.get(device, name);
        } catch (Throwable e) {
            getTelemetry().addLine("A fatal error occurred configuring the device: " + name);
            errors.add(name);
            // ErrorUtil.handleCatchAllException(e, getTelemetry());

            /*
                Report error message and stacktrace to Logcat, but not the driver station.
                OpModes should manually check getHardwareErrors() for the list of hardware errors
                that occurred during their opMode setup, to prevent bloating up the telemetry of init-phase.
                However, telemetry will still add the required lines stating a device was not configured.
             */
            DbgLog.msg(e.getLocalizedMessage());
        }

        return hardwareDevice;
    }
}
