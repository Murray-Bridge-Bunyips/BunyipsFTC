package org.murraybridgebunyips.bunyipslib.tasks

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode

/**
 * Task interface with BunyipsOpMode dependency injection.
 */
abstract class BunyipsTask(open val opMode: BunyipsOpMode, override var time: Double) : Task(time) {
    constructor(opMode: BunyipsOpMode) : this(opMode, 0.0)
}