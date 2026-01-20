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
        buttonPanel.add(cancelButton);
        buttonPanel.add(exportButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void showExportPreviewDialog(JFrame owner, ITreeRenderer treeRenderer, TreeNode rootNode) {
        ExportPreviewDialog exportPreviewDialog = new ExportPreviewDialog(owner);
        exportPreviewDialog.displayPreview(treeRenderer, rootNode);
        exportPreviewDialog.setVisible(true);
    }

//    private void onExport() {
//        ExportDialog exportDialog = new ExportDialog(this);
//
//        if (exportDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//            File fileToSave = exportDialog.getSelectedFile();
//            try {
//                Files.writeString(
//                        fileToSave.toPath(),
//                        treeRenderer.renderTree(rootNode)
//                );
//            } catch (IOException ex) {
//                showError(ex);
//            }
//        }
//    }

    private void displayPreview(ITreeRenderer treeRenderer, TreeNode rootNode) {
        outputArea.setText(treeRenderer.renderTree(rootNode));
    }

    private void onCancel() {
        dispose();
    }
}
