package com.stemroom.dao;

import java.util.List;

/** Reads and saves rows in the settings table. */
public class SettingDao {

    /** The saved value, or the fallback when the setting has never been saved. */
    public String get(String key, String fallback) {
        List<String> rows = Db.query("SELECT setting_value FROM settings WHERE setting_key = ?",
                rs -> rs.getString(1), key);
        return rows.isEmpty() ? fallback : rows.get(0);
    }

    /** Inserts the setting, or changes it if it is already there. */
    public void save(String key, String value) {
        Db.update("INSERT INTO settings (setting_key, setting_value) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value)", key, value);
    }
}
