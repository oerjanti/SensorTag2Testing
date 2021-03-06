package com.uti.sensors.bleshow.Devices;

import com.uti.sensors.bleshow.DeviceViewFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DeviceContext {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DeviceItem> ITEMS = new ArrayList<DeviceItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DeviceItem> ITEM_MAP = new HashMap<String, DeviceItem>();

    public static final Map<String, String> Names = new HashMap<String, String>();


    private static final int COUNT = 1;

    public static void createMacWithName(String mac, String name) {
        Names.put(mac, name);
    }

    public static String queryName(String mac) {
        return Names.get(mac);
    }

    public static boolean CheckDeviceExist(final String mac) {
        return (ITEM_MAP.get(mac) != null);
    }

    public static int AddorUpdateDevice(String mac, int rssi) {
        DeviceItem dev = ITEM_MAP.get(mac);
        if (dev == null) {
            String name = Names.get(mac);

            if (name == null) {
                name = new String(mac);
            }
            dev = new DeviceItem(mac, name, rssi);
            dev.position = ITEMS.size();
            addItem(dev);
            return -1;
        }

        dev.nRSSI = rssi;
        return dev.position;
    }

    private static void addItem(DeviceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.MAC, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DeviceItem {
        public final String MAC;
        public final String name;
        public int nRSSI;
        // public boolean bConnected;
        public enum CONNECT_STATE {
            DISCONNECTING, CONNECTING,
            CONNECTED, DISCONNECTED
        };

        public CONNECT_STATE state;
        public int position;
        public DeviceViewFragment fragment;

        public DeviceItem(String mac, String name, int rssi) {
            this.MAC = mac;
            this.name = name;
            this.nRSSI = rssi;
            this.state = CONNECT_STATE.DISCONNECTED;
            this.position = 0;
            this.fragment = null;
        }

        @Override
        public String toString() {
            return MAC;
        }
    }
}
