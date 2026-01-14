package unimilk.dirtreeprinter.frontend;

import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.api.tree.IDirTreeGenerator;
import unimilk.dirtreeprinter.api.tree.TreeNode;
import unimilk.dirtreeprinter.frontend.settings.SettingsDialog;
import unimilk.dirtreeprinter.frontend.tree.CheckBoxTreeCellRenderer;

import java.util.List;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class MainFrontend extends JFrame {

    private static MainFrontend mainFrontend;
    private String rootFolder;
    private final ISettingsManager settingsManager;
    private final IDirTreeGenerator dirTreeGenerator;
    private final TreeDisplayArea treeDisplayArea;

    public MainFrontend(ISettingsManager settingsManager, IDirTreeGenerator dirTreeGenerator) {
        this.settingsManager = settingsManager;
        this.dirTreeGenerator = dirTreeGenerator;

        setTitle("Directory Tree Generator");
        List<Image> icons = List.of(
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon16.png"))).getImage(),
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon32.png"))).getImage(),
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon48.png"))).getImage(),
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon64.png"))).getImage()
        );
        setIconImages(icons);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        treeDisplayArea = new TreeDisplayArea();

        add(new TopContainer(), BorderLayout.PAGE_START);
        add(new JScrollPane(treeDisplayArea), BorderLayout.CENTER);

        mainFrontend = this;
    }

    public String getRootFolder() {
        return rootFolder;
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
            settingsItem.addActionListener(e -> SettingsDialog.openSettingsDialog(mainFrontend, settingsManager));
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

    private class TreeDisplayArea extends JPanel {

        private TreeModel treeModel;
        private JTree tree;

        TreeDisplayArea() {
            setVisible(false);
        }

        void generateUiTree(TreeNode rootNode) {
            DefaultMutableTreeNode rootUiNode = generateUiTreeNode(rootNode);
            treeModel = new DefaultTreeModel(rootUiNode);
        }

        void showTree() {
            tree = new JTree(treeModel);
            tree.setCellRenderer(new CheckBoxTreeCellRenderer());
            add(tree);
            tree.setVisible(true);
            this.setVisible(true);
        }

        DefaultMutableTreeNode generateUiTreeNode(TreeNode backendNode) {
            DefaultMutableTreeNode uiNode = new DefaultMutableTreeNode(backendNode);
            for (TreeNode child : backendNode.getChildren()) {
                uiNode.add(generateUiTreeNode(child));
            }
            return uiNode;
        }

        public boolean isEmpty() {
            // TODO
            return true;
        }

        public void clear() {
            tree.setVisible(false);
            this.setVisible(false);
        }
    }

    void selectFolderToScan() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path dir = chooser.getSelectedFile().toPath();
            try {
                TreeNode rootNode = dirTreeGenerator.generateTree(dir, settingsManager.getSettings());
                treeDisplayArea.generateUiTree(rootNode);
                treeDisplayArea.showTree();
                rootFolder = dir.getFileName().toString();
            } catch (IOException ex) {
                showError(ex);
            }
        }
    }

    void saveToFile() {
        if (treeDisplayArea.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please first choose a folder to scan.",
                    "Nothing to Output",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        SaveDialog saveDialog = new SaveDialog(this);

        if (saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveDialog.getSelectedFile();
            try {
                Files.writeString(
                        fileToSave.toPath(),
                        // TODO render tree to string
                        "outputArea.getText()"
                );
            } catch (IOException ex) {
                showError(ex);
            }
        }
    }

    void clearOutput() {
        if (treeDisplayArea.isEmpty()) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you wish to clear the output?",
                "Clear All",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            treeDisplayArea.clear();
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