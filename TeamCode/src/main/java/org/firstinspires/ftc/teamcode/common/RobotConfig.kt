package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Abstract class to use as parent to the class you will define to mirror a "saved configuration" on the Robot Controller
 */
abstract class RobotConfig {
    protected var hardwareMap: HardwareMap? = null

    /**
     * Initialise the hardwareMap and assign the class instance variables to the class they represent.
     */
    protected abstract fun init()

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
            hardwareDevice = hardwareMap?.get(device, name) as HardwareDevice
        } catch (e: Throwable) {
            errors.add(name)
            e.localizedMessage?.let { DbgLog.msg(it) }
        }
        return hardwareDevice
    }

    /**
     * Check if hardware is available by checking if the instance is null.
     */
    fun assert(vararg devices: HardwareDevice?): Boolean {
        for (device in devices) {
            if (device == null) {
                return false
            }
        }
        return true
    }

    fun assert(devices: List<HardwareDevice?>) = assert(*devices.toTypedArray())

    companion object {
        /**
         * Static array of hardware errors stored via hardware name.
         */
        val errors = ArrayList<String>()

        /**
         * Factory method for creating a new instance of a configuration with a HardwareMap.
         */
        fun new(
            config: RobotConfig,
            hardwareMap: HardwareMap,
            addTelemetry: (msg: String, retained: Boolean) -> Unit
        ): RobotConfig {
            config.hardwareMap = hardwareMap
            errors.clear()
            config.init()
            addTelemetry(
                "${config.javaClass.simpleName.uppercase()}: Configuration completed with ${errors.size} error(s).",
                false
            )
            if (errors.isNotEmpty()) {
                for (error in errors) {
                    addTelemetry("! DEV_FAULT: $error", true)
                }
            }
            return config
        }
    }
}