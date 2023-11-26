package org.firstinspires.ftc.teamcode.common

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.internal.system.Assert

/**
 * Abstract class to use as parent to the class you will define to mirror a "saved configuration" on the Robot Controller
 * ```
 *     private final YourConfig config = new YourConfig();
 *
 *     @Override
 *     protected void onInit() {
 *         config.init(this, hardwareMap);
 *     }
 * ```
 */
abstract class RobotConfig {
    protected var hardwareMap: HardwareMap? = null

    /**
     * Assign class instance variables to public HardwareDevices.
     */
    protected abstract fun configureHardware()

    /**
     * Use HardwareMap to fetch HardwareDevices and assign instances.
     * Should be called as the first line in onInit();
     */
    fun init(opMode: BunyipsOpMode, hardwareMap: HardwareMap) {
        errors.clear()
        this.hardwareMap = hardwareMap
        Assert.assertNotNull(hardwareMap)
        configureHardware()
        opMode.addTelemetry(
            "${this.javaClass.simpleName}: Configuration completed with ${errors.size} error(s).",
        )
        if (errors.isNotEmpty()) {
            for (error in errors) {
                opMode.addRetainedTelemetry("! DEV_FAULT: $error")
            }
        }
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
            hardwareDevice = hardwareMap?.get(device, name) as HardwareDevice
        } catch (e: Throwable) {
            errors.add(name)
            e.localizedMessage?.let { Dbg.warn(it) }
        }
        return hardwareDevice
    }

    // Global storage objects
    companion object {
        /**
         * Static array of hardware errors stored via hardware name.
         */
        @JvmStatic
        val errors = ArrayList<String>()

        /**
         * Static Pose2d to store the robot's last known position after an OpMode has ended.
         */
        @JvmStatic
        var lastKnownPosition: Pose2d? = null
    }
}