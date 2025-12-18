package unimilk.dirtreeprinter.frontend;

import com.sun.tools.javac.Main;
import unimilk.dirtreeprinter.backend.DirTreeGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainFrontend extends JFrame {
    final JTextArea outputArea = new JTextArea();
    private static MainFrontend mainFrontend;

    public MainFrontend() {
        setTitle("Directory Tree Generator");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setEditable(false);

        add(new TopContainer(), BorderLayout.PAGE_START);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        mainFrontend = this;
    }

    private class TopContainer extends JPanel {

        TopContainer() {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            add(new JSeparator(SwingConstants.HORIZONTAL));
            add(createToolBar());
            add(createButtonPanel());
        }

        JToolBar createToolBar() {
            JToolBar toolBar = new JToolBar();
            toolBar.setLayout(new BorderLayout());

            toolBar.add(createFileButton(), BorderLayout.LINE_START);

            return toolBar;
        }

        JPanel createButtonPanel() {
            JButton selectButton = new JButton("Select Folder");
            JButton saveButton = new JButton("Save As...");
            JButton clearButton = new JButton("Clear All");

            selectButton.addActionListener(e -> selectFolderToScan());
            saveButton.addActionListener(e -> saveToFile());
            clearButton.addActionListener(e -> clearOutput());

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            panel.add(selectButton);
            panel.add(saveButton);
            panel.add(clearButton);
            return panel;
        }

        JButton createFileButton() {
            JButton fileButton = new JButton("File");

            JPopupMenu menu = new JPopupMenu();

            JMenuItem openItem = new JMenuItem("Open Folder");
            JMenuItem saveItem = new JMenuItem("Save As...");
            JMenuItem settingsItem = new JMenuItem("Settings...");
            JMenuItem exitItem = new JMenuItem("Exit");

            openItem.addActionListener(e -> selectFolderToScan());
            saveItem.addActionListener(e -> saveToFile());
            settingsItem.addActionListener(e -> SettingsDialog.openSettingsDialog(mainFrontend));
            exitItem.addActionListener(e -> System.exit(0));

            menu.add(openItem);
            menu.add(saveItem);
            menu.addSeparator();
            menu.add(settingsItem);
            menu.addSeparator();
            menu.add(exitItem);

            fileButton.addActionListener(e -> menu.show(fileButton, 0, fileButton.getHeight()));

            return fileButton;
        }

    }

    void selectFolderToScan() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path dir = chooser.getSelectedFile().toPath();
            try {
                outputArea.setText(DirTreeGenerator.generateTree(dir));
            } catch (IOException ex) {
                showError(ex);
            }
        }
    }

    void saveToFile() {
        if (outputArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please first choose a folder to scan.",
                    "Nothing to Output",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(
                        chooser.getSelectedFile().toPath(),
                        outputArea.getText()
                );
            } catch (IOException ex) {
                showError(ex);
            }
        }
    }

    void clearOutput() {
        if (outputArea.getText().isEmpty()) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you wish to clear the output?",
                "Clear All",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            outputArea.setText("");
        }
    }

    void showError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}