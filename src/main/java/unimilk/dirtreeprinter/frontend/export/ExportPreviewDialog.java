package unimilk.dirtreeprinter.frontend.export;

import unimilk.dirtreeprinter.api.tree.ITreeRenderer;
import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ExportPreviewDialog extends JDialog {
    private final JTextArea outputArea = new JTextArea();
    private String rootFolder;

    private ExportPreviewDialog(JFrame owner) {
        super(owner, true);
        setSize(600, 800);
        setLocationRelativeTo(owner);
        setTitle("Export Preview");
        setLayout(new BorderLayout());

        // prepare outputArea
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // prepare bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JButton exportButton = new JButton("Export");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> onCancel());
        exportButton.addActionListener(e -> onExport());
        buttonPanel.add(cancelButton);
        buttonPanel.add(exportButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void showExportPreviewDialog(JFrame owner, ITreeRenderer treeRenderer, TreeNode rootNode, String rootFolder) {
        ExportPreviewDialog exportPreviewDialog = new ExportPreviewDialog(owner);
        exportPreviewDialog.rootFolder = rootFolder;
        exportPreviewDialog.displayPreview(treeRenderer, rootNode);
        exportPreviewDialog.setVisible(true);
    }

    private void onExport() {
        ExportDialog exportDialog = new ExportDialog(rootFolder);

        if (exportDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = exportDialog.getSelectedFile();
            try {
                Files.writeString(
                        fileToSave.toPath(),
                        outputArea.getText()
                );
                dispose();
            } catch (IOException ex) {
                showError(ex);
            }
        }
    }

    private void displayPreview(ITreeRenderer treeRenderer, TreeNode rootNode) {
        outputArea.setText(treeRenderer.renderTree(rootNode));
    }

    private void onCancel() {
        dispose();
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
