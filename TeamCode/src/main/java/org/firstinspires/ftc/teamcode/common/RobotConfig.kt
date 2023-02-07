package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * Abstract class to use as parent to the class you will define to mirror a "saved configuration" on the Robot Controller
 */
abstract class RobotConfig {
    // HardwareMap functions during initialisation
    protected var hardwareMap: HardwareMap? = null

    /**
     * Assign your class instance variables to the saved device names in the hardware map
     */
    protected abstract fun init(hardwareMap: HardwareMap?, telemetry: Telemetry)
    protected fun setTelemetry(telemetry: Telemetry?) {
        Companion.telemetry = telemetry
    }

    /**
     * Convenience method for reading the device from the hardwareMap without having to check for exceptions.
     * Uses class initialistion instead of hardwareMap initialistion to widen the range of devices, supporting
     * custom classes for dead wheels and etc.
     *
     *
     * Every hardware error with this method be saved to a static array, which can be accessed during the
     * lifetime of the opMode.
     *
     * @param name   name of device saved in the configuration file
     * @param device the class of the item to configure, in final abstraction extending HardwareDevice
     */
    protected fun getHardware(name: String, deviceMapping: Any?): HardwareDevice? {
        var hardwareDevice: HardwareDevice? = null
        try {
            val deviceMapping = deviceMapping as DeviceMapping<HardwareDevice>
            hardwareDevice = deviceMapping[name]
        } catch (e: Throwable) {
            if (!errors.contains(name))
                errors.add(name)
            // ErrorUtil.handleCatchAllException(e, getTelemetry());
            // getTelemetry().addLine("A fatal error occurred configuring the device: " + name);

            /*
                Report error message and stacktrace to Logcat, but not the driver station.
                OpModes should manually check getHardwareErrors() for the list of hardware errors
                that occurred during their opMode setup, to prevent bloating up the telemetry of init-phase.
                This can be done by calling printHardwareErrors() after robot configuration, or interpreting
                this data in another way than simply telling the operator.
             */
            e.localizedMessage?.let { DbgLog.msg(it) }
        }
        return hardwareDevice
    }

    companion object {
        private val errors = ArrayList<String>()

        // Telemetry functions during initialisation
        protected var telemetry: Telemetry? = null
            private set

        /**
         * Return an ArrayList of the hardware errors that happened during hardware initialisation.
         * Can be called any time during the opMode either to check the number of disconnected equipment,
         * or for utilisation in other forms such as data reporting and debugging.
         *
         * @return ArrayList of Strings that correlate to hardware devices that failed to initialise.
         * Returns null if there are no reported hardware errors.
         */
        @JvmStatic
        protected val hardwareErrors: ArrayList<String>?
            get() = if (errors.size != 0) errors else null

        /**
         * Telemetry out all errors from hardware initialisation. This is useful to call after initialisation to ensure
         * that everything on board has successfully initialised, and to see what is not at the Driver Station.
         */
        @JvmStatic
        protected fun printHardwareErrors() {
            if (errors.size == 0) {
                telemetry!!.addData(
                    "BunyipsOpMode Status",
                    "ROBOT HARDWARE CONFIGURATION COMPLETE. ALL SYSTEMS GO."
                )
                return
            }
            telemetry!!.addData(
                "BunyipsOpMode Status",
                "ERROR(S) DURING CONFIGURATION. THESE DEVICES WERE NOT ABLE TO BE INITIALISED."
            )
            val error: Iterator<String> = errors.iterator()
            for (i in errors.indices) {
                telemetry!!.addData("DEVICE $i", error.next())
            }
        }
    }
}