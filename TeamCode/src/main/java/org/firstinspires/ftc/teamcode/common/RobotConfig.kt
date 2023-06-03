package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Abstract class to use as parent to the class you will define to mirror a "saved configuration" on the Robot Controller
 */
abstract class RobotConfig {
    protected abstract var hardwareMap: HardwareMap

    /**
     * HashMap to provide a mapping between the device and its name in the hardware map
     * This is where your hardware devices will be saved and mapped to instance variables in your
     * extended RobotConfig class.
     */
    protected abstract val deviceMappings: HashMap<HardwareDevice?, String>

    /**
     * Initialise the hardwareMap and assign the class instance variables to the class they represent.
     */
    protected abstract fun init()

    /**
     * An ArrayList of the hardware errors that happened during hardware initialisation.
     * Can be checked any time during the opMode either to check the number of disconnected equipment,
     * or for utilisation in other forms such as data reporting and debugging.
     *
     * In an initialisation, it is often useful to include this array with configuration, where the
     * OpMode will be able to handle the errors that occurred during initialisation.
     *
     * @return Array of strings that correlate to hardware device names that failed to initialise.
     */
    val hardwareErrors: ArrayList<String> = ArrayList()

    /**
     * Check all entered HardwareDevices if they have thrown errors during initialisation.
     * This method is essential to call for OpModes where ensuring hardware integrity is important,
     * but can be omitted in OpModes where hardware errors are not critical.
     *
     * E.g.
     *      if (!hasHardwareErrors(config.bl, config.br, config.fl, config.fr)) {
     *          // Safe to non-null assert these motors for a drive system
     *      }
     *
     * @param devices the hardware devices to check for errors
     */
    fun hasHardwareErrors(vararg devices: HardwareDevice?): Boolean {
        for (device in devices) {
            val name = deviceMappings[device]
            if (hardwareErrors.contains(name) || device == null) {
                return true
            }
        }
        return false
    }

    fun hasHardwareErrors(devices: List<HardwareDevice?>): Boolean {
        return hasHardwareErrors(*devices.toTypedArray())
    }

    /**
     * Convenience method for reading the device from the hardwareMap without having to check for exceptions.
     * Uses class initialistion instead of hardwareMap initialistion to widen the range of devices, supporting
     * custom classes for dead wheels, etc.
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
            if (!hardwareErrors.contains(name)) {
                hardwareErrors.add(name)
            }
            e.localizedMessage?.let { DbgLog.msg(it) }
        }
        return hardwareDevice
    }
}