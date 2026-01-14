package org.firstinspires.ftc.teamcode.common;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import java.lang.reflect.Field;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.annotations.Hook;

/**
 * Temporary shim to use the new field image on FTCDashboard.
 */
public class FtcDashboardDecodeField {
    @Hook(on = Hook.Target.PRE_INIT, priority = 7) // prior to all standard initialisation
    public static void injectDecodeField() {
        try {
            Field defaultField = TelemetryPacket.class.getDeclaredField("DEFAULT_FIELD");
            defaultField.setAccessible(true);
            Canvas DEFAULT_FIELD = (Canvas) defaultField.get(null); // static
            if (DEFAULT_FIELD != null) {
                DEFAULT_FIELD.clear(); // remove old operations and redo them using the new field we're serving

                DEFAULT_FIELD.setAlpha(0.4);
                // image in Common module under src/main/assets/images
                DEFAULT_FIELD.drawImage("/images/decode.webp", 0, 0, 144, 144);
                DEFAULT_FIELD.setAlpha(1.0);
                DEFAULT_FIELD.drawGrid(0, 0, 144, 144, 7, 7);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("error accessing dashboard packet", e);
        }
    }
}
