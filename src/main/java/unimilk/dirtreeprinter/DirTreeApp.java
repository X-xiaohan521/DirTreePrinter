package unimilk.dirtreeprinter;

import unimilk.dirtreeprinter.frontend.MainFrontend;

import javax.swing.*;

public class DirTreeApp {

    private static MainFrontend mainFrontend;

    public static void main(String[] args) {
        mainFrontend = new MainFrontend();
        SwingUtilities.invokeLater(() -> mainFrontend.setVisible(true));
    }
}
