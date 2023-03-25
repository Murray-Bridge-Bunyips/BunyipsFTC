package org.firstinspires.ftc.teamcode.common

import org.firstinspires.ftc.robotcore.external.Telemetry
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Util to prevent unhandled exceptions from crashing the app
 */
object ErrorUtil {
    @Throws(InterruptedException::class)
    fun handleCatchAllException(e: Throwable, telemetry: Telemetry) {
        telemetry.log().add("Opmode Exception:" + e.message)
        val stckTrace = stackTraceAsString(e)
        telemetry.log().add("Opmode Stacktrace: $stckTrace")
        if (e is InterruptedException) {
            throw e
        }
    }

    private fun stackTraceAsString(e: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        return sw.toString()
    }
}