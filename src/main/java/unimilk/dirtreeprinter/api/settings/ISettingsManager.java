package unimilk.dirtreeprinter.api.settings;

import unimilk.dirtreeprinter.backend.settings.Settings;

public interface ISettingsManager {
    /**
     * Load config from file.
     * @return  settings load from config file
     */
    Settings loadSettings();

    /**
     * Save config to file.
     * @param settings  the settings to be saved
     *
     */
    void saveSettings(Settings settings);
}
