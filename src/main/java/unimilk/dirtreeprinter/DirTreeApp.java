package unimilk.dirtreeprinter;

import com.formdev.flatlaf.*;
import unimilk.dirtreeprinter.backend.AppPaths;
import unimilk.dirtreeprinter.backend.settings.SettingsManager;
import unimilk.dirtreeprinter.backend.tree.DirTreeGenerator;
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
        mainFrontend = new MainFrontend(settingsManager, new DirTreeGenerator());
        SwingUtilities.invokeLater(() -> mainFrontend.setVisible(true));
    }
}
