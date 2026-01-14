package unimilk.dirtreeprinter.backend.settings;

import com.formdev.flatlaf.json.Json;
import org.junit.jupiter.api.Test;
import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.backend.AppPaths;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

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
        saveNormalSettingsShouldWork();
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

    @Test
    void loadBrokenSettingsShouldWork() {
        ISettingsManager settingsManager = new JsonSettingsManager(AppPaths.getJsonConfigFile());
        try (Writer writer = new BufferedWriter(Files.newBufferedWriter(AppPaths.getJsonConfigFile()))) {
            writer.write("{\"filterMode\":\"black\",\"rules\":[");
        } catch (IOException e) {
            fail();
        }
        settingsManager.loadSettings();
        assertEquals(new Settings(), settingsManager.getSettings());
    }

    @Test
    void loadMessedSettingsShouldWork() {
        ISettingsManager settingsManager = new JsonSettingsManager(AppPaths.getJsonConfigFile());
        try (Writer writer = new BufferedWriter(Files.newBufferedWriter(AppPaths.getJsonConfigFile()))) {
            writer.write("{\"filterMode\":\"json\",\"rules\":[],\"modified\":false}");
        } catch (IOException e) {
            fail();
        }
        settingsManager.loadSettings();
        assertEquals(new Settings(), settingsManager.getSettings());
    }
}