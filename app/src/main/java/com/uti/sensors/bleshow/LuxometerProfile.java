package com.uti.sensors.bleshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.polidea.rxandroidble.RxBleConnection;
import com.uti.Utils.GenericTabRow;

import java.util.UUID;

import rx.Observable;

import static java.lang.StrictMath.pow;

/**
 * Created by kunyi on 2017/5/14.
 */

public class LuxometerProfile extends GenericProfile {
    private static final String TAG = "LuxometerProfile";
    private final static String GattServ = "F000AA70-0451-4000-B000-0000000000000";
    private final static String GattData = "F000AA71-0451-4000-B000-0000000000000";
    private final static String GattConf = "F000AA72-0451-4000-B000-0000000000000";
    private final static String GattPeri = "F000AA73-0451-4000-B000-0000000000000";
    private final static byte[] Bconf = new byte[]{(byte) 0x01};
    private final boolean DBG = false;
    protected GenericTabRow tr;
    protected Context context;
    private float lux;

    public LuxometerProfile(@NonNull Observable<RxBleConnection> conn) {
        super(conn,
                UUID.fromString(GattServ),
                UUID.fromString(GattConf),
                UUID.fromString(GattData),
                UUID.fromString(GattPeri),
                Bconf);
    }

    @Override
    public boolean registerNotification(Context con, View parenet, TableLayout tabLayout) {
        tr = new GenericTabRow(con);
        tr.sl1.autoScale = true;
        tr.sl1.autoScaleBounceBack = true;
        tr.sl1.setColor(255, 0, 150, 125);
        tr.setIcon("sensortag2", "lightsensor");
        tr.title.setText("Luxometer Data");
        tr.uuidLabel.setText(GattData);
        tr.value.setText("0.0 Lux");
        tr.periodBar.setProgress(100);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tabLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        super.registerNotificationImp(bytes -> {
            convertRaw(bytes);
            tr.value.setText(String.format("%.1f Lux", lux));
            tr.sl1.addValue(lux);
            if (DBG)
                Log.d(TAG, "Lux:" + lux);
        });
        return false;
    }

    @Override
    public boolean configuration() {
        super.configurationImp(bytes -> {
            if (DBG)
                Log.d(TAG, "Configuration");
        });
        return true;
    }

    @Override
    protected void convertRaw(byte[] bytes) {
        Integer i = u16AtOffset(bytes, 0);
        int mantissa = i & 0x0FFF;
        int exponent = (i >> 12) & 0xFF;
        float magnitude = (float)pow(2.0f, exponent);
        lux = (magnitude * mantissa) / 100.0F;
    }
}
