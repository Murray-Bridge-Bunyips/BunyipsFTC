package org.firstinspires.ftc.teamcode.finder

import dev.frozenmilk.sinister.sdk.apphooks.SinisterOpModeRegistrar
import dev.frozenmilk.sinister.sdk.opmodes.OpModeScanner
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta

object Registrar : SinisterOpModeRegistrar {
    override fun registerOpModes(registrationHelper: OpModeScanner.RegistrationHelper) {
        // Register on the bottom of the TeleOp list
        registrationHelper.register(
            OpModeMeta.Builder()
                .setName("Scout Finder")
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .setGroup("dash")
                .build(),
            ScoutFinder::class.java
        )
    }
}