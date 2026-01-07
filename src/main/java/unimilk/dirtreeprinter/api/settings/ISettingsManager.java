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

    /**
     * Get the currently using Settings.
     * @return the single truth of Settings instance
     */
    ISettings getSettings();

    /**
     * Apply the new Settings to the currently running application.
     * @param newSettings the new Settings to be applied
     */
    void applySettingsFrom(ISettings newSettings);

    /**
     * Apply and save the new Settings.
     * @param newSettings the new Settings to be applied and saved
     */
    void applyAndSaveSettingsFrom(ISettings newSettings);
}
