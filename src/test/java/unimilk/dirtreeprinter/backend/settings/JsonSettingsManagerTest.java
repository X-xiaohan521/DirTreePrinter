package unimilk.dirtreeprinter.backend.settings;

import org.junit.jupiter.api.Test;
import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.backend.AppPaths;

import static org.junit.jupiter.api.Assertions.*;

class JsonSettingsManagerTest {

    @Test
    void saveNormalSettingsShouldWork() {
        ISettingsManager settingsManager = new JsonSettingsManager(AppPaths.getJsonConfigFile());
        ISettings settings = new Settings();
        settings.addRule(".idea");
        settings.addRule(".vscode");
        settings.markClean();
        settingsManager.applySettingsFrom(settings);
        settingsManager.saveSettings();
        assertTrue(true);
    }

    @Test
    void loadNormalSettingsShouldWork() {
        ISettingsManager settingsManager = new JsonSettingsManager(AppPaths.getJsonConfigFile());
        settingsManager.loadSettings();
        ISettings actualSettings = settingsManager.getSettings().copy();
        ISettings expectedSettings = new Settings();
        expectedSettings.addRule(".idea");
        expectedSettings.addRule(".vscode");
        expectedSettings.markClean();
        assertEquals(expectedSettings, actualSettings);
    }

    @Test
    void saveBrokenSettingsShouldWork() {
        ISettingsManager settingsManager = new JsonSettingsManager(AppPaths.getJsonConfigFile());
        ISettings settings = new Settings();
        settingsManager.applyAndSaveSettingsFrom(settings);
        assertTrue(true);
    }
}