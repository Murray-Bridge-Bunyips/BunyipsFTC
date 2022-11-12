package org.firstinspires.ftc.teamcode.common;

import java.util.HashMap;

/**
 * Global storage class for storing instance variables and results for usage across different classes
 */
public class GlobalStorage {
    private volatile HashMap<String, String> storage;

    public GlobalStorage() {
        storage = new HashMap<>();
    }

    public void updateItem(String key, String value) {
        storage.remove(key);
        storage.put(key, value);
    }

    public String getItem(String key) {
        return storage.get(key);
    }

    public void clearStorage() {
        storage.clear();
    }
}
