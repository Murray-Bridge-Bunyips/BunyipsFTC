package org.firstinspires.ftc.teamcode.tuning

import dev.frozenmilk.sinister.sdk.apphooks.SinisterOpModeRegistrar
import dev.frozenmilk.sinister.sdk.opmodes.OpModeScanner
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta

object Registrar : SinisterOpModeRegistrar {
    override fun registerOpModes(registrationHelper: OpModeScanner.RegistrationHelper) {
        // Register both OpModes on the bottom of the TeleOp list
        registrationHelper.register(
            OpModeMeta.Builder()
                .setName("Motor Direction Finder")
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .setGroup("dash")
                .build(),
            MotorDirection::class.java
        )
        registrationHelper.register(
            OpModeMeta.Builder()
                .setName("RoadRunner Tuning")
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .setGroup("dash")
                .build(),
            RoadRunner::class.java
        )
    }
}