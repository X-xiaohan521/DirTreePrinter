package unimilk.dirtreeprinter.backend.settings;

import unimilk.dirtreeprinter.api.settings.FilterMode;
import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.settings.ISettingsManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlSettingsManager implements ISettingsManager {

    private final Path configPath;
    private ISettings settings;

    public YamlSettingsManager(Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public void loadSettings() {
        if (!Files.exists(configPath)) {
            saveDefaultConfig();
            return;
        }

        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(configPath)) {
            // load YAML file
            Map<String, Object> root = yaml.load(in);
            if (root == null) {
                saveDefaultConfig();
                return;
            }

            // set settings
            Settings settings = new Settings();

            // filter
            Object filterObj = root.get("filter");
            if (filterObj instanceof Map<?, ?>) {
                // mode
                Object modeObj = ((Map<?, ?>) filterObj).get("mode");
                if (modeObj instanceof String) {
                    FilterMode mode = FilterMode.of(((String) modeObj).toLowerCase());
                    if (mode != null) {
                        settings.setFilterMode(mode);
                    }
                }

                // rules
                Object rulesObj = ((Map<?, ?>) filterObj).get("rules");
                if (rulesObj instanceof List<?>) {
                    for (Object rule : (List<?>) rulesObj) {
                        if (rule instanceof String) {
                            settings.addRule((String)rule);
                        }
                    }
                }
            }
            settings.markClean();
            this.settings = settings;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load settings.", e);
        }

    }

    @Override
    public void saveSettings() {
        Yaml yaml = new Yaml();

        // filter
        Map<String, Object> filter = new LinkedHashMap<>();
        filter.put("mode", settings.getFilterMode().isBlacklist() ? "BLACKLIST" : "WHITELIST");
        filter.put("rules", settings.getRules());

        // root
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("filter", filter);

        // write YAML file
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            yaml.dump(root, writer);
            this.settings.markClean();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save settings.", e);
        }
    }

    private void saveDefaultConfig() {
        try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
            Files.createDirectories(configPath.getParent());
            assert in != null;
            Files.copy(in, configPath, StandardCopyOption.REPLACE_EXISTING);
            loadSettings();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write default config.", e);
        }
    }

    public ISettings getSettings() {
        return this.settings;
    }

    public void applySettingsFrom(ISettings newSettings) {
        this.settings = newSettings;
    }

    public void applyAndSaveSettingsFrom(ISettings newSettings) {
        this.settings = newSettings;
        saveSettings();
    }

}
