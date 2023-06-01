package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Abstract class to use as parent to the class you will define to mirror a "saved configuration" on the Robot Controller
 */
abstract class RobotConfig {
    // HardwareMap functions during initialisation
    protected abstract var hardwareMap: HardwareMap

    /**
     * Assign your class instance variables to the saved device names in the hardware map
     */
    protected abstract fun init()

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
    protected fun getHardware(name: String, device: Class<*>?): HardwareDevice? {
        var hardwareDevice: HardwareDevice? = null
        try {
            hardwareDevice = hardwareMap.get(device, name) as HardwareDevice
        } catch (e: Throwable) {
            if (!hardwareErrors.contains(name))
                hardwareErrors.add(name)
            e.localizedMessage?.let { DbgLog.msg(it) }
        }
        return hardwareDevice
    }

    companion object {
        /**
         * Return an ArrayList of the hardware errors that happened during hardware initialisation.
         * Can be called any time during the opMode either to check the number of disconnected equipment,
         * or for utilisation in other forms such as data reporting and debugging.
         *
         * @return ArrayList of Strings that correlate to hardware devices that failed to initialise.
         * Returns null if there are no reported hardware errors.
         */
        protected val hardwareErrors: ArrayList<String> = ArrayList()
    }
}