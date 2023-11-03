package org.firstinspires.ftc.teamcode.common

import java.io.PrintWriter
import java.io.StringWriter

/**
 * Util to prevent unhandled exceptions from crashing the app
 */
object ErrorUtil {
    private const val MAX_STACKTRACE_CHARS = 200

    @Throws(InterruptedException::class)
    fun handleCatchAllException(e: Throwable, log: (msg: String) -> Unit) {
        log("encountered exception! <${e.message}>")
        var stack = stackTraceAsString(e)
        if (stack.length > MAX_STACKTRACE_CHARS) {
            stack = stack.substring(0, MAX_STACKTRACE_CHARS - 4)
            stack += " ..."
        }
        log("stacktrace (max->$MAX_STACKTRACE_CHARS): $stack")
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