package org.lamisplus.datafi.dao;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.models.Lab;
import org.lamisplus.datafi.models.Settings;

public class SettingsDAO {

    public static void setSetting(String name, String value) {
        Settings settings = new Select().from(Settings.class).where("name = ?", name).executeSingle();
        if (settings != null) {
            Settings loadSettings = Settings.load(Settings.class, settings.getId());
            loadSettings.setValue(value);
            loadSettings.save();
        } else {
            Settings newSettings = new Settings();
            newSettings.setName(name);
            newSettings.setValue(value);
            newSettings.save();
        }
    }

    public static String getSettings(String name) {
        Settings settings = new Select().from(Settings.class).where("name = ?", name).executeSingle();
        if (settings != null) {
            return settings.getValue();
        }
        return null;
    }
}
