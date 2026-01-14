package unimilk.dirtreeprinter.backend.settings;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.settings.ISettingsManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonSettingsManager implements ISettingsManager {
    private final Path configPath;
    private ISettings settings;

    public JsonSettingsManager(Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public void loadSettings() {
        Gson gson = new Gson();
        String jsonStr;
        try (BufferedReader reader = Files.newBufferedReader(this.configPath)) {
            jsonStr = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load settings.", e);
        }
        try {
            this.settings = gson.fromJson(jsonStr, Settings.class);
            if (this.settings.getFilterMode() == null) {
                saveDefaultSettings();
            }
        } catch (JsonSyntaxException e) {
            saveDefaultSettings();
        }
    }

    @Override
    public void saveSettings() {
        Gson gson = new Gson();
        try (Writer writer = Files.newBufferedWriter(this.configPath)) {
            this.settings.markClean();
            String jsonStr = gson.toJson(this.settings);
            writer.write(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save settings.", e);
        }
    }

    @Override
    public ISettings getSettings() {
        return this.settings;
    }

    @Override
    public void applySettingsFrom(ISettings newSettings) {
        this.settings = newSettings;
        this.settings.markClean();
    }

    @Override
    public void applyAndSaveSettingsFrom(ISettings newSettings) {
        this.settings = newSettings;
        saveSettings();
    }

    @Override
    public void saveDefaultSettings() {
        this.settings = new Settings();
        saveSettings();
    }
}
