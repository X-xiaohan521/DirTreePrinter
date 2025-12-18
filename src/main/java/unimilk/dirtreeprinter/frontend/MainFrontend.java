package unimilk.dirtreeprinter.frontend;

import com.formdev.flatlaf.FlatDarkLaf;
import unimilk.dirtreeprinter.backend.DirTreeGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainFrontend extends JFrame {
    final JTextArea outputArea = new JTextArea();

    public MainFrontend() {
        FlatDarkLaf.setup();
        setTitle("Directory Tree Generator");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(Color.DARK_GRAY);
        outputArea.setForeground(Color.WHITE);
        outputArea.setEditable(false);

        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.PAGE_START);
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

    void selectFolderToScan() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
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
                    null,
                    "Please first choose a folder to scan.",
                    "Nothing to Output",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
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
                null,
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
                null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}