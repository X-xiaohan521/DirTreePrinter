package unimilk.dirtreeprinter.backend;

import java.nio.file.Path;

public final class AppPaths {

    public static Path getConfigDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            return Path.of(appData, "DirTreePrinter");
        } else if (os.contains("mac")) {
            return Path.of(userHome, "Library", "Application Support", "DirTreePrinter");
        } else {
            return Path.of(userHome, ".config", "DirTreePrinter");
        }
    }

    /**
     * Gets the Path of YAML config file.
     * @return the Path of YAML config file
     * @deprecated since the YamlSettingsManager has been deprecated, there's no need to get the YAML config path
     */
    public static Path getYamlConfigFile() {
        return getConfigDir().resolve("config.yml");
    }

    public static Path getJsonConfigFile() {
        return getConfigDir().resolve("config.json");
    }
}
