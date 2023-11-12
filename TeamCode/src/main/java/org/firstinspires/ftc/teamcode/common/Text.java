package org.firstinspires.ftc.teamcode.common;

import java.util.Arrays;
import java.util.IllegalFormatFlagsException;
import java.util.List;
import java.util.Locale;

public class Text {
    /**
     * Format a string using only '%' placeholders.
     * Differs from String.format() as type can be omitted.
     */
    public static String formatString(String fstring, List<Object> objs) {
        // Replace all % with the strings in order
        int occurrences = 0;
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < fstring.length(); i++) {
            if (fstring.charAt(i) == '%') {
                // Remove character and insert new string
                try {
                    newString.append(objs.get(occurrences));
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalFormatFlagsException("Missing '%' placeholders!");
                }
                occurrences++;
                continue;
            }
            newString.append(fstring.charAt(i));
        }
        // Ignoring this corner case as it is not critical and can crash the robot
//        if (occurrences != objs.size()) {
//            throw new IllegalFormatFlagsException("Missing args for '%' placeholders!");
//        }
        return newString.toString();
    }

    public static String formatString(String fstring, Object... objs) {
        return formatString(fstring, Arrays.asList(objs));
    }

    /**
     * Round a number to a certain number of decimal points.
     */
    public static double round(double toRound, int toNums) {
        return Double.parseDouble(String.format(Locale.getDefault(), "%." + toNums + "f", toRound));
    }
}
