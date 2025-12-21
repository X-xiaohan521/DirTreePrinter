package unimilk.dirtreeprinter.api.settings;

public interface ISettingsManager {
    /**
     * Load config from file.
     */
    void loadSettings();

    /**
     * Save current settings to file.
     *
     */
    void saveSettings();
}
