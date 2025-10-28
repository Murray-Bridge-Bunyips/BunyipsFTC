package org.firstinspires.ftc.teamcode.common;

import android.content.Context;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.util.WebHandlerManager;

import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar;

import java.lang.reflect.Field;

import fi.iki.elonen.NanoHTTPD;

/**
 * Temporary shim to use the new field image on FTCDashboard.
 */
public class FtcDashboardDecodeField {
    @WebHandlerRegistrar
    public static void injectDecodeField(Context context, WebHandlerManager manager) {
        // serving the new field image on /field-image
        manager.register("/field-image",
                (s) -> NanoHTTPD.newChunkedResponse(
                        NanoHTTPD.Response.Status.OK, "image/webp",
                        context.getResources().openRawResource(R.raw.decode)
                ));
        try {
            Field defaultField = TelemetryPacket.class.getDeclaredField("DEFAULT_FIELD");
            defaultField.setAccessible(true);
            Canvas DEFAULT_FIELD = (Canvas) defaultField.get(null); // static
            if (DEFAULT_FIELD != null) {
                DEFAULT_FIELD.clear(); // remove old operations and redo them using the new field we're serving

                DEFAULT_FIELD.setAlpha(0.4);
                DEFAULT_FIELD.drawImage("/field-image", 0, 0, 144, 144);
                DEFAULT_FIELD.setAlpha(1.0);
                DEFAULT_FIELD.drawGrid(0, 0, 144, 144, 7, 7);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("error accessing dashboard packet", e);
        }
    }
}
