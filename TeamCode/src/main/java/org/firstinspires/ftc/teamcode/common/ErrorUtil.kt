package org.firstinspires.ftc.teamcode.common

import org.firstinspires.ftc.robotcore.external.Telemetry
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Util to prevent unhandled exceptions from crashing the app
 */
object ErrorUtil {
    @Throws(InterruptedException::class)
    fun handleCatchAllException(e: Throwable, log: (msg: String) -> Unit) {
        log("encountered exception! <${e.message}>")
        log("stacktrace: ${stackTraceAsString(e)}")
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