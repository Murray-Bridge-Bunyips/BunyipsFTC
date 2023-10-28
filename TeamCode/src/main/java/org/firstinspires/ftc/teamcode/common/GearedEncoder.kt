package org.firstinspires.ftc.teamcode.common

interface GearedEncoder : Encoder {
    override val wheelDiameterMM: Double?
        get() = throw IllegalAccessException("Cannot access wheelDiameterMM on a GearedEncoder")

    val gearRatio: Double

    override fun position(scope: Encoder.Scope): Double {
        // TODO
        return 0.0
    }

    override fun travelledMM(scope: Encoder.Scope): Double {
        // TODO
        return 0.0
    }
}