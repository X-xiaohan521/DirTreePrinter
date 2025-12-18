package unimilk.dirtreeprinter;

import unimilk.dirtreeprinter.frontend.MainFrontend;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class DirTreeApp {

    private static MainFrontend mainFrontend;

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        mainFrontend = new MainFrontend();
        SwingUtilities.invokeLater(() -> mainFrontend.setVisible(true));
    }
}
