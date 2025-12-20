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

    public static Path getConfigFile() {
        return getConfigDir().resolve("config.yml");
    }
}
