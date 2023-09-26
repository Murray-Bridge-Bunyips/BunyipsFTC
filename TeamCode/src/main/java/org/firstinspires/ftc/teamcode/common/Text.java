package org.firstinspires.ftc.teamcode.common;

import java.util.Locale;

public class Text {
    /**
     * String.format() but without the need to call a locale to avoid a linter warning.
     * This is the default behaviour in Kotlin, but not in Java.
     */
    public static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }
}
