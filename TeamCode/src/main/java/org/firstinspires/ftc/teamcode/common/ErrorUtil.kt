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
        if (e.cause != null) {
            log("caused by: ${e.cause}")
        }
        var stack = stackTraceAsString(e)
        if (stack.length > MAX_STACKTRACE_CHARS) {
            stack = stack.substring(0, MAX_STACKTRACE_CHARS - 4)
            stack += " ..."
        }
        log("stacktrace (max@$MAX_STACKTRACE_CHARS): $stack")
        DbgLog.logStacktrace(e)
        if (e is InterruptedException) {
            // FTC SDK must handle this
            throw e
        }
    }

    fun stackTraceAsString(e: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        return sw.toString()
    }
}