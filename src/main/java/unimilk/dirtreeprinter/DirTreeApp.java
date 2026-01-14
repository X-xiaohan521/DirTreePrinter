package unimilk.dirtreeprinter;

import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.backend.AppPaths;
import unimilk.dirtreeprinter.backend.settings.JsonSettingsManager;
import unimilk.dirtreeprinter.backend.tree.DirTreeGenerator;
import unimilk.dirtreeprinter.frontend.MainFrontend;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.nio.file.Path;

public class DirTreeApp {

    private static MainFrontend mainFrontend;

    public static void main(String[] args) {

        // load config path
        Path configPath = AppPaths.getJsonConfigFile();

        // load settings
        ISettingsManager settingsManager = new JsonSettingsManager(configPath);
        settingsManager.loadSettings();

        // load frontend
        FlatDarkLaf.setup();
        mainFrontend = new MainFrontend(settingsManager, new DirTreeGenerator());
        SwingUtilities.invokeLater(() -> mainFrontend.setVisible(true));
    }
}
