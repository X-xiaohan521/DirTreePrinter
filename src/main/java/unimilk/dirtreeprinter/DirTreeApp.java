package unimilk.dirtreeprinter;

import com.formdev.flatlaf.*;
import unimilk.dirtreeprinter.api.settings.FilterMode;
import unimilk.dirtreeprinter.backend.AppPaths;
import unimilk.dirtreeprinter.backend.settings.Settings;
import unimilk.dirtreeprinter.backend.settings.SettingsManager;
import unimilk.dirtreeprinter.frontend.MainFrontend;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.nio.file.Path;

public class DirTreeApp {

    private static MainFrontend mainFrontend;

    public static void main(String[] args) {

        // load config path
        Path configPath = AppPaths.getConfigFile();

        // load settings
        SettingsManager settingsManager = new SettingsManager(configPath);
        settingsManager.loadSettings();

        // load frontend
        FlatDarkLaf.setup();
        mainFrontend = new MainFrontend();
        SwingUtilities.invokeLater(() -> mainFrontend.setVisible(true));
    }

}
