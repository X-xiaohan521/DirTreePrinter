package unimilk.dirtreeprinter.backend.settings;

import com.google.gson.Gson;
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
        this.settings = gson.fromJson(jsonStr, ISettings.class);
    }

    @Override
    public void saveSettings() {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(this.settings);
        try (Writer writer = Files.newBufferedWriter(this.configPath)) {
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
    }

    @Override
    public void applyAndSaveSettingsFrom(ISettings newSettings) {
        this.settings = newSettings;
        saveSettings();
    }
}
