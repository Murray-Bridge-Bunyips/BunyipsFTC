package org.firstinspires.ftc.teamcode.common;

import java.util.HashMap;

/**
 * Cross-OpMode storage for global variables.
 */
public class GlobalStorage {
    private static final HashMap<String, Object> storage = new HashMap<>();

    public static void put(String key, Object value) {
        storage.put(key, value);
    }

    public static Object get(String key) {
        return storage.get(key);
    }

    public static boolean contains(String key) {
        return storage.containsKey(key);
    }

    public static void remove(String key) {
        storage.remove(key);
    }

    public static void clear() {
        storage.clear();
    }
}
