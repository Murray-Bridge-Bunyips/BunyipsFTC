package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Abstract class to use as parent to the class you will define to mirror a "saved configuration" on the Robot Controller
 */
public abstract class RobotConfig {

    private static final ArrayList<String> errors = new ArrayList<>();
    private static Telemetry telemetry;
    private HardwareMap hardwareMap;

    /**
     * Assign your class instance variables to the saved device names in the hardware map
     */
    abstract protected void init(HardwareMap hardwareMap, Telemetry telemetry);

    // Telemetry functions during initialisation
    protected static Telemetry getTelemetry() {
        return telemetry;
    }

    protected void setTelemetry(Telemetry telemetry) {
        RobotConfig.telemetry = telemetry;
    }

    // HardwareMap functions during initialisation
    protected HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    protected void setHardwareMap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }


    /**
     * Return an ArrayList of the hardware errors that happened during hardware initialisation.
     * Can be called any time during the opMode either to check the number of disconnected equipment,
     * or for utilisation in other forms such as data reporting and debugging.
     *
     * @return ArrayList of Strings that correlate to hardware devices that failed to initialise.
     * Returns null if there are no reported hardware errors.
     */
    protected static ArrayList<String> getHardwareErrors() {
        return errors.size() != 0 ? errors : null;
    }

    /**
     * Telemetry out all errors from hardware initialisation. This is useful to call after initialisation to ensure
     * that everything on board has successfully initialised, and to see what is not at the Driver Station.
     */
    protected static void printHardwareErrors() {
        if (errors.size() == 0) {
            getTelemetry().addData("BunyipsOpMode Status", "ROBOT HARDWARE CONFIGURATION COMPLETE. ALL SYSTEMS GO.");
            return;
        }

        getTelemetry().addData("BunyipsOpMode Status", "ERROR(S) DURING CONFIGURATION. THESE DEVICES WERE NOT ABLE TO BE INITIALISED.");
        Iterator<String> error = errors.iterator();
        for (int i = 0; i < errors.size(); i++) {
            getTelemetry().addData("DEVICE " + i, error.next());
        }
    }

    /**
     * Convenience method for reading the device from the hardwareMap without having to check for exceptions.
     * Uses class initialistion instead of hardwareMap initialistion to widen the range of devices, supporting
     * custom classes for dead wheels and etc.
     * <p>
     * Every hardware error with this method be saved to a static array, which can be accessed during the
     * lifetime of the opMode.
     *
     * @param name   name of device saved in the configuration file
     * @param device the class of the item to configure, in final abstraction extending HardwareDevice
     * @see this.printHardwareErrors()
     */
    protected HardwareDevice getHardware(String name, Class<?> device) {
        HardwareDevice hardwareDevice = null;

        try {
            hardwareDevice = (HardwareDevice) hardwareMap.get(device, name);
        } catch (Throwable e) {
            errors.add(name);
            // ErrorUtil.handleCatchAllException(e, getTelemetry());
            // getTelemetry().addLine("A fatal error occurred configuring the device: " + name);

            /*
                Report error message and stacktrace to Logcat, but not the driver station.
                OpModes should manually check getHardwareErrors() for the list of hardware errors
                that occurred during their opMode setup, to prevent bloating up the telemetry of init-phase.
                This can be done by calling printHardwareErrors() after robot configuration, or interpreting
                this data in another way than simply telling the operator.
             */
            DbgLog.msg(e.getLocalizedMessage());
        }

        return hardwareDevice;
    }
}
