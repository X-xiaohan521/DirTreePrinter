package unimilk.dirtreeprinter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.formdev.flatlaf.FlatDarkLaf;

public class DirTreeApp extends JFrame {

    private final JTextArea outputArea = new JTextArea();

    public DirTreeApp() {
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

    private JPanel createButtonPanel() {
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

    private void selectFolderToScan() {
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

    private void saveToFile() {
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

    private void clearOutput() {
        if (outputArea.getText().isEmpty()) {return;}
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

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DirTreeApp().setVisible(true));
        // DirTreeGUI GUI = new DirTreeGUI();
    }
}
